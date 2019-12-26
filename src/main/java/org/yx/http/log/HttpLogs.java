package org.yx.http.log;

import java.nio.charset.Charset;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.yx.http.HttpGson;
import org.yx.http.handler.WebContext;
import org.yx.log.LogKits;

public class HttpLogs {
	private static HttpLogHandler handler = new PlainHttpLogHandler();

	public static HttpLogHandler getHandler() {
		return handler;
	}

	public static void setHandler(HttpLogHandler handler) {
		HttpLogs.handler = Objects.requireNonNull(handler);
	}

	public static void log(WebContext ctx, HttpServletRequest req, Throwable ex, long time) {
		handler.log(ctx, req, ex, time);
	}

	public static String getParam(WebContext wc, int maxLength) {
		if (wc == null) {
			return null;
		}
		return parse(wc.dataInString(), wc.data(), maxLength, wc.charset());
	}

	public static String getResponse(WebContext wc, int maxLength) {
		if (wc == null) {
			return null;
		}
		return parse(wc.respInString(), wc.result(), maxLength, wc.charset());
	}

	public static String parse(String data, Object obj, int maxLength, Charset charset) {
		if (data != null) {
			return LogKits.shorterSubfix(data, maxLength);
		}
		if (obj == null) {
			return null;
		}
		if (obj.getClass() == byte[].class) {
			byte[] bs = (byte[]) obj;
			int len = bs.length;
			if (len > maxLength * 3) {
				len = maxLength * 3;
			}
			String temp = new String(bs, 0, len, charset);
			return LogKits.shorterSubfix(temp, maxLength);
		}
		String resp = HttpGson.gson().toJson(obj);
		return LogKits.shorterSubfix(resp, maxLength);
	}

}
