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
package org.yx.util;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.yx.http.HttpHeaderName;
import org.yx.http.act.HttpActions;
import org.yx.http.handler.MultipartHolder;
import org.yx.http.handler.MultipartItem;
import org.yx.http.handler.WebContext;
import org.yx.http.kit.HttpKit;
import org.yx.http.kit.HttpSettings;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.kit.LocalWebContext;
import org.yx.http.user.SessionObject;
import org.yx.http.user.WebSessions;

/**
 * <B>注意：本工具类不能在自定义的servlet中调用，比如login</B>
 */
public final class WebUtil {

	public static <T extends SessionObject> T getUserObject(Class<T> clz) {
		return WebSessions.getUserObject(getSessionId(), clz);
	}

	/**
	 * 移除session内容，也就是logout
	 */
	public static void removeUserObject() {
		WebSessions.remove(getSessionId());
	}

	public static String getHeader(HttpServletRequest req, String name) {
		return req.getHeader(name);
	}

	public static WebContext context() {
		return LocalWebContext.getCtx();
	}

	/**
	 * 获取http请求中的HttpServletRequest对象
	 * 
	 * @return HttpServletRequest对象
	 */
	public static HttpServletRequest getHttpRequest() {
		WebContext ctx = context();
		if (ctx != null) {
			return ctx.httpRequest();
		}
		return null;
	}

	/**
	 * 从request对象获取sessionId
	 * 
	 * @return sessionId
	 */
	public static String getSessionId() {
		return fromHeaderOrCookieOrParamter(getHttpRequest(), HttpHeaderName.sessionId());
	}

	private static String fromHeaderOrCookie(HttpServletRequest req, String name) {
		if (req == null) {
			return null;
		}
		String value = req.getHeader(name);
		if (value != null && value.length() > 0) {
			return value;
		}
		if (!HttpSettings.isCookieEnable()) {
			return null;
		}
		Cookie[] cookies = req.getCookies();
		if (cookies == null || cookies.length == 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public static String fromHeaderOrCookieOrParamter(HttpServletRequest req, String name) {
		if (req == null) {
			return null;
		}
		String v = fromHeaderOrCookie(req, name);
		if (v != null) {
			return v;
		}
		v = req.getParameter(name);
		if (v != null) {
			return v;
		}
		Object attr = req.getAttribute(name);
		if (attr != null && attr.getClass() == String.class) {
			return (String) attr;
		}
		return null;
	}

	public static String fromHeaderOrParamter(HttpServletRequest req, String name) {
		if (req == null) {
			return null;
		}
		String v = req.getHeader(name);
		if (v != null && v.length() > 0) {
			return v;
		}
		v = req.getParameter(name);
		if (v != null) {
			return v;
		}
		Object attr = req.getAttribute(name);
		if (attr != null && attr.getClass() == String.class) {
			return (String) attr;
		}
		return null;
	}

	public static String getUserFlag(HttpServletRequest req) {
		return fromHeaderOrCookieOrParamter(req, HttpHeaderName.userFlag());
	}

	public static Charset getCharset(HttpServletRequest req) {
		return InnerHttpUtil.charset(req);
	}

	public static String getUserId() {
		SessionObject obj = getUserObject(SessionObject.class);
		if (obj == null) {
			return null;
		}
		return obj.getUserId();
	}

	/**
	 * 用这个方法获取上传项，<B>仅用于@upload修饰的接口</B>
	 * 
	 * @return 除了参数外所有的multipart
	 */
	public static List<MultipartItem> getMultiParts() {
		return MultipartHolder.get();
	}

	/**
	 * 根据name获取对应的MultipartItem，<B>仅用于@upload修饰的接口</B>
	 * 
	 * @param name MultipartItem对应的名称，注意该名称是part的名称，而不是文件名
	 * @return 对应的MultipartItem，如果不存在就返回null
	 */
	public static MultipartItem getPart(String name) {
		List<MultipartItem> list = MultipartHolder.get();
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (MultipartItem p : list) {
			if (name.equals(p.getName())) {
				return p;
			}
		}
		return null;
	}

	public static HttpKit getKit() {
		return InnerHttpUtil.getKit();
	}

	public static void setKit(HttpKit kit) {
		InnerHttpUtil.setKit(kit);
	}

	public static byte[] getSessionEncryptKey() {
		String sessionId = WebUtil.getSessionId();
		if (sessionId == null) {
			return null;
		}
		return WebSessions.loadUserSession().getEncryptKey(sessionId);
	}

	/**
	 * @return 前缀匹配的方式里，获取剩余的url
	 */
	public static String getUrlLeft() {
		WebContext ctx = context();
		String action = ctx.actionInfo().formalName();
		if (!action.endsWith(HttpActions.PREFIX_MATCH_ENDING)) {
			return null;
		}
		String p = ctx.httpRequest().getPathInfo();
		if (p == null) {
			return null;
		}
		p = HttpActions.formatActionName(p);
		if (p.length() < action.length()) {
			return "";
		}
		return p.substring(action.length() - 1);
	}
}
