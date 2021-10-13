package org.yx.http.log;

import static org.yx.conf.AppInfo.LN;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.yx.exception.BizException;
import org.yx.http.act.HttpActions;
import org.yx.http.handler.WebContext;
import org.yx.http.kit.HttpSettings;
import org.yx.log.Log;
import org.yx.util.StringUtil;

public class PlainHttpLogHandler implements HttpLogHandler {
	protected Logger log = Log.get("sumk.http.log");

	@Override
	public void log(WebContext ctx, HttpServletRequest req, Throwable ex, final long time) {
		if (ex != null && log.isErrorEnabled()) {
			if (ctx != null) {
				logError(ctx, ex, time);
			} else {
				logError(req, ex, time);
			}
			return;
		}
		if (time >= HttpSettings.warnTime() && log.isWarnEnabled()) {
			log.warn(buildLogMsg(ctx, req, time));
			return;
		}

		if (time >= HttpSettings.infoTime() && log.isInfoEnabled()) {
			log.info(buildLogMsg(ctx, req, time));
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug(buildLogMsg(ctx, req, time));
			return;
		}
	}

	protected void logError(HttpServletRequest req, Throwable ex, long time) {
		StringBuilder sb = newStringBuilder(req);
		sb.append(req.getRequestURI()).append("   remote:").append(remoteAddr(req)).append("   time:").append(time);
		logError(sb.toString(), ex);
	}

	protected void logError(WebContext wc, Throwable ex, long time) {
		StringBuilder sb = newStringBuilder(wc.httpRequest());
		sb.append(getRawAct(wc)).append("   remote:").append(remoteAddr(wc.httpRequest())).append("   time:")
				.append(time).append(LN).append("   param: ").append(getParam(wc, HttpSettings.maxReqLogSize()));
		logError(sb.toString(), ex);
	}

	protected void logError(String msg, Throwable ex) {
		if (ex instanceof BizException) {
			log.warn(msg, ex);
			return;
		}
		log.error(msg, ex);
	}

	protected StringBuilder newStringBuilder(HttpServletRequest req) {
		return new StringBuilder(64).append("[").append(req.getMethod()).append("] ");
	}

	protected String buildLogMsg(WebContext wc, HttpServletRequest req, long time) {
		StringBuilder sb = newStringBuilder(req);
		if (wc != null) {
			sb.append(getRawAct(wc)).append("   remote:").append(remoteAddr(req)).append("   time:").append(time)
					.append(LN).append("   param: ").append(getParam(wc, HttpSettings.maxReqLogSize())).append(LN)
					.append("   resp: ").append(getResponse(wc, HttpSettings.maxRespLogSize()));
			return sb.toString();
		}
		sb.append(req.getRequestURI()).append("   time:").append(time);
		return sb.toString();
	}

	protected String remoteAddr(HttpServletRequest req) {
		String ip = req.getHeader("X-Real-IP");
		return StringUtil.isNotEmpty(ip) ? ip : req.getRemoteAddr();
	}

	protected String getParam(WebContext wc, int maxLength) {
		return HttpLogs.getParam(wc, maxLength);
	}

	protected String getResponse(WebContext wc, int maxLength) {
		return HttpLogs.getResponse(wc, maxLength);
	}

	protected String getRawAct(WebContext wc) {
		String act = wc.rawAct();
		return act.endsWith(HttpActions.PREFIX_MATCH_ENDING) ? wc.httpRequest().getPathInfo() : act;
	}
}
