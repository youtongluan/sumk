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

import javax.servlet.http.HttpServletRequest;

import org.yx.bean.IOC;
import org.yx.common.BizExcutor;
import org.yx.exception.HttpException;
import org.yx.http.HttpGson;
import org.yx.http.Web;
import org.yx.http.filter.HttpBizFilter;
import org.yx.validate.ParamInfo;

public class InvokeHandler implements HttpHandler {

	private static final Object STOP = new Object();

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		HttpNode info = ctx.getHttpNode();
		if (!String.class.isInstance(ctx.getData())) {
			HttpException.throwException(this.getClass(), ctx.getData().getClass().getName() + " is not String");
		}
		Object ret = info.accept(http -> {
			if (http.argClz == null || http.argTypes == null || http.argTypes.length == 0) {
				return exec(http.method, http.obj, null, null, ctx);
			}
			Object[] params = new Object[http.argTypes.length];
			Object argObj = HttpGson.gson.fromJson((String) ctx.getData(), http.argClz);
			for (int i = 0, k = 0; i < params.length; i++) {

				if (argObj == null) {
					params[i] = null;
					continue;
				}
				Field f = http.fields[k++];
				params[i] = f.get(argObj);
			}
			return exec(http.method, http.obj, params, info.paramInfos, ctx);
		});
		if (STOP == ret) {
			return true;
		}
		ctx.setResult(ret);
		return false;
	}

	private static Object exec(Method m, Object obj, Object[] params, ParamInfo[] paramInfos, WebContext ctx)
			throws Throwable {
		List<HttpBizFilter> list = IOC.getBeans(HttpBizFilter.class);
		if (list == null || list.isEmpty()) {
			return BizExcutor.exec(m, obj, params, paramInfos);
		}
		HttpServletRequest req = ctx.getHttpRequest();
		try {
			for (HttpBizFilter f : list) {
				if (!f.beforeInvoke(req, ctx.getHttpResponse(), ctx.getCharset())) {
					return STOP;
				}
			}
			Object ret = BizExcutor.exec(m, obj, params, paramInfos);
			for (HttpBizFilter f : list) {
				if (!f.afterInvoke(req, ctx.getHttpResponse(), ctx.getCharset(), ret)) {
					return STOP;
				}
			}
			return ret;
		} catch (Exception e) {
			for (HttpBizFilter f : list) {
				Exception e2 = f.error(req, e);
				if (e2 != null) {
					e = e2;
				}
			}
			throw e;
		}
	}

}
