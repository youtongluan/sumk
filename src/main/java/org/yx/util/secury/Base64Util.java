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
package org.yx.util.secury;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public final class Base64Util {

	private static final Decoder decoder = Base64.getMimeDecoder();

	private static final Encoder encoder = Base64.getEncoder();

	/**
	 * 解码，是否含有\r\n都能解码
	 * 
	 * @param src
	 *            数据源
	 * @return 明文
	 */
	public static byte[] decode(byte[] src) {
		return decoder.decode(src);
	}

	public static byte[] encode(byte[] src) {
		return encoder.encode(src);
	}
}
