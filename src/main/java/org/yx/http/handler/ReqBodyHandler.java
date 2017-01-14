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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.yx.exception.HttpException;
import org.yx.http.HttpUtil;
import org.yx.http.Web;
import org.yx.log.Log;

public class ReqBodyHandler implements HttpHandler {

	private final int MAXLENGTH = 1024 * 1024 * 10;

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		if (ctx.getData() != null) {
			Log.get(ReqBodyHandler.class).debug("data is not null");
			return false;
		}
		if (ctx.getInfo().getArgClz() == null) {
			return false;
		}
		HttpServletRequest req = ctx.getHttpRequest();
		InputStream in = req.getInputStream();
		int count = 0;
		int n = 0;
		byte[] temp = new byte[1024 * 4];
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while (-1 != (n = in.read(temp))) {
			output.write(temp, 0, n);
			count += n;
			if (count > MAXLENGTH) {
				HttpException.throwException(ReqBodyHandler.class, "request body is too long");
			}
		}
		byte[] bs = output.toByteArray();
		ctx.setData(HttpUtil.extractData(bs));
		output.close();
		return false;
	}

}
