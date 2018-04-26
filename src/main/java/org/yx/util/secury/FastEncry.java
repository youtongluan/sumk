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

import org.yx.conf.AppInfo;

public class FastEncry implements Encry {

	private byte[] salt = "j33weut305mTUhgueot5x386fjsjowut03185".getBytes();

	public FastEncry() {
		String s = AppInfo.get("sumk.encry.fast.salt");
		if (s != null && s.length() > 5) {
			this.salt = s.getBytes();
		}
	}

	@Override
	public byte[] encrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		byte[] ret = new byte[contentBytes.length];
		for (int i = 0; i < contentBytes.length; i++) {
			ret[i] = (byte) (contentBytes[i] ^ key[i % key.length] ^ salt[i % salt.length]);
		}
		return ret;
	}

	@Override
	public byte[] decrypt(byte[] contentBytes, byte[] key) throws Exception {
		if (contentBytes == null || contentBytes.length == 0) {
			return contentBytes;
		}
		byte[] ret = new byte[contentBytes.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) (contentBytes[i] ^ salt[i % salt.length] ^ key[i % key.length]);
		}
		return ret;
	}

}
