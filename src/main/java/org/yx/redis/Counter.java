/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.redis;

public class Counter {

	private volatile long visitCount = 0;

	private volatile long cacheCount = 0;

	private int interval;

	public Counter(int interval) {
		super();
		this.interval = interval;
	}

	public long getVisitCount() {
		return visitCount;
	}

	public long getCachedCount() {
		return cacheCount;
	}

	public boolean isCacheRefresh() {
		++visitCount;
		return interval > 0 && visitCount == interval;
	}

	public void incCached() {
		cacheCount++;
	}

	@Override
	public String toString() {
		return "visitCount=" + visitCount + ", cacheCount=" + cacheCount;
	}

}
