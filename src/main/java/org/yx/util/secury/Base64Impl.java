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

import org.yx.conf.AppInfo;

public final class Base64Impl implements Base64 {

	public static final Base64 inst = new Base64Impl(java.util.Base64.getEncoder(), java.util.Base64.getMimeDecoder());

	private final Decoder decoder;

	private final Encoder encoder;

	public Base64Impl(Encoder encoder, Decoder decoder) {
		this.encoder = encoder;
		this.decoder = decoder;
	}

	@Override
	public byte[] decode(byte[] src) {
		return decoder.decode(src);
	}

	@Override
	public byte[] decode(String src) {
		return decoder.decode(src.getBytes(AppInfo.UTF8));
	}

	@Override
	public byte[] encode(byte[] src) {
		return encoder.encode(src);
	}

	@Override
	public String encodeToString(byte[] src) {
		return new String(encoder.encode(src), AppInfo.UTF8);
	}
}
