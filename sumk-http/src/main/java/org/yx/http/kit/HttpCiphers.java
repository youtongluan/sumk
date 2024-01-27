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

import org.yx.common.util.S;
import org.yx.http.HttpEncryptor;
import org.yx.http.Signer;

public final class HttpCiphers {

	private static HttpEncryptor encryptor = new DefaultHttpEncryptor();

	public static HttpEncryptor getEncryptor() {
		return encryptor;
	}

	public static void setEncryptor(HttpEncryptor encryptor) {
		HttpCiphers.encryptor = Objects.requireNonNull(encryptor);
	}

	private static Signer signer = (bs, httpServletRequest) -> S.hash().digestByteToString(bs);

	public static Signer getSigner() {
		return signer;
	}

	public static void setSigner(Signer signer) {
		HttpCiphers.signer = Objects.requireNonNull(signer);
	}

}
