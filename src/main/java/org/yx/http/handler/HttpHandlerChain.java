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
package org.yx.http.handler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.yx.common.ActStatis;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.exception.HttpException;
import org.yx.exception.InvalidParamException;
import org.yx.http.ErrorCode;
import org.yx.http.InnerHttpUtil;
import org.yx.http.Web;
import org.yx.log.Log;

import com.google.gson.stream.JsonWriter;

public class HttpHandlerChain implements HttpHandler {

	private Logger LOG = Log.get("sumk.http.chain");
	private Logger LOG_REQ = Log.get("sumk.http.req");
	private Logger LOG_ERROR = Log.get("sumk.http.req.error");
	private List<HttpHandler> handlers = new ArrayList<>();

	public static HttpHandlerChain inst = new HttpHandlerChain();
	public static HttpHandlerChain upload = new HttpHandlerChain();
	static final ActStatis actStatic = new ActStatis();

	private HttpHandlerChain() {

	}

	public void addHandler(HttpHandler handler) {
		if (this.handlers.contains(handler)) {
			return;
		}
		this.handlers.add(handler);
	}

	public void setHandlers(List<HttpHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		long begin = System.currentTimeMillis();
		boolean success = true;
		try {
			for (int i = 0; i < this.handlers.size(); i++) {
				HttpHandler h = this.handlers.get(i);
				if (h.accept(ctx.httpNode().action)) {
					if (LOG.isTraceEnabled()) {
						if (String.class.isInstance(ctx.data())) {
							String s = ((String) ctx.data());
							LOG.trace("{} with data:{}", h.getClass().getSimpleName(), s);
						} else {
							LOG.trace(h.getClass().getSimpleName());
						}
					}
					if (h.handle(ctx)) {
						success = true;
					}
				}
			}
		} catch (final Throwable e) {
			success = false;
			Throwable temp = e;
			if (InvocationTargetException.class.isInstance(temp)) {
				temp = ((InvocationTargetException) temp).getTargetException();
			}

			if (HttpException.class.isInstance(temp)) {
				LOG_ERROR.error(temp.getMessage(), temp);
				error(ctx, -1013243, "data format error");
				return true;
			}
			if (InvalidParamException.class.isInstance(temp)) {
				LOG_ERROR.info(temp.getMessage(), temp);
				error(ctx, ErrorCode.VALIDATE_ERROR, temp.getMessage());
				return true;
			}
			do {
				if (BizException.class.isInstance(temp)) {
					BizException be = (BizException) temp;
					LOG_ERROR.info(temp.toString());
					error(ctx, be.getCode(), be.getMessage());
					return true;
				}
			} while ((temp = temp.getCause()) != null);
			LOG_ERROR.error(e.getMessage(), e);
			error(ctx, -2343254, "请求出错");
		} finally {
			if (LOG_REQ.isDebugEnabled() && ctx.dataInString() != null) {
				StringWriter stringWriter = new StringWriter();
				JsonWriter writer = new JsonWriter(stringWriter);
				writer.beginObject();
				writer.name("req").value(ctx.dataInString());
				String resp = ctx.respInString();
				int maxLen = AppInfo.getInt("http.log.resp.length", 10000000);
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
			}
			UploadFileHolder.remove();
			actStatic.visit(ctx.act(), System.currentTimeMillis() - begin, success);
		}
		return true;
	}

	private void error(WebContext ctx, int code, String message) throws UnsupportedEncodingException, IOException {
		if (LOG_REQ.isInfoEnabled()) {
			StringWriter stringWriter = new StringWriter();
			JsonWriter writer = new JsonWriter(stringWriter);
			writer.beginObject();
			String data = ctx.dataInString();
			if (data != null) {
				writer.name("req").value(data);
			}
			writer.name("err_code").value(code);
			writer.name("err_msg").value(message);
			writer.endObject();
			writer.flush();
			writer.close();
			LOG_REQ.info(stringWriter.toString());
		}
		InnerHttpUtil.error(ctx.httpResponse(), code, message, ctx.charset());
	}

}
