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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.ActStatis;
import org.yx.exception.HttpException;
import org.yx.http.handler.ReqBodyHandler;

public final class InnerHttpUtil {
	private static HttpKit kit = new DefaultHttpKit();

	public static HttpKit getKit() {
		return kit;
	}

	public static void setKit(HttpKit kit) {
		Objects.requireNonNull(kit);
		InnerHttpUtil.kit = kit;
	}

	static final int MAXLENGTH = 1024 * 1024 * 100;

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

	public static String getType(HttpServletRequest req) {
		return kit.getType(req);
	}

	public static Charset charset(HttpServletRequest req) {
		return kit.charset(req);
	}

	public static void error(HttpServletResponse resp, int httpStatus, int code, String errorMsg, Charset charset)
			throws IOException {
		kit.error(resp, httpStatus, code, errorMsg, charset);
	}

	public static void error(HttpServletResponse resp, int code, String errorMsg, Charset charset) throws IOException {
		kit.error(resp, code, errorMsg, charset);
	}

	public static void noCache(HttpServletResponse resp) {
		kit.noCache(resp);
	}

	public static void act(String act, long time, boolean isSuccess) {
		kit.act(act, time, isSuccess);
	}

	public static ActStatis getActStatic() {
		return kit.actStatis();
	}

}
