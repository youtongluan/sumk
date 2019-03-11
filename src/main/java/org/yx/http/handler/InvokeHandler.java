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

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.yx.annotation.ErrorHandler.ExceptionStrategy;
import org.yx.annotation.http.Web;
import org.yx.asm.ArgPojo;
import org.yx.bean.IOC;
import org.yx.common.BizExcutor;
import org.yx.exception.BizException;
import org.yx.exception.HttpException;
import org.yx.http.HttpGson;
import org.yx.http.filter.WebFilter;
import org.yx.log.Log;
import org.yx.validate.ParamInfo;

public class InvokeHandler implements HttpHandler {

	private static final Object STOP = new Object();

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		HttpActionNode info = ctx.httpNode();
		if (ctx.data() != null && !String.class.isInstance(ctx.data())) {
			HttpException.throwException(this.getClass(), ctx.data().getClass().getName() + " is not String");
		}
		Object ret = null;
		if (info.errorHandler != null) {
			try {
				ret = exec(info, ctx);
			} catch (Exception e) {
				if (BizException.class.isInstance(e)
						&& ExceptionStrategy.IF_NO_BIZEXCEPTION == info.errorHandler.strategy()) {
					throw e;
				}
				Log.get("sumk.http.invoke").debug(e.getMessage(), e);
				BizException.throwException(info.errorHandler.code(), info.errorHandler.message());
			}
		} else {
			ret = exec(info, ctx);
		}
		if (STOP == ret) {
			return true;
		}
		ctx.result(ret);
		return false;
	}

	private static Object exec(HttpActionNode info, WebContext ctx) throws Throwable {
		return info.accept(http -> {
			if (http.argClz == null || http.argTypes == null || http.argTypes.length == 0) {
				return exec(http.method, http.obj, null, null, ctx);
			}
			if (ctx.data() == null) {
				if (http.argNames != null && http.argNames.length > 0) {
					HttpException.throwException(InvokeHandler.class, "there is no data");
					return exec(http.method, http.obj, new Object[0], info.paramInfos, ctx);
				}
			}
			ArgPojo argObj = HttpGson.gson().fromJson((String) ctx.data(), http.argClz);

			return exec(http.method, http.obj, argObj == null ? null : argObj.params(), info.paramInfos, ctx);
		});
	}

	private static Object exec(Method m, Object obj, Object[] params, ParamInfo[] paramInfos, WebContext ctx)
			throws Throwable {
		List<WebFilter> list = IOC.getBeans(WebFilter.class);
		if (list == null || list.isEmpty()) {
			return BizExcutor.exec(m, obj, params, paramInfos);
		}
		HttpServletRequest req = ctx.httpRequest();
		try {
			for (WebFilter f : list) {
				if (!f.beforeInvoke(req, ctx.httpResponse(), params)) {
					return STOP;
				}
			}
			Object ret = BizExcutor.exec(m, obj, params, paramInfos);
			for (WebFilter f : list) {
				if (!f.afterInvoke(req, ctx.httpResponse(), params, ret)) {
					return STOP;
				}
			}
			return ret;
		} catch (Exception e) {
			for (WebFilter f : list) {
				Exception e2 = f.error(req, params, e);
				if (e2 != null) {
					e = e2;
				}
			}
			throw e;
		}
	}

}
