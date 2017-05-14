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
package org.yx.http.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.conf.AppInfo;
import org.yx.http.ErrorCode;
import org.yx.http.HttpSessionHolder;
import org.yx.http.HttpUtil;
import org.yx.log.Log;
import org.yx.util.StringUtils;
import org.yx.util.UUIDSeed;
import org.yx.util.secury.Base64Util;

public abstract class AbstractSessionFilter implements LoginServlet {

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user = req.getParameter(userName());
		final String sid = createSessionId();

		try {
			LoginObject obj = login(sid, user, req);

			Charset charset = HttpUtil.charset(req);
			if (obj == null) {
				Log.get("loginAction").info(user + ":login Object must not be null");
				HttpUtil.error(resp, ErrorCode.LOGINFAILED, "login failed", charset);
				return;
			}
			if (obj.getErrorMsg() != null) {
				Log.get("loginAction").debug(user + ":" + obj.getErrorMsg());
				HttpUtil.error(resp, ErrorCode.LOGINFAILED, obj.getErrorMsg(), charset);
				return;
			}
			byte[] key = createEncryptKey(req);
			String userToken = obj.getUserId();
			if (StringUtils.isNotEmpty(userToken)) {
				String type = this.getType();
				if (StringUtils.isNotEmpty(type)) {
					type = new String(Base64Util.encode(type.toUpperCase().getBytes()));
					userToken = type + "_" + userToken;
				}
			}
			session.putKey(sid, key, userToken);
			resp.setHeader(UserSession.SESSIONID, sid);
			if (StringUtils.isNotEmpty(userToken)) {
				resp.setHeader(UserSession.TOKEN, userToken);
			}
			outputKey(resp, key);
			if (AppInfo.getBoolean("http.cookie.enable", true)) {
				StringBuilder cookie = new StringBuilder();
				String contextPath = req.getContextPath();

				if (!contextPath.startsWith("/")) {
					contextPath = "/" + contextPath;
				}
				cookie.append(UserSession.SESSIONID).append('=').append(sid);
				if (StringUtils.isNotEmpty(userToken)) {
					cookie.append('&').append(UserSession.TOKEN).append('=').append(userToken);
				}
				cookie.append("; path=").append(contextPath);

				resp.setHeader("Set-Cookie", cookie.toString());
			}

			resp.getOutputStream().write("\t\n".getBytes());
			if (obj.getJson() != null) {
				resp.getOutputStream().write(obj.getJson().getBytes(charset));
			}
		} catch (Exception e) {
			Log.printStack(e);
		}

	}

	protected void outputKey(HttpServletResponse resp, byte[] key) throws IOException {
		resp.getOutputStream().write(Base64Util.encode(key));
	}

	protected byte[] createEncryptKey(HttpServletRequest req) {
		byte[] key = UUIDSeed.seq().substring(4).getBytes();
		return key;
	}

	protected String userName() {
		return "username";
	}

	private UserSession session;

	/**
	 * 存放在sid中的sessionId
	 * 
	 * @return
	 */
	protected String createSessionId() {
		return UUIDSeed.random();
	}

	@Override
	public void init(ServletConfig config) {
		session = HttpSessionHolder.loadUserSession();
	}

	protected UserSession userSession() {
		return session;
	}

	/**
	 * @param sessionId
	 *            http头部sid的信息
	 * @param user
	 *            对应于http parameter的username
	 * @param password
	 *            对应于http parameter的password
	 * @param validCode
	 *            验证码,对应于http parameter的code
	 * @return 登陆信息，无论成功与否，返回值不能是null
	 */
	protected abstract LoginObject login(String sessionId, String user, HttpServletRequest req);

	@Override
	public String getType() {
		return "";
	}

}
