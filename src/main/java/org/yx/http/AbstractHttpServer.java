/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.ThreadContext;
import org.yx.conf.AppInfo;
import org.yx.http.handler.HttpInfo;
import org.yx.log.Log;

/**
 * 
 * @author Administrator
 */
public abstract class AbstractHttpServer extends HttpServlet {

	private static final long serialVersionUID = 74378082364534491L;

	private static boolean PATH_CHECK;
	static {
		AppInfo.addObserver((a, b) -> {
			AbstractHttpServer.PATH_CHECK = AppInfo.getBoolean("http.path.check", false);
		});
	}

	private boolean validPath(Class<?> actionClz, String path) {
		String pname = actionClz.getName();
		String[] names = pname.split("\\.");
		for (int i = 2; i < names.length; i++) {
			if (names[i].equals(path)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		try {
			setRespHeader(req, resp);

			final String act = req.getParameter("act");
			String path = req.getPathInfo();
			Log.get(this.getClass()).trace("{}?act={}", path, act);
			if (PATH_CHECK) {
				if (path.endsWith("/")) {
					path = path.substring(0, path.length() - 1);
				}
				int index = path.lastIndexOf("/");
				if (index < 0) {
					Log.get(this.getClass()).error(path + " path error");
					HttpUtil.error(resp, -1001, "请求格式不正确", HttpUtil.charset(req));
					return;
				}
				path = path.substring(index + 1);
			}
			if (act == null) {
				Log.get(this.getClass()).error("act is empty");
				HttpUtil.error(resp, -1002, "请求格式不正确", HttpUtil.charset(req));
				return;
			}
			HttpInfo info = HttpHolder.getHttpInfo(act);
			if (info == null) {
				Log.get(this.getClass()).error(act + " donot found handler");
				HttpUtil.error(resp, -1003, "请求格式不正确", HttpUtil.charset(req));
				return;
			}
			if (PATH_CHECK && !validPath(info.getObj().getClass(), path)) {
				Log.get(this.getClass()).error(act + " in error package");
				HttpUtil.error(resp, -1004, "请求的模块不正确", HttpUtil.charset(req));
				return;
			}
			ThreadContext.httpContext(act);
			handle(act, info, req, resp);

		} catch (Exception e) {
			Log.printStack(e);
			try {
				HttpUtil.error(resp, -1005, "请求格式不正确", HttpUtil.charset(req));
			} catch (IOException e1) {
				Log.printStack(e);
			}
		} finally {
			ThreadContext.remove();
		}
	}

	protected void setRespHeader(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json;charset=" + HttpUtil.charset(req));
	}

	protected abstract void handle(String act, HttpInfo info, HttpServletRequest req, HttpServletResponse resp)
			throws Exception;
}
