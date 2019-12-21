package org.yx.http.log;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.yx.conf.AppInfo;
import org.yx.http.handler.WebContext;
import org.yx.log.Log;

import com.google.gson.stream.JsonWriter;

public class DefaultHttpLogHandler implements HttpLogHandler {
	private Logger LOG_REQ = Log.get("sumk.http.log.json");
	private int maxLen = AppInfo.getInt("sumk.http.log.resp.maxlength", 100000);

	public void log(WebContext ctx) {
		if (LOG_REQ.isDebugEnabled() && ctx.dataInString() != null) {
			try {
				StringWriter stringWriter = new StringWriter();
				JsonWriter writer = new JsonWriter(stringWriter);
				writer.beginObject();
				writer.name("req").value(ctx.dataInString());
				String resp = ctx.respInString();
				if (resp != null && maxLen > 0) {
					if (resp.length() > maxLen) {
						resp = resp.substring(0, maxLen);
					}
					writer.name("resp").value(resp);
				}
				writer.endObject();
				writer.flush();
				writer.close();
				LOG_REQ.debug(stringWriter.toString());
			} catch (Exception e) {
				Log.get("sumk.http").error(e.toString(), e);
			}
		}
	}

	public void errorLog(int code, String errorMsg, WebContext ctx) {
		if (LOG_REQ.isInfoEnabled()) {
			try {
				StringWriter stringWriter = new StringWriter();
				JsonWriter writer = new JsonWriter(stringWriter);
				writer.beginObject();
				writer.name("act").value(ctx.act());
				String data = ctx.dataInString();
				if (data != null) {
					writer.name("req").value(data);
				}
				writer.name("err_code").value(code);
				writer.name("err_msg").value(errorMsg);
				writer.endObject();
				writer.flush();
				writer.close();
				LOG_REQ.info(stringWriter.toString());
			} catch (Exception e) {
				Log.get("sumk.http").error(e.toString(), e);
			}
		}
	}

	public void errorLog(int code, String errorMsg, HttpServletRequest req) {
		if (LOG_REQ.isInfoEnabled()) {
			try {
				StringWriter stringWriter = new StringWriter();
				JsonWriter writer = new JsonWriter(stringWriter);
				writer.beginObject();
				writer.name("err_code").value(code);
				writer.name("err_msg").value(errorMsg);
				writer.endObject();
				writer.flush();
				writer.close();
				LOG_REQ.info(stringWriter.toString());
			} catch (Exception e) {
				Log.get("sumk.http").error(e.toString(), e);
			}
		}
	}

}
