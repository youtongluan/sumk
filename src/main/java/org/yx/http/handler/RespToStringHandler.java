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

import org.yx.http.HttpGson;
import org.yx.http.Web;

public class RespToStringHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		Object obj = ctx.result();
		if (obj == null) {
			ctx.result(HttpGson.gson().toJson(obj));
			return false;
		}
		Class<?> clz = obj.getClass();
		if (clz.isArray()) {
			ctx.result(HttpGson.gson().toJson(obj));
			return false;
		}
		if (clz.isPrimitive() || clz.equals(String.class)) {
			ctx.result(String.valueOf(obj));
			return false;
		}
		ctx.result(HttpGson.gson().toJson(obj));
		return false;
	}

}
