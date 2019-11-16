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

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.yx.annotation.ErrorHandler.ExceptionStrategy;
import org.yx.annotation.http.Web;
import org.yx.asm.ArgPojo;
import org.yx.common.BizExcutor;
import org.yx.exception.BizException;
import org.yx.exception.HttpException;
import org.yx.http.HttpGson;
import org.yx.http.WebFilter;
import org.yx.log.Log;
import org.yx.validate.ParamInfo;

public class InvokeHandler implements HttpHandler {

	private static final Object STOP = new Object();
	private static WebFilter[] filters;

	public static void setFilters(WebFilter[] filters) {
		InvokeHandler.filters = Objects.requireNonNull(filters);
		if (filters.length > 0) {
			Logger log = Log.get("sumk.http");
			if (log.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder("web filter:");
				for (WebFilter f : filters) {
					sb.append("  ").append(f.getClass().getSimpleName());
				}
				log.debug(sb.toString());
			}
		}
	}

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
				Log.get("sumk.http").debug(e.getMessage(), e);
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
			ArgPojo argObj;
			if (http.argNames.length == 0) {
				argObj = http.getEmptyArgObj();
			} else {
				if (ctx.data() == null) {
					HttpException.throwException(InvokeHandler.class, "there is no data");
				}
				argObj = HttpGson.gson().fromJson((String) ctx.data(), http.argClz);
			}

			return exec(argObj, http.owner, info.paramInfos, ctx);
		});
	}

	private static Object exec(ArgPojo argObj, Object owner, ParamInfo[] paramInfos, WebContext ctx) throws Throwable {
		Object[] params = argObj.params();
		if (filters.length == 0) {
			return BizExcutor.exec(argObj, owner, params, paramInfos);
		}
		HttpServletRequest req = ctx.httpRequest();
		try {
			for (WebFilter f : filters) {
				if (!f.beforeInvoke(req, ctx.httpResponse(), params)) {
					return STOP;
				}
			}
			Object ret = BizExcutor.exec(argObj, owner, params, paramInfos);
			for (WebFilter f : filters) {
				if (!f.afterInvoke(req, ctx.httpResponse(), params, ret)) {
					return STOP;
				}
			}
			return ret;
		} catch (Throwable e) {
			for (WebFilter f : filters) {
				Throwable e2 = f.error(req, params, e);
				if (e2 != null) {
					e = e2;
				}
			}
			throw e;
		}
	}

}
