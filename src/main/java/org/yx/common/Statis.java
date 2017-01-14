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
package org.yx.common;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Statis {
	public final String name;

	public final AtomicInteger count;

	public final AtomicLong time;

	public final AtomicInteger failedCount;
	public final AtomicLong failedTime;

	public Statis(String name) {
		this.name = name;
		this.count = new AtomicInteger(0);
		this.time = new AtomicLong(0);
		this.failedCount = new AtomicInteger(0);
		this.failedTime = new AtomicLong(0);
	}

	/**
	 * 增加一次访问
	 * 
	 * @param t
	 */
	public void successVisit(long t) {
		this.count.incrementAndGet();
		this.time.addAndGet(t);
	}

	public void failedVisit(long t) {
		this.failedCount.incrementAndGet();
		this.failedTime.addAndGet(t);
	}

	@Override
	public String toString() {
		return name + ": count=" + count + ", time=" + time + ", failedCount=" + failedCount + ", failedTime="
				+ failedTime;
	}

}
