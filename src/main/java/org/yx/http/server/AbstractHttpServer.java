/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.http.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.exception.InvalidParamException;
import org.yx.http.HttpContextHolder;
import org.yx.http.HttpErrorCode;
import org.yx.http.act.HttpActionInfo;
import org.yx.http.act.HttpActions;
import org.yx.http.handler.WebContext;
import org.yx.http.kit.HttpSettings;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.log.HttpLogs;
import org.yx.log.Logs;
import org.yx.util.M;
import org.yx.util.StringUtil;
import org.yx.util.UUIDSeed;

public abstract class AbstractHttpServer extends AbstractCommonHttpServlet {

	private static final long serialVersionUID = 74378082364534491L;

	protected final Logger log = Logs.http();

	protected void handle(HttpServletRequest req, HttpServletResponse resp) {
		final long beginTime = System.currentTimeMillis();
		Throwable ex = null;
		WebContext wc = null;
		try {
			final Charset charset = InnerHttpUtil.charset(req);
			this.setRespHeader(req, resp, charset);

			String rawAct = getRawAct(req);
			log.trace("raw act={}", rawAct);
			if (rawAct == null || rawAct.isEmpty()) {
				log.error("raw act is empty in {}?{}", req.getPathInfo(), req.getQueryString());
				throw BizException.create(HttpErrorCode.ACT_FORMAT_ERROR,
						M.get("sumk.http.error.actformat", "请求格式不正确", rawAct));
			}
			HttpActionInfo info = HttpActions.getHttpInfo(rawAct);
			if (info == null) {
				throw BizException.create(HttpErrorCode.ACT_NOT_FOUND,
						M.get("sumk.http.error.act.notfound", "接口不存在", rawAct));
			}
			rawAct = info.rawAct();
			if (info.node().overflowThreshold()) {
				throw BizException.create(HttpErrorCode.THREAD_THRESHOLD_OVER, "系统限流降级");
			}
			HttpContextHolder.set(req, resp);
			String traceId = UUIDSeed.seq18();
			ActionContext.newContext(rawAct, traceId, req.getParameter("thisIsTest"));
			setTraceIdForResponse(req, resp, traceId);
			wc = new WebContext(rawAct, info.node(), req, resp, beginTime, charset);
			wc.setAttach(info.getAttach());
			handle(wc);

		} catch (Throwable e) {
			try {
				ex = handleError(req, resp, e);
			} catch (Exception e2) {
				ex = e;
				log.error("处理异常发生错误。可能是网络问题，也可能是异常处理出问题(不该发生)", e2);
			}
		} finally {
			long time = System.currentTimeMillis() - beginTime;
			HttpLogs.log(wc, req, ex, time);
			HttpContextHolder.clear();
			ActionContext.remove();
			if (wc != null) {

				InnerHttpUtil.record(wc.rawAct(), time, ex == null && !wc.isFailed());
			}
		}
	}

	protected void setTraceIdForResponse(HttpServletRequest req, HttpServletResponse resp, String traceId) {
		resp.setHeader(HttpSettings.traceHeaderName(), traceId);
	}

	protected void sendError(HttpServletRequest req, HttpServletResponse resp, int code, String errorMsg) {
		InnerHttpUtil.sendError(resp, code, errorMsg, InnerHttpUtil.charset(req));
	}

	protected String getRawAct(HttpServletRequest req) {
		String act = req.getPathInfo();
		if (StringUtil.isEmpty(act) && AppInfo.getBoolean("sumk.http.act.query", false)) {
			return req.getParameter("act");
		}
		return act;
	}

	protected void setRespHeader(HttpServletRequest req, HttpServletResponse resp, Charset charset) throws IOException {
		InnerHttpUtil.setRespHeader(resp, charset);
	}

	protected abstract void handle(WebContext wc) throws Throwable;

	protected Throwable handleError(HttpServletRequest req, HttpServletResponse resp, Throwable e) {
		Throwable temp = e;
		if (InvocationTargetException.class.isInstance(temp)) {
			temp = ((InvocationTargetException) temp).getTargetException();
		}
		if (InvalidParamException.class.isInstance(temp)) {
			sendError(req, resp, HttpErrorCode.VALIDATE_ERROR, temp.getMessage());
			return temp;
		}
		Throwable root = temp;
		do {
			if (BizException.class.isInstance(temp)) {
				BizException be = (BizException) temp;
				String msg2 = AppInfo.get("sumk.http.error." + be.getCode(), null);
				if (msg2 != null && msg2.length() > 0) {
					be = BizException.create(be.getCode(), msg2);
				}
				sendError(req, resp, be.getCode(), be.getMessage());
				return be;
			}
		} while ((temp = temp.getCause()) != null);

		sendError(req, resp, HttpErrorCode.HANDLE_ERROR,
				M.get("sumk.http.error." + HttpErrorCode.HANDLE_ERROR, "请求处理异常"));
		return root;
	}
}
