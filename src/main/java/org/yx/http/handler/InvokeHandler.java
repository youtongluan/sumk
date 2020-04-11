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

import org.yx.annotation.Bean;
import org.yx.annotation.ErrorHandler.ExceptionStrategy;
import org.yx.exception.BizException;
import org.yx.http.act.HttpActionNode;
import org.yx.http.invoke.WebHandler;
import org.yx.http.kit.HttpException;
import org.yx.log.Log;

@Bean
public class InvokeHandler implements HttpHandler {

	@Override
	public int order() {
		return 2000;
	}

	@Override
	public void handle(WebContext ctx) throws Throwable {
		HttpActionNode info = ctx.httpNode();
		Object ret = null;
		if (info.errorHandler != null) {
			try {
				ret = WebHandler.handle(ctx);
			} catch (Exception e) {
				if (BizException.class.isInstance(e)
						&& ExceptionStrategy.IF_NO_BIZEXCEPTION == info.errorHandler.strategy()) {
					throw e;
				}
				Log.get("sumk.http.error").error("业务处理含有原始异常", e);

				throw HttpException.create(info.errorHandler.code(), info.errorHandler.message());
			}
		} else {
			ret = WebHandler.handle(ctx);
		}
		ctx.result(ret);
	}

}
