package org.yx.http.log;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.yx.http.handler.WebContext;

public class HttpLogHolder {
	private static HttpLogHandler handler = new DefaultHttpLogHandler();

	public static HttpLogHandler getHandler() {
		return handler;
	}

	public static void setHandler(HttpLogHandler handler) {
		HttpLogHolder.handler = Objects.requireNonNull(handler);
	}

	public static void log(WebContext ctx) {
		if (handler != null) {
			handler.log(ctx);
		}
	}

	public static void errorLog(int code, String errorMsg, WebContext ctx) {
		if (handler != null) {
			handler.errorLog(code, errorMsg, ctx);
		}
	}

	public static void errorLog(int code, String errorMsg, HttpServletRequest req) {
		if (handler != null) {
			handler.errorLog(code, errorMsg, req);
		}
	}

}
