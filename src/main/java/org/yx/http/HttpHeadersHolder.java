package org.yx.http;

import javax.servlet.http.HttpServletRequest;

import org.yx.http.filter.Session;

public class HttpHeadersHolder {
	private static ThreadLocal<HttpServletRequest> _req = new ThreadLocal<>();

	public static void setHttpRequest(HttpServletRequest req) {
		_req.set(req);
	}

	public static String getHeader(String name) {
		return _req.get().getHeader(name);
	}

	public static HttpServletRequest getHttpRequest() {
		return _req.get();
	}

	public static String token() {
		return _req.get().getHeader(Session.SESSIONID);
	}

	public static void remove() {
		_req.remove();
	}

}
