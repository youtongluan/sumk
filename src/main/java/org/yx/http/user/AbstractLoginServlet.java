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

import org.yx.common.context.ActionContext;
import org.yx.common.sumk.UnsafeByteArrayOutputStream;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.HttpHeaderName;
import org.yx.http.kit.HttpSettings;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.log.HttpLogs;
import org.yx.log.Logs;
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
		String user = null;
		Charset charset = InnerHttpUtil.charset(req);
		try {
			if (!acceptMethod(req, resp)) {
				Logs.http().warn("不是login的有效method，比如HEAD等方法可能不支持");
				return;
			}
			InnerHttpUtil.setRespHeader(resp, charset);
			final String sid = createSessionId(req);
			user = getUserName(req);
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
			boolean singleLogin = HttpSettings.isSingleLogin();
			if (!session.setSession(sid, obj.getSessionObject(), key, singleLogin)) {
				Logs.http().debug("{} :sid:{} login failed", user, sid);
				InnerHttpUtil.sendError(resp, HttpErrorCode.LOGINFAILED, user + " : login failed", charset);
				return;
			}
			String userId = obj.getSessionObject().getUserId();
			resp.setHeader(HttpHeaderName.sessionId(), sid);
			if (StringUtil.isNotEmpty(userId)) {
				resp.setHeader(HttpHeaderName.userFlag(), userId);
			}

			if (HttpSettings.isCookieEnable()) {
				String contextPath = req.getContextPath();

				if (!contextPath.startsWith("/")) {
					contextPath = "/" + contextPath;
				}
				String attr = ";Path=".concat(contextPath);
				setSessionCookie(req, resp, sid, attr);
				setUserFlagCookie(req, resp, userId, attr);
			}

			UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream(64);
			this.outputKey(out, key, req, resp);
			if (obj.getResponseData() != null) {
				out.write(obj.getResponseData().getBytes(charset));
			}
			resp.getOutputStream().write(out.toByteArray());
		} catch (Throwable e) {
			ex = e;
			Logs.http().error("user:" + user + ",message:" + e.getLocalizedMessage(), e);
			if (BizException.class.isInstance(e)) {
				BizException be = (BizException) e;
				InnerHttpUtil.sendError(resp, be.getCode(), be.getMessage(), charset);
			} else {
				InnerHttpUtil.sendError(resp, HttpErrorCode.LOGINFAILED, "login fail:" + user, charset);
			}
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
		return req.getParameter(AppInfo.get("sumk.http.login.username", "username"));
	}

	protected void setSessionCookie(HttpServletRequest req, HttpServletResponse resp, final String sid, String attr) {
		StringBuilder cookie = new StringBuilder(HttpHeaderName.sessionId()).append('=').append(sid).append(attr);

		resp.addHeader("Set-Cookie", cookie.toString());
	}

	protected void setUserFlagCookie(HttpServletRequest req, HttpServletResponse resp, String userId, String attr) {
		if (HttpSettings.isSingleLogin() && StringUtil.isNotEmpty(userId)) {
			StringBuilder cookie = new StringBuilder();
			cookie.append(HttpHeaderName.userFlag()).append('=').append(userId).append(attr);
			resp.addHeader("Set-Cookie", cookie.toString());
		}
	}

	protected void outputKey(OutputStream out, byte[] key, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (AppInfo.getBoolean("sumk.http.key.output.body", false)) {
			out.write(S.base64().encode(key));
			out.write(new byte[] { '\t', '\n' });
		}

		if (AppInfo.getBoolean("sumk.http.key.output.header", true)) {
			resp.setHeader(AppInfo.get("sumk.http.header.skey", "skey"), S.base64().encodeToString(key));
		}
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
	 * @param userName
	 *            用户名，默认通过request.getParameter("username")获取的
	 * @param req
	 *            用户请求的HttpServletRequest对象
	 * @return 登录信息，无论成功与否，返回值不能是null
	 */
	protected abstract LoginObject login(String sessionId, String userName, HttpServletRequest req);
}
