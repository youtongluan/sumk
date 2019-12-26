package org.yx.http.log;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.yx.exception.BizException;
import org.yx.http.HttpSettings;
import org.yx.http.handler.WebContext;
import org.yx.log.Log;

public class PlainHttpLogHandler implements HttpLogHandler {
	private static final String LN = "\n";
	private Logger log = Log.get("sumk.http.log");

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
		StringBuilder sb = new StringBuilder();
		sb.append(req.getRequestURI()).append("   time:").append(time);
		logError(sb.toString(), ex);
	}

	protected void logError(WebContext wc, Throwable ex, long time) {
		StringBuilder sb = new StringBuilder();
		sb.append(wc.act()).append("   time:").append(time).append(LN).append("   param: ")
				.append(HttpLogs.getParam(wc, HttpSettings.maxReqLogSize()));
		logError(sb.toString(), ex);
	}

	protected void logError(String msg, Throwable ex) {
		if (BizException.class.isInstance(ex)) {
			log.warn(msg, ex);
			return;
		}
		log.error(msg, ex);
	}

	protected String buildLogMsg(WebContext wc, HttpServletRequest req, long time) {
		StringBuilder sb = new StringBuilder();
		if (wc != null) {
			sb.append(wc.act()).append("   time:").append(time).append(LN).append("   param: ")
					.append(HttpLogs.getParam(wc, HttpSettings.maxReqLogSize())).append(LN).append("   resp: ")
					.append(HttpLogs.getResponse(wc, HttpSettings.maxRespLogSize()));
			return sb.toString();
		}
		sb.append(req.getRequestURI()).append("   time:").append(time);
		return sb.toString();
	}
}
