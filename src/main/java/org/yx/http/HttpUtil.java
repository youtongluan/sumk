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
package org.yx.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.conf.AppInfo;
import org.yx.exception.HttpException;
import org.yx.http.handler.ReqBodyHandler;
import org.yx.log.Log;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtil;

public final class HttpUtil {
	static final int MAXLENGTH = 1024 * 1024 * 100;

	public static String getType(HttpServletRequest req) {
		String type = HttpHeadersHolder.fromHeaderOrCookieOrParamter(req, HttpHeader.TYPE);
		return type == null ? "" : type;
	}

	public static Charset charset(HttpServletRequest req) {
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

	public static void error(HttpServletResponse resp, int code, String errorMsg, Charset charset)
			throws UnsupportedEncodingException, IOException {
		resp.setStatus(HttpSettings.ERROR_HTTP_STATUS);
		ErrorResp r = new ErrorResp();
		r.setCode(code);
		r.setMessage(errorMsg);
		resp.getOutputStream().write(GsonUtil.toJson(r).getBytes(charset));
	}

	public static void error(HttpServletResponse resp, int httpStatus, int code, String errorMsg, Charset charset)
			throws UnsupportedEncodingException, IOException {
		resp.setStatus(httpStatus);
		ErrorResp r = new ErrorResp();
		r.setCode(code);
		r.setMessage(errorMsg);
		resp.getOutputStream().write(GsonUtil.toJson(r).getBytes(charset));
	}

	public static byte[] extractData(byte[] bs) {

		if (bs != null && bs.length > 4 && bs[0] == 100 && bs[1] == 97 && bs[2] == 116 && bs[3] == 97 && bs[4] == 61) {
			byte[] temp = new byte[bs.length - 5];
			System.arraycopy(bs, 5, temp, 0, temp.length);
			return temp;
		}
		return bs;
	}

	public static byte[] extractData(InputStream in) throws IOException {
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
		output.close();
		return extractData(bs);
	}

}
