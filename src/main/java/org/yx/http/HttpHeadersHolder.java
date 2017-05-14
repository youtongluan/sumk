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

import javax.servlet.http.HttpServletRequest;

import org.yx.http.filter.UserSession;

public class HttpHeadersHolder {

	/**
	 * http请求的类型，要类型一致才能访问
	 */
	public static final String TYPE = "stype";

	private static ThreadLocal<HttpServletRequest> _req = new ThreadLocal<>();

	static void setHttpRequest(HttpServletRequest req) {
		_req.set(req);
	}

	public static String getHeader(String name) {
		return _req.get().getHeader(name);
	}

	public static HttpServletRequest getHttpRequest() {
		return _req.get();
	}

	public static String token() {
		return _req.get().getHeader(UserSession.SESSIONID);
	}

	public static String getType() {
		return HttpUtil.getType(_req.get());
	}

	static void remove() {
		_req.remove();
	}

}
