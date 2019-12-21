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

import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.http.kit.InnerHttpUtil;

/**
 * 这个类要在sumk-http中调用，不能在自定义的servlet中调用
 */
public final class HttpContextHolder {

	private static ThreadLocal<HttpServletRequest> _req = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> _resp = new ThreadLocal<>();

	public static void set(HttpServletRequest req, HttpServletResponse resp) {
		_req.set(Objects.requireNonNull(req));
		if (resp != null) {
			_resp.set(resp);
		}
	}

	public static void clear() {
		_req.remove();
		_resp.remove();
	}

	public static HttpServletRequest getHttpRequest() {
		return _req.get();
	}

	public static HttpServletResponse getHttpResponse() {
		return _resp.get();
	}

	public static String getHeader(String name) {
		return _req.get().getHeader(name);
	}

	public static String sessionId() {
		return fromHeaderOrCookieOrParamter(_req.get(), HttpHeaderName.sessionId());
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
		String type = fromHeaderOrCookie(req, name);
		if (type != null) {
			return type;
		}
		return req.getParameter(name);
	}

	public static String getType() {
		return InnerHttpUtil.getType(_req.get());
	}

	static void remove() {
		_req.remove();
	}

	public static String getToken() {
		return fromHeaderOrCookieOrParamter(_req.get(), HttpHeaderName.token());
	}
}
