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
package org.yx.common;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StatisItem {
	private final String name;

	private final AtomicInteger count = new AtomicInteger(0);

	private final AtomicLong time = new AtomicLong(0);

	private final AtomicInteger failedCount = new AtomicInteger(0);
	private final AtomicLong failedTime = new AtomicLong(0);

	public StatisItem(String name) {
		this.name = Objects.requireNonNull(name);
	}

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

	public String toSimpleString() {
		long c = count.get();
		long t = time.get();
		double avg = c == 0 ? 0 : t * 1d / c;
		return String.join("   ", name, String.valueOf(c), String.valueOf(t), String.valueOf(Math.round(avg)),
				String.valueOf(failedCount.get()), String.valueOf(failedTime.get()));
	}

	public static String header() {
		return "name  count  time  avg  failedCount  failedTime";
	}

	public String getName() {
		return name;
	}

	public int getSuccessCount() {
		return count.get();
	}

	public long getSuccessTime() {
		return time.get();
	}

	public int getFailedCount() {
		return failedCount.get();
	}

	public long getFailedTime() {
		return failedTime.get();
	}

}
