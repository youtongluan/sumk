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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.yx.http.HttpUtil;
import org.yx.http.Web;

public class ReqHeaderHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		HttpServletRequest req = ctx.getHttpRequest();
		ctx.setCharset(HttpUtil.charset(req));
		ctx.setSign(req.getParameter("sign"));
		String data = req.getParameter("data");
		if (data != null) {
			ctx.setData(data);
		}
		Enumeration<String> names = req.getHeaderNames();
		Map<String, String> map = new HashMap<>();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			map.put(name, req.getHeader(name));
		}
		ctx.setHeaders(map);
		return false;
	}

}
