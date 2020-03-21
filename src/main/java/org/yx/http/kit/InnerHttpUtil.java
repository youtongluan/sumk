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
import org.yx.conf.AppInfo;
import org.yx.exception.HttpException;
import org.yx.http.handler.ReqBodyHandler;
import org.yx.log.Logs;

public final class InnerHttpUtil {
	private static HttpKit kit = new DefaultHttpKit();

	public static HttpKit getKit() {
		return kit;
	}

	public static void setKit(HttpKit kit) {
		InnerHttpUtil.kit = Objects.requireNonNull(kit);
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

	public static Charset charset(HttpServletRequest req) {
		return kit.charset(req);
	}

	public static void noCache(HttpServletResponse resp) {
		kit.noCache(resp);
	}

	public static void record(String act, long time, boolean isSuccess) {
		kit.record(act, time, isSuccess);
	}

	public static ActStatis getActStatic() {
		return kit.actStatis();
	}

	public static boolean checkGetMethod(HttpServletResponse resp) throws IOException {
		if (AppInfo.getBoolean("sumk.http.get.enable", true)) {
			return true;
		}
		Logs.http().info("get方法被禁用了");
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "not allowd");
		return false;
	}

	public static void sendError(HttpServletResponse resp, int code, String message, Charset charset) {
		try {
			kit.sendError(resp, HttpSettings.getErrorHttpStatus(), code, message, charset);
		} catch (IOException e) {
			Logs.http().error(e.getLocalizedMessage(), e);
		}
	}

	public static void setRespHeader(HttpServletResponse resp, Charset charset) throws IOException {
		kit.setRespHeader(resp, charset);
	}

}
