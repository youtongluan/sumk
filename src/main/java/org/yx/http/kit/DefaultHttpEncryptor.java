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

import java.util.Objects;

import org.yx.exception.BizException;
import org.yx.http.HttpEncryptor;
import org.yx.http.HttpErrorCode;
import org.yx.http.handler.WebContext;
import org.yx.util.S;
import org.yx.util.WebUtil;
import org.yx.util.secury.Encryptor;

public class DefaultHttpEncryptor implements HttpEncryptor {

	private Encryptor cipher = S.cipher();

	protected byte[] getKey() {
		byte[] key = WebUtil.getSessionEncryptKey();
		if (key == null) {
			throw new BizException(HttpErrorCode.SESSION_KEY_NOT_FOUND, "加密用的key没有找到");
		}
		return key;
	}

	@Override
	public byte[] encrypt(byte[] data, WebContext ctx) throws Exception {
		return cipher.encrypt(data, getKey());
	}

	@Override
	public byte[] decrypt(byte[] data, WebContext ctx) throws Exception {
		return cipher.decrypt(data, getKey());
	}

	public Encryptor getCipher() {
		return cipher;
	}

	public void setCipher(Encryptor cipher) {
		this.cipher = Objects.requireNonNull(cipher);
	}
}
