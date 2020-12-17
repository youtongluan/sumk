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
package org.yx.http.user;

import java.util.Arrays;
import java.util.Objects;

import org.yx.conf.AppInfo;

public class TimedCachedObject {
	long refreshTime;
	final String json;
	final byte[] key;

	public TimedCachedObject(String json, byte[] key) {
		this.json = Objects.requireNonNull(json);
		this.key = Objects.requireNonNull(key);
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public String getJson() {
		return json;
	}

	public byte[] getKey() {
		return key;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public final boolean isExpired(long duration, long now) {
		return this.refreshTime + duration < now;
	}

	public static byte[] toBytes(String json, byte[] key) {
		int keyLength = key.length;
		int b1 = keyLength & 0xFF;
		int b0 = (keyLength >> 8) & 0xFF;
		byte[] j = json.getBytes(AppInfo.UTF8);
		byte[] ret = new byte[2 + keyLength + j.length];
		ret[0] = (byte) b0;
		ret[1] = (byte) b1;
		System.arraycopy(key, 0, ret, 2, keyLength);
		System.arraycopy(j, 0, ret, 2 + keyLength, j.length);
		return ret;
	}

	public static TimedCachedObject deserialize(byte[] bv) {
		if (bv == null || bv.length < 3) {
			return null;
		}

		int keyLength = (bv[0] & 0xff) << 8 | (bv[1] & 0xff);
		String json = new String(bv, 2 + keyLength, bv.length - 2 - keyLength, AppInfo.UTF8);
		return new TimedCachedObject(json, Arrays.copyOfRange(bv, 2, 2 + keyLength));
	}
}
