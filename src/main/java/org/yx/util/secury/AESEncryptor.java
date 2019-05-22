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
import javax.crypto.spec.SecretKeySpec;

import org.yx.conf.AppInfo;

public class AESEncryptor implements Encryptor {

	private String c = "AES/ECB/ISO10126Padding";
	private String algorithm = "AES";

	public AESEncryptor() {
		String s = AppInfo.get("sumk.encry.aes.cipher");
		if (s != null && s.length() > 1) {
			this.c = s;
			this.algorithm = c.split("/")[0];
		}
	}

	@Override
	public byte[] encrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
		Cipher cipher = Cipher.getInstance(c);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encryptResult = cipher.doFinal(contentBytes);
		return encryptResult;
	}

	@Override
	public byte[] decrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
		Cipher cipher = Cipher.getInstance(c);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return cipher.doFinal(contentBytes);

	}

}
