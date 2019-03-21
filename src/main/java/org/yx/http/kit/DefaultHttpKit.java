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
package org.yx.http.kit;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.ActStatis;
import org.yx.conf.AppInfo;
import org.yx.http.ErrorResp;
import org.yx.http.HttpGson;
import org.yx.http.HttpHeader;
import org.yx.http.HttpHeadersHolder;
import org.yx.http.HttpSettings;
import org.yx.log.Log;
import org.yx.util.StringUtil;

public class DefaultHttpKit implements HttpKit {

	private final ActStatis actStatic = new ActStatis();

	public String getType(HttpServletRequest req) {
		String type = HttpHeadersHolder.fromHeaderOrCookieOrParamter(req, HttpHeader.TYPE);
		return type == null ? "" : type;
	}

	public Charset charset(HttpServletRequest req) {
		String charsetName = AppInfo.get("http.charset");
		if (StringUtil.isEmpty(charsetName)) {
			charsetName = req.getCharacterEncoding();
		}
		if (StringUtil.isEmpty(charsetName) || charsetName.equalsIgnoreCase(HttpSettings.DEFAULT_CHARSET.name())) {
			return HttpSettings.DEFAULT_CHARSET;
		}

		if (!Charset.isSupported(charsetName)) {
			Log.get("sumk.http").error("charset '{}' is not supported", charsetName);
			return HttpSettings.DEFAULT_CHARSET;
		}
		return Charset.forName(charsetName);
	}

	public void error(HttpServletResponse resp, int code, String errorMsg, Charset charset) throws IOException {
		resp.setStatus(HttpSettings.ERROR_HTTP_STATUS);
		ErrorResp r = new ErrorResp();
		r.setCode(code);
		r.setMessage(errorMsg);
		resp.getOutputStream().write(HttpGson.gson().toJson(r).getBytes(charset));
	}

	public void error(HttpServletResponse resp, int httpStatus, int code, String errorMsg, Charset charset)
			throws IOException {
		resp.setStatus(httpStatus);
		ErrorResp r = new ErrorResp();
		r.setCode(code);
		r.setMessage(errorMsg);
		resp.getOutputStream().write(HttpGson.gson().toJson(r).getBytes(charset));
	}

	public void noCache(HttpServletResponse resp) {
		resp.setHeader("cache-control", "no-store");
		resp.setDateHeader("Expires", 0);
	}

	public void act(String act, long time, boolean isSuccess) {
		actStatic.visit(act, time, isSuccess);
	}

	@Override
	public ActStatis actStatis() {
		return this.actStatic;
	}

}
