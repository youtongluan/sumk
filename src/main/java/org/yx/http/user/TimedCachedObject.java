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

import java.util.Objects;

public class TimedCachedObject {
	long refreshTime;
	final String json;
	final byte[] key;

	public TimedCachedObject(String json, byte[] key, long refreshTime) {
		this.json = Objects.requireNonNull(json);
		this.key = Objects.requireNonNull(key);
		this.refreshTime = refreshTime;
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
}
