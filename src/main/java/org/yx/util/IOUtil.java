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
package org.yx.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import org.yx.common.sumk.UnsafeByteArrayOutputStream;
import org.yx.log.RawLog;

public final class IOUtil {

	public static byte[] readAllBytes(InputStream in, boolean closeInput) throws IOException {
		UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream(1024);
		transferTo(in, out, closeInput);
		out.close();
		return out.toByteArray();
	}

	public static String readAll(Reader in, boolean closeInput) throws IOException {
		StringBuilder sb = new StringBuilder();
		int len;
		char[] buf = new char[1024];
		try {
			while ((len = in.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}
		} finally {
			if (closeInput) {
				try {
					in.close();
				} catch (Exception e) {
					RawLog.error("sumk.sys", e);
				}
			}
		}
		return sb.toString();
	}

	public static int transferTo(InputStream in, OutputStream output, boolean closeInput) throws IOException {
		if (in == null) {
			return 0;
		}
		try {
			int n = 0;
			int count = 0;
			byte[] temp = new byte[1024];
			while (-1 != (n = in.read(temp))) {
				output.write(temp, 0, n);
				count += n;
			}
			return count;
		} finally {
			if (closeInput) {
				try {
					in.close();
				} catch (Exception e) {
					RawLog.error("sumk.sys", e);
				}
			}
		}
	}
}
