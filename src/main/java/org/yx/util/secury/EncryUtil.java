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
package org.yx.util.secury;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryUtil {

	public static byte[] encrypt(byte[] contentBytes, byte[] key) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encryptResult = cipher.doFinal(contentBytes);
		return encryptResult;
	}

	public static byte[] decrypt(byte[] contentBytes, byte[] key) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return cipher.doFinal(contentBytes);

	}

	public static byte[] simpleEncrypt(String content, byte[] key) throws Exception {
		byte[] ret = content.getBytes("UTF-8");
		for (int i = 0; i < ret.length; i++) {
			ret[i] ^= key[i % key.length];
		}
		return ret;
	}

	public static String simpleDecrypt(byte[] contentBytes, byte[] key) throws Exception {
		byte[] ret = new byte[contentBytes.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) (contentBytes[i] ^ key[i % key.length]);
		}
		return new String(ret, "UTF-8");

	}

}
