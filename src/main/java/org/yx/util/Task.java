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
package org.yx.util;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.yx.main.SumkThreadPool;

public final class Task {
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable job, long delayMS, long periodMS) {
		return SumkThreadPool.scheduleAtFixedRate(job, delayMS, periodMS);
	}

	public static ScheduledFuture<?> schedule(Runnable job, long delayMS) {
		return SumkThreadPool.schedule(job, delayMS);
	}

	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable job, long delay, long period, TimeUnit unit) {
		return scheduleAtFixedRate(job, unit.toMillis(delay), unit.toMillis(period));
	}
}
