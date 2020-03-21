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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.yx.annotation.Bean;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Logs;

@Bean
public class ReqBodyHandler implements HttpHandler {

	@Override
	public int order() {
		return 1300;
	}

	@Override
	public boolean supportRestType(RestType type) {
		return type == RestType.PLAIN;
	}

	@Override
	public void handle(WebContext ctx) throws Exception {
		if (ctx.data() != null) {
			Logs.http().debug("data is not null");
			return;
		}
		if (ctx.httpNode().argClz == null) {
			return;
		}
		HttpServletRequest req = ctx.httpRequest();
		InputStream in = req.getInputStream();
		ctx.data(InnerHttpUtil.extractData(in));
	}

}
