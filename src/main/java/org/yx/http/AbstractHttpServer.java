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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.yx.common.LogType;
import org.yx.common.ThreadContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.handler.HttpNode;
import org.yx.util.StringUtil;

public abstract class AbstractHttpServer extends HttpServlet {

	private static final long serialVersionUID = 74378082364534491L;
	private static Set<String> FUSING = Collections.emptySet();

	static {
		AppInfo.addObserver((a, b) -> {
			String fusing = AppInfo.get("http.fusing", null);
			if (fusing == null) {
				FUSING = Collections.emptySet();
			} else {
				Set<String> set = new HashSet<>();
				String[] fs = StringUtil.splitByComma(fusing);
				for (String f : fs) {
					set.add(f.trim());
				}
				FUSING = set;
			}
		});
	}

	protected Logger log = LogType.HTTP_LOG;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		try {
			setRespHeader(req, resp);

			final String act = getParser().parse(req);
			log.trace("act={}", act);
			if (act == null || act.isEmpty()) {
				log.error("act is empty in {}?{}", req.getPathInfo(), req.getQueryString());
				InnerHttpUtil.error(resp, -1002, "请求格式不正确", InnerHttpUtil.charset(req));
				return;
			}
			if (FUSING.contains(act)) {
				log.error("{} is in fusing", act);
				InnerHttpUtil.error(resp, ErrorCode.FUSING, AppInfo.get("http.errorcode.fusing", "请求出错"),
						InnerHttpUtil.charset(req));
				return;
			}
			HttpNode info = HttpActionHolder.getHttpInfo(act);
			if (info == null) {
				log.error(act + " donot found handler");
				InnerHttpUtil.error(resp, -1003, "请求格式不正确", InnerHttpUtil.charset(req));
				return;
			}
			ThreadContext.httpContext(act, req.getParameter("thisIsTest"));
			handle(act, info, req, resp);

		} catch (Exception e) {
			if (BizException.class.isInstance(e)) {
				log.info("code:{},message:{}", BizException.class.cast(e).getCode(), e.getMessage());
			} else {
				log.error(e.toString(), e);
			}
			try {
				InnerHttpUtil.error(resp, -1005, "请求格式不正确", InnerHttpUtil.charset(req));
			} catch (IOException e1) {
				log.error(e.toString(), e);
			}
		} finally {
			ThreadContext.remove();
		}
	}

	protected abstract ActParser getParser();

	protected void setRespHeader(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json;charset=" + InnerHttpUtil.charset(req));
	}

	protected abstract void handle(String act, HttpNode info, HttpServletRequest req, HttpServletResponse resp)
			throws Exception;
}
