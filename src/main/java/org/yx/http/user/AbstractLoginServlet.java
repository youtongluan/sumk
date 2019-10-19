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
import java.nio.charset.Charset;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.conf.AppInfo;
import org.yx.http.HttpErrorCode;
import org.yx.http.HttpHeader;
import org.yx.http.HttpSettings;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Log;
import org.yx.util.S;
import org.yx.util.StringUtil;
import org.yx.util.UUIDSeed;

public abstract class AbstractLoginServlet implements LoginServlet {

	private static final String LOGIN_NAME = "*login*";
	private UserSession session;
	protected String type = "";

	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final long begin = System.currentTimeMillis();
		boolean success = false;
		try {
			resp.setContentType("application/json;charset=" + InnerHttpUtil.charset(req));
			final String sid = createSessionId(req);
			String user = getUserName(req);
			LoginObject obj = login(sid, user, req);
			Charset charset = InnerHttpUtil.charset(req);
			if (obj == null) {
				Log.get("sumk.http.login").info("{}:login Object must not be null", user);
				InnerHttpUtil.error(resp, HttpErrorCode.LOGINFAILED, "login failed", charset);
				return;
			}
			if (obj.getErrorMsg() != null) {
				Log.get("sumk.http.login").debug("{} : {}", user, obj.getErrorMsg());
				InnerHttpUtil.error(resp, HttpErrorCode.LOGINFAILED, obj.getErrorMsg(), charset);
				return;
			}
			byte[] key = createEncryptKey(req);
			boolean singleLogin = WebSessions.isSingleLogin(this.getType(req));
			if (!session.setSession(sid, obj.getSessionObject(), key, singleLogin)) {
				Log.get("sumk.http.login").debug("{} :sid:{} login failed", user, sid);
				InnerHttpUtil.error(resp, HttpErrorCode.LOGINFAILED, "login failed", charset);
				return;
			}
			String userId = obj.getSessionObject().getUserId();
			resp.setHeader(HttpHeader.SESSIONID, sid);
			if (StringUtil.isNotEmpty(userId)) {
				resp.setHeader(HttpHeader.TOKEN, userId);
			}
			outputKey(resp, key);
			if (HttpSettings.isCookieEnable()) {
				String contextPath = req.getContextPath();

				if (!contextPath.startsWith("/")) {
					contextPath = "/" + contextPath;
				}
				String attr = ";Path=".concat(contextPath);
				setSessionCookie(req, resp, sid, attr);
				setTokenCookie(req, resp, userId, attr);
				setTypeCookie(req, resp, attr);
			}

			resp.getOutputStream().write(new byte[] { '\t', '\n' });
			if (obj.getResponseData() != null) {
				resp.getOutputStream().write(obj.getResponseData().getBytes(charset));
			}
			success = true;
		} catch (Exception e) {
			Log.get("sumk.http.login").error(e.toString(), e);
			InnerHttpUtil.error(resp, HttpErrorCode.LOGINFAILED, "login fail", AppInfo.UTF8);
		} finally {
			InnerHttpUtil.act(LOGIN_NAME, System.currentTimeMillis() - begin, success);
		}

	}

	protected String getUserName(HttpServletRequest req) {
		return req.getParameter(AppInfo.get("sumk.http.username", "username"));
	}

	protected void setSessionCookie(HttpServletRequest req, HttpServletResponse resp, final String sid, String attr) {
		StringBuilder cookie = new StringBuilder();

		cookie.append(HttpHeader.SESSIONID).append('=').append(sid).append(attr);

		resp.addHeader("Set-Cookie", cookie.toString());
	}

	protected void setTokenCookie(HttpServletRequest req, HttpServletResponse resp, String userId, String attr) {
		if (WebSessions.isSingleLogin(this.getType(req)) && StringUtil.isNotEmpty(userId)) {
			StringBuilder cookie = new StringBuilder();
			cookie.append(HttpHeader.TOKEN).append('=').append(userId).append(attr);
			resp.addHeader("Set-Cookie", cookie.toString());
		}
	}

	protected void outputKey(HttpServletResponse resp, byte[] key) throws IOException {
		resp.getOutputStream().write(S.base64.encode(key));
	}

	protected byte[] createEncryptKey(HttpServletRequest req) {
		byte[] key = UUIDSeed.seq().substring(4).getBytes();
		return key;
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

	@Override
	public String getType(HttpServletRequest req) {
		return type;
	}

	protected void setTypeCookie(HttpServletRequest req, HttpServletResponse resp, String attr) {
		StringBuilder cookie = new StringBuilder();
		String type = this.getType(req);
		if (StringUtil.isNotEmpty(type)) {
			cookie.append(HttpHeader.TYPE).append('=').append(type).append(attr);
			resp.addHeader("Set-Cookie", cookie.toString());
		}
	}

	@Override
	public boolean acceptType(String type) {
		if (StringUtil.isEmpty(type)) {
			return StringUtil.isEmpty(this.type);
		}
		return type.equals(this.type);
	}

}
