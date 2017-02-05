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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.yx.bean.IOC;
import org.yx.common.BizExcutor;
import org.yx.exception.HttpException;
import org.yx.http.HttpGson;
import org.yx.http.Web;
import org.yx.http.filter.HttpBizFilter;
import org.yx.http.filter.HttpRequest;
import org.yx.rpc.server.intf.ActionContext;

public class InvokeHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		HttpInfo info = ctx.getInfo();
		if (!String.class.isInstance(ctx.getData())) {
			HttpException.throwException(this.getClass(), ctx.getData().getClass().getName() + " is not String");
		}
		Object ret = info.accept(http -> {
			if (http.getArgClz() == null || http.argTypes == null || http.argTypes.length == 0) {
				return exec(http.m, http.obj, null);
			}
			Object[] params = new Object[http.getArgTypes().length];
			Object argObj = HttpGson.gson.fromJson((String) ctx.getData(), http.argClz);
			for (int i = 0, k = 0; i < params.length; i++) {
				if (ActionContext.class.isInstance(http.getArgTypes()[i])) {

					params[i] = null;
					continue;
				}
				if (argObj == null) {
					params[i] = null;
					continue;
				}
				Field f = http.getFields()[k++];
				params[i] = f.get(argObj);
			}
			return exec(http.m, http.obj, params);
		});
		ctx.setResult(ret);
		return false;
	}

	private static Object exec(Method m, Object obj, Object[] params) throws Throwable {
		List<HttpBizFilter> list = IOC.getBeans(HttpBizFilter.class);
		if (list == null || list.isEmpty()) {
			return BizExcutor.exec(m, obj, params);
		}
		HttpRequest req = new HttpRequest(params);
		try {
			for (HttpBizFilter f : list) {
				f.beforeInvoke(req);
			}
			Object ret = BizExcutor.exec(m, obj, params);
			for (HttpBizFilter f : list) {
				f.afterInvoke(req, ret);
			}
			return ret;
		} catch (Exception e) {
			for (HttpBizFilter f : list) {
				f.error(req, e);
			}
			throw e;
		}
	}

}
