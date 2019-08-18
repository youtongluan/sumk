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

import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public final class Base64 {

	private Base64() {

	}

	public static final Base64 inst = new Base64();
	private final Decoder decoder = java.util.Base64.getMimeDecoder();

	private final Encoder encoder = java.util.Base64.getEncoder();

	/**
	 * 解码，是否含有\r\n都能解码
	 * 
	 * @param src
	 *            数据源
	 * @return 明文
	 */
	public byte[] decode(byte[] src) {
		return decoder.decode(src);
	}

	public byte[] encode(byte[] src) {
		return encoder.encode(src);
	}
}
