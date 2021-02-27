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
import java.util.Map;
import java.util.function.BiConsumer;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.conf.AppInfo;
import org.yx.http.HttpErrorCode;
import org.yx.http.kit.HttpSettings;
import org.yx.http.kit.InnerHttpUtil;

public abstract class AbstractCommonHttpServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;
	private static final String METHOD_OPTIONS = "OPTIONS";

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		if (!(req instanceof HttpServletRequest && res instanceof HttpServletResponse)) {
			throw new ServletException("non-HTTP request or response");
		}
		service((HttpServletRequest) req, (HttpServletResponse) res);
	}

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (HttpSettings.responseHeaders() != null) {
			for (Map.Entry<String, String> entry : HttpSettings.responseHeaders().entrySet()) {
				resp.setHeader(entry.getKey(), entry.getValue());
			}
		}
		String method = req.getMethod().toUpperCase();
		if (!HttpSettings.allHttpMethods().contains(method)) {
			if (METHOD_OPTIONS.equals(method)) {
				this.doOptions(req, resp);
				return;
			}
			InnerHttpUtil.sendError(resp, HttpErrorCode.METHOD_UNSUPPORT,
					AppInfo.get("sumk.http.method.notsupport", "NOT SUPPORTED"), AppInfo.UTF8);
			return;
		}
		this.handle(req, resp);
	}

	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BiConsumer<HttpServletRequest, HttpServletResponse> h = InnerHttpUtil.getOptionMethodHandler();
		if (h != null) {
			h.accept(req, resp);
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String m : HttpSettings.allHttpMethods()) {
			sb.append(m).append(", ");
		}
		sb.append(METHOD_OPTIONS);
		String allow = sb.toString();
		resp.setHeader("Allow", allow);
	}

	protected abstract void handle(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException;

}
