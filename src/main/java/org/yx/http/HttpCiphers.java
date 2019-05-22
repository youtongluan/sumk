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

import javax.servlet.http.HttpServletRequest;

import org.yx.http.handler.WebContext;
import org.yx.util.S;

public class HttpCiphers {

	private static HttpEncryptor encryptor = new HttpEncryptor() {
		@Override
		public byte[] encrypt(byte[] data, WebContext ctx) throws Exception {
			return S.encryptor.encrypt(data, ctx.key());
		}

		@Override
		public byte[] decrypt(byte[] data, WebContext ctx) throws Exception {
			return S.encryptor.decrypt(data, ctx.key());
		}
	};

	public static HttpEncryptor getEncryptor() {
		return encryptor;
	}

	public static void setEncryptor(HttpEncryptor encryptor) {
		HttpCiphers.encryptor = Objects.requireNonNull(encryptor);
	}

	private static Signer signer = new Signer() {

		@Override
		public String sign(byte[] bs, HttpServletRequest httpServletRequest) throws Exception {
			return S.hasher.digestByteToString(bs);
		}

	};

	public static Signer getSigner() {
		return signer;
	}

	public static void setSigner(Signer signer) {
		HttpCiphers.signer = Objects.requireNonNull(signer);
	}

}
