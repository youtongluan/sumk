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

import org.yx.annotation.http.Web;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.kit.InnerHttpUtil;

public class ReqTypeChecker implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		String type = InnerHttpUtil.getType(ctx.httpRequest());
		if (!ctx.httpNode().acceptType(type)) {
			BizException.throwException(HttpErrorCode.TYPE_ERROR, "客户端类别错误");
		}
		return false;
	}

}
