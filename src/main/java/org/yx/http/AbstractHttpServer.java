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
package org.yx.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.yx.common.SumkLogs;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.handler.HttpActionNode;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.util.UUIDSeed;

public abstract class AbstractHttpServer extends HttpServlet {

	private static final long serialVersionUID = 74378082364534491L;

	protected Logger log = SumkLogs.HTTP_LOG;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!InnerHttpUtil.checkGetMethod(resp)) {
			return;
		}
		this.handle(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.handle(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "not allowd");
	}

	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "not allowd");
	}

	protected void handle(HttpServletRequest req, HttpServletResponse resp) {
		try {
			setRespHeader(req, resp);

			final String act = getParser().parse(req);
			log.trace("act={}", act);
			if (act == null || act.isEmpty()) {
				log.error("act is empty in {}?{}", req.getPathInfo(), req.getQueryString());
				errorAndLog(resp, HttpErrorCode.ACT_FORMAT_ERROR, "请求格式不正确", req);
				return;
			}
			if (HttpSettings.getFusing().contains(act)) {
				log.error("{} is in fusing", act);
				errorAndLog(resp, HttpErrorCode.FUSING, AppInfo.get("sumk.http.errorcode.fusing", "请求出错"), req);
				return;
			}
			HttpActionNode info = HttpActionHolder.getHttpInfo(act);
			if (info == null) {
				InnerHttpUtil.actNotFound(req, resp, act);
				return;
			}
			info.checkThreshold();
			HttpContextHolder.set(req, resp);
			ActionContext.newContext(act, UUIDSeed.seq18(), req.getParameter("thisIsTest"));
			handle(act, info, req, resp);

		} catch (Exception e) {
			if (BizException.class.isInstance(e)) {
				BizException be = BizException.class.cast(e);
				errorAndLog(resp, be.getCode(), be.getMessage(), req);
			} else {
				log.error(e.toString(), e);
				errorAndLog(resp, HttpErrorCode.FRAMEWORK_ERROR, "请求处理异常", req);
			}
		} finally {
			HttpContextHolder.remove();
			ActionContext.remove();
		}
	}

	protected void errorAndLog(HttpServletResponse resp, int code, String errorMsg, HttpServletRequest req) {
		try {
			InnerHttpUtil.error(req, resp, code, errorMsg);
		} catch (IOException e) {
			log.error(e.toString(), e);
		}
	}

	protected ActParser getParser() {
		return ActParser.pathActParser;
	}

	protected void setRespHeader(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json;charset=" + InnerHttpUtil.charset(req));
	}

	protected abstract void handle(String act, HttpActionNode info, HttpServletRequest req, HttpServletResponse resp)
			throws Exception;
}
