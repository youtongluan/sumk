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
package org.test.web.client;

import java.security.MessageDigest;

/**
 * 客户端使用这个类进行加解密、签名
 */
public class Encrypt {

	private static byte[] salt = "j33weut305mTUhgueot5x386fjsjowut03185".getBytes();

	public static byte[] encrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		byte[] ret = new byte[contentBytes.length];
		for (int i = 0; i < contentBytes.length; i++) {
			ret[i] = (byte) (contentBytes[i] ^ key[i % key.length] ^ salt[i % salt.length]);
		}
		return ret;
	}

	public static byte[] decrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		byte[] ret = new byte[contentBytes.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) (contentBytes[i] ^ salt[i % salt.length] ^ key[i % key.length]);
		}
		return ret;
	}

	/**
	 * 对提交数据的明文进行签名
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data) throws Exception {
		return parseByte2HexStr(encryptByte(data));
	}

	public static byte[] encryptByte(byte[] data) throws Exception {
		MessageDigest md = MessageDigest.getInstance("md5");
		md.update(data);
		return md.digest();
	}

	private static String parseByte2HexStr(byte buf[]) {
		StringBuilder sb = new StringBuilder(32);
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

}
