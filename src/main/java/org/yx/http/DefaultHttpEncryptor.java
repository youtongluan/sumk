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
package org.yx.http;

import java.util.Objects;

import org.yx.http.handler.WebContext;
import org.yx.util.S;
import org.yx.util.secury.Encryptor;

public class DefaultHttpEncryptor implements HttpEncryptor {

	private Encryptor cipher = S.cipher();

	@Override
	public byte[] encrypt(byte[] data, WebContext ctx) throws Exception {
		return cipher.encrypt(data, ctx.key());
	}

	@Override
	public byte[] decrypt(byte[] data, WebContext ctx) throws Exception {
		return cipher.decrypt(data, ctx.key());
	}

	public Encryptor getCipher() {
		return cipher;
	}

	public void setCipher(Encryptor cipher) {
		this.cipher = Objects.requireNonNull(cipher);
	}
}
