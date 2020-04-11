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
package org.yx.http.user;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.UnsafeByteArrayOutputStream;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.http.HttpErrorCode;
import org.yx.http.HttpHeaderName;
import org.yx.http.kit.HttpSettings;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.log.HttpLogs;
import org.yx.log.Log;
import org.yx.util.S;
import org.yx.util.StringUtil;
import org.yx.util.UUIDSeed;

public abstract class AbstractLoginServlet implements LoginServlet {

	private static final String LOGIN_NAME = "*login*";
	private UserSession session;

	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) {
		final long beginTime = System.currentTimeMillis();
		Throwable ex = null;
		Charset charset = InnerHttpUtil.charset(req);
		try {
			if (!acceptMethod(req, resp)) {
				Log.get("sumk.http.login").warn("不是有效的method，只能使用GET或POST");
				return;
			}
			InnerHttpUtil.setRespHeader(resp, charset);
			final String sid = createSessionId(req);
			String user = getUserName(req);
			LoginObject obj = login(sid, user, req);
			if (obj == null) {
				InnerHttpUtil.sendError(resp, HttpErrorCode.LOGINFAILED, user + " : login failed", charset);
				return;
			}
			if (obj.getErrorMsg() != null) {
				InnerHttpUtil.sendError(resp, HttpErrorCode.LOGINFAILED, obj.getErrorMsg(), charset);
				return;
			}
			byte[] key = createEncryptKey(req);
			boolean singleLogin = WebSessions.isSingleLogin();
			if (!session.setSession(sid, obj.getSessionObject(), key, singleLogin)) {
				Log.get("sumk.http.login").debug("{} :sid:{} login failed", user, sid);
				InnerHttpUtil.sendError(resp, HttpErrorCode.LOGINFAILED, user + " : login failed", charset);
				return;
			}
			String userId = obj.getSessionObject().getUserId();
			resp.setHeader(HttpHeaderName.sessionId(), sid);
			if (StringUtil.isNotEmpty(userId)) {
				resp.setHeader(HttpHeaderName.userFlag(), userId);
			}
			UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream(256);
			outputKey(out, key, req, resp);
			if (HttpSettings.isCookieEnable()) {
				String contextPath = req.getContextPath();

				if (!contextPath.startsWith("/")) {
					contextPath = "/" + contextPath;
				}
				String attr = ";Path=".concat(contextPath);
				setSessionCookie(req, resp, sid, attr);
				setUserFlagCookie(req, resp, userId, attr);
			}

			out.write(new byte[] { '\t', '\n' });
			if (obj.getResponseData() != null) {
				out.write(obj.getResponseData().getBytes(charset));
			}
			resp.getOutputStream().write(out.toByteArray());
		} catch (Throwable e) {
			ex = e;
			Log.get("sumk.http.login").error(e.getLocalizedMessage(), e);
			InnerHttpUtil.sendError(resp, HttpErrorCode.LOGINFAILED, "login fail", charset);
		} finally {
			long time = System.currentTimeMillis() - beginTime;
			HttpLogs.log(null, req, ex, time);
			ActionContext.remove();
			InnerHttpUtil.record(LOGIN_NAME, time, ex == null);
		}

	}

	protected boolean acceptMethod(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String method = req.getMethod().toUpperCase();
		if (!"GET".equals(method) && !"POST".equals(method)) {
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, method + " not allowd");
			return false;
		}
		return true;
	}

	protected String getUserName(HttpServletRequest req) {
		return req.getParameter(AppInfo.get("sumk.http.username", "username"));
	}

	protected void setSessionCookie(HttpServletRequest req, HttpServletResponse resp, final String sid, String attr) {
		StringBuilder cookie = new StringBuilder();

		cookie.append(HttpHeaderName.sessionId()).append('=').append(sid).append(attr);

		resp.addHeader("Set-Cookie", cookie.toString());
	}

	protected void setUserFlagCookie(HttpServletRequest req, HttpServletResponse resp, String userId, String attr) {
		if (WebSessions.isSingleLogin() && StringUtil.isNotEmpty(userId)) {
			StringBuilder cookie = new StringBuilder();
			cookie.append(HttpHeaderName.userFlag()).append('=').append(userId).append(attr);
			resp.addHeader("Set-Cookie", cookie.toString());
		}
	}

	protected void outputKey(OutputStream out, byte[] key, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		out.write(S.base64().encode(key));
	}

	protected byte[] createEncryptKey(HttpServletRequest req) {
		return UUIDSeed.seq().substring(4).getBytes();
	}

	protected String createSessionId(HttpServletRequest req) {
		return UUIDSeed.random();
	}

	@Override
	public void init(ServletConfig config) {
		session = WebSessions.loadUserSession();
	}

	protected UserSession userSession() {
		return session;
	}

	/**
	 * @param sessionId
	 *            http头部sid的信息
	 * @param user
	 *            对应于http parameter的username
	 * @param req
	 *            用户请求的HttpServletRequest对象
	 * @return 登陆信息，无论成功与否，返回值不能是null
	 */
	protected abstract LoginObject login(String sessionId, String user, HttpServletRequest req);
}
