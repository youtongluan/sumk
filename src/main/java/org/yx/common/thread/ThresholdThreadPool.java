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
package org.yx.common.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.yx.log.Log;

public class ThresholdThreadPool extends ThreadPoolExecutor implements SumkExecutorService {

	private int threshold;

	public ThresholdThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler,
			int threshold) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		this.threshold = threshold;
	}

	public void execute(Runnable command, int priority) {
		if (priority < threshold) {
			String msg = new StringBuilder().append("Task ").append(toString()).append(" discarded, because of ")
					.append(priority).append(" lower than ").append(threshold).toString();
			Log.get("sumk.thread").warn(msg);
			return;
		}
		this.execute(command);
	}

	@Override
	public int threshold() {
		return threshold;
	}

	@Override
	public void threshold(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public void setThreadFactory(ThreadFactory threadFactory) {
	}

}
