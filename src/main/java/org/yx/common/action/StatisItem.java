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
package org.yx.common.action;

import java.util.Objects;

public class StatisItem {
	private final String name;
	private int successCount;
	private long successTime;
	private int failedCount;
	private long failedTime;

	public StatisItem(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public void successVisit(long t) {
		successCount++;
		successTime += t;
	}

	public void failedVisit(long t) {
		failedCount++;
		failedTime += t;
	}

	@Override
	public String toString() {
		return name + ": count=" + getSuccessCount() + ", time=" + getSuccessTime() + ", failedCount="
				+ getFailedCount() + ", failedTime=" + getFailedTime();
	}

	public String toSimpleString() {
		long c = getSuccessCount();
		long t = getSuccessTime();
		double avg = c == 0 ? 0 : t * 1d / c;
		return String.join("   ", name, String.valueOf(c), String.valueOf(t), String.valueOf(Math.round(avg)),
				String.valueOf(getFailedCount()), String.valueOf(getFailedTime()));
	}

	public static String header() {
		return "name  success  time  avg  failed  failedTime";
	}

	public String getName() {
		return name;
	}

	public long getSuccessCount() {
		return Integer.toUnsignedLong(this.successCount);
	}

	public long getSuccessTime() {
		return this.successTime;
	}

	public long getFailedCount() {
		return Integer.toUnsignedLong(this.failedCount);
	}

	public long getFailedTime() {
		return this.failedTime;
	}

}
