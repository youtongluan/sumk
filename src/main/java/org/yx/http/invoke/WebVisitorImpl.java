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
package org.yx.http.invoke;

import org.yx.asm.ArgPojo;
import org.yx.common.BizExcutor;
import org.yx.exception.SumkException;
import org.yx.http.HttpGson;
import org.yx.http.act.HttpActionNode;
import org.yx.http.handler.WebContext;
import org.yx.validate.ParamInfo;

public class WebVisitorImpl implements WebVisitor {

	@Override
	public Object visit(WebContext ctx) throws Throwable {
		HttpActionNode http = ctx.httpNode();
		ArgPojo argObj;
		if (http.argNames.length == 0) {
			argObj = http.getEmptyArgObj();
		} else {
			if (ctx.data() == null) {
				throw new SumkException(3253234, "there is no data receive from request");
			}
			argObj = HttpGson.gson().fromJson((String) ctx.data(), http.argClz);
		}

		return exec(argObj, http.owner, http.paramInfos);
	}

	private Object exec(ArgPojo argObj, Object owner, ParamInfo[] paramInfos) throws Throwable {
		Object[] params = argObj.params();
		return BizExcutor.exec(argObj, owner, params, paramInfos);
	}

}
