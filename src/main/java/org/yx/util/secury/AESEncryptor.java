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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.yx.log.Logs;

public class AESEncryptor implements Encryptor {

	private static final String AES = "AES";

	private String c = "AES/CBC/PKCS5Padding";

	public AESEncryptor() {
	}

	public AESEncryptor(String cipher) {
		if (cipher != null && cipher.length() > 1) {
			this.c = cipher;
			Logs.system().info("aes cipher:{}", this.c);
		}
	}

	private Cipher getCipher(int mode, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(c);
		if (c.contains("ECB")) {
			cipher.init(mode, new SecretKeySpec(key, AES));
		} else {
			cipher.init(mode, new SecretKeySpec(key, AES), new IvParameterSpec(key));
		}
		return cipher;
	}

	@Override
	public byte[] encrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		return getCipher(Cipher.ENCRYPT_MODE, key).doFinal(contentBytes);
	}

	@Override
	public byte[] decrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		return getCipher(Cipher.DECRYPT_MODE, key).doFinal(contentBytes);
	}

}
