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
package org.yx.common.util.secury;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class CommonDigest implements Hasher {
	protected final String algorithm;

	public CommonDigest(String algorithm) {
		this.algorithm = algorithm;
	}

	public String digest(String data, Charset charset) throws Exception {
		return digestByteToString(data.getBytes(charset));
	}

	public byte[] digest(byte[] data) throws Exception {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(data);
		return md.digest();
	}

	public String parseByte2HexStr(byte buf[]) {
		StringBuilder sb = new StringBuilder(32);
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString().toLowerCase();
	}

	@Override
	public String digestByteToString(byte[] data) throws Exception {
		return parseByte2HexStr(digest(data));
	}
}
