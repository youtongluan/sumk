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
package org.yx.http.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.yx.common.ActStatis;
import org.yx.exception.BizException;
import org.yx.exception.HttpException;
import org.yx.exception.InvalidParamException;
import org.yx.http.ErrorCode;
import org.yx.http.HttpUtil;
import org.yx.http.Web;
import org.yx.log.Log;

public class HttpHandlerChain implements HttpHandler {

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
				if (h.accept(ctx.getHttpNode().action)) {
					if (Log.isTraceEnable("handle")) {
						if (String.class.isInstance(ctx.getData())) {
							String s = ((String) ctx.getData());
							Log.get(this.getClass(), "handle").trace("{} with data:{}", h.getClass().getSimpleName(),
									s);
						} else {
							Log.get(this.getClass(), "handle").trace(h.getClass().getSimpleName());
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
				Log.printStack(temp);
				HttpUtil.error(ctx.getHttpResponse(), -1013243, "data format error", ctx.getCharset());
				return true;
			}
			if (InvalidParamException.class.isInstance(temp)) {
				InvalidParamException e3 = InvalidParamException.class.cast(temp);
				Log.get("sumk.http").info("InvalidParamException,message:{},paramName:{},arg:{}", e3.getMessage(),
						e3.getInfo().getParamName(), e3.getParam());
				HttpUtil.error(ctx.getHttpResponse(), ErrorCode.VALIDATE_ERROR, e3.getMessage(), ctx.getCharset());
				return true;
			}
			while (temp != null) {
				if (BizException.class.isInstance(temp)) {
					BizException be = (BizException) temp;
					Log.get("sumk.http").info("bussiness exception,code:{},message:{}", be.getCode(), be.getMessage());
					HttpUtil.error(ctx.getHttpResponse(), be.getCode(), be.getMessage(), ctx.getCharset());
					return true;
				}
				temp = temp.getCause();
			}
			Log.printStack(e);
			HttpUtil.error(ctx.getHttpResponse(), -2343254, "请求出错", ctx.getCharset());
		} finally {
			UploadFileHolder.remove();
			actStatic.visit(ctx.getAct(), System.currentTimeMillis() - begin, success);
		}
		return true;
	}

}
