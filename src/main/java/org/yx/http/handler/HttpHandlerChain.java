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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.yx.annotation.http.Web;
import org.yx.exception.BizException;
import org.yx.exception.HttpException;
import org.yx.exception.InvalidParamException;
import org.yx.http.HttpErrorCode;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.log.HttpLogHolder;
import org.yx.log.Log;

public class HttpHandlerChain implements HttpHandler {

	private Logger LOG = Log.get("sumk.http.chain");
	private Logger LOG_ERROR = Log.get("sumk.http.req.error");
	private List<HttpHandler> handlers = new ArrayList<>();

	public static HttpHandlerChain inst = new HttpHandlerChain();
	public static HttpHandlerChain upload = new HttpHandlerChain();

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
		boolean success = true;
		try {
			for (int i = 0; i < this.handlers.size(); i++) {
				HttpHandler h = this.handlers.get(i);
				if (h.accept(ctx.httpNode().action)) {
					if (LOG.isTraceEnabled()) {
						if (String.class.isInstance(ctx.data())) {
							String s = ((String) ctx.data());
							LOG.trace("{} - {} with data:{}", ctx.act(), h.getClass().getSimpleName(), s);
						} else {
							LOG.trace("{} - {}", ctx.act(), h.getClass().getSimpleName());
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
				LOG_ERROR.error(msg(ctx, temp.getMessage()), temp);
				error(ctx, HttpErrorCode.DATA_FORMAT_ERROR, "数据格式错误");
				return true;
			}
			if (InvalidParamException.class.isInstance(temp)) {
				LOG_ERROR.info(msg(ctx, temp.getMessage()), temp);
				error(ctx, HttpErrorCode.VALIDATE_ERROR, temp.getMessage());
				return true;
			}
			do {
				if (BizException.class.isInstance(temp)) {
					BizException be = (BizException) temp;
					LOG_ERROR.info(msg(ctx, temp.toString()));
					error(ctx, be.getCode(), be.getMessage());
					return true;
				}
			} while ((temp = temp.getCause()) != null);
			LOG_ERROR.error(msg(ctx, e.getMessage()), e);
			error(ctx, HttpErrorCode.HANDLE_ERROR, "请求出错");
		} finally {
			HttpLogHolder.log(ctx);
			UploadFileHolder.remove();
			InnerHttpUtil.act(ctx.act(), System.currentTimeMillis() - ctx.beginTime(), success);
		}
		return true;
	}

	private String msg(WebContext ctx, String message) {
		StringBuilder sb = new StringBuilder();
		return sb.append("[").append(ctx.act()).append("] ").append(message).toString();
	}

	private void error(WebContext ctx, int code, String message) throws UnsupportedEncodingException, IOException {
		HttpLogHolder.errorLog(code, message, ctx);
		InnerHttpUtil.error(ctx.httpResponse(), code, message, ctx.charset());
	}

}
