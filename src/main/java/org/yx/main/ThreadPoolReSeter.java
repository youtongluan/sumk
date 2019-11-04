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
package org.yx.main;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.yx.common.thread.SumkExecutorService;
import org.yx.common.thread.ThresholdThreadPool;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

public class ThreadPoolReSeter implements Runnable {

	private Logger logger = Log.get("sumk.thread");

	@Override
	public void run() {
		try {
			synchronized (ThreadPoolReSeter.class) {
				resetCurrentThreshold();
				resetThreadPoolSize();
				resetScheduledThreadPoolSize();
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	private void resetCurrentThreshold() {

		int threshold = AppInfo.getInt("sumk.core.threadpool.threshold", 0);
		SumkExecutorService executor = SumkThreadPool.executor();
		if (threshold > 0) {
			executor.threshold(threshold);
			return;
		}
		if (!ThresholdThreadPool.class.isInstance(executor)) {
			return;
		}

		ThresholdThreadPool pool = (ThresholdThreadPool) executor;
		threshold = pool.getPoolSize() + pool.getQueue().size();
		executor.threshold(threshold);
		logger.trace("set pool threshold to {}", threshold);
	}

	private void resetThreadPoolSize() {
		if (!ThreadPoolExecutor.class.isInstance(SumkThreadPool.executor())) {
			return;
		}
		ThreadPoolExecutor pool = (ThreadPoolExecutor) SumkThreadPool.executor();
		int size = AppInfo.getInt("sumk.core.threadpool.core", 0);
		if (size > 0 && pool.getCorePoolSize() != size) {
			logger.info("change ThreadPool size from {} to {}", pool.getCorePoolSize(), size);
			pool.setCorePoolSize(size);
		}

		size = AppInfo.getInt("sumk.core.threadpool.max", 0);
		if (size > 0 && pool.getMaximumPoolSize() != size) {
			logger.info("change ThreadPool max size from {} to {}", pool.getMaximumPoolSize(), size);
			pool.setMaximumPoolSize(size);
		}

		size = AppInfo.getInt("sumk.core.threadpool.aliveTime", 0);
		if (size > 0 && pool.getKeepAliveTime(TimeUnit.MILLISECONDS) != size) {
			logger.info("change ThreadPool keepalive time from {} to {}", pool.getKeepAliveTime(TimeUnit.MILLISECONDS),
					size);
			pool.setKeepAliveTime(size, TimeUnit.MILLISECONDS);
		}

		String v = AppInfo.get("sumk.core.threadpool.allowCoreThreadTimeOut", null);
		if (v != null) {
			boolean allowCoreTimeout = "1".equals(v) || "true".equalsIgnoreCase(v);
			if (allowCoreTimeout != pool.allowsCoreThreadTimeOut()) {
				logger.info("change ThreadPool allowsCoreThreadTimeOut from {} to {}", pool.allowsCoreThreadTimeOut(),
						allowCoreTimeout);
				pool.allowCoreThreadTimeOut(allowCoreTimeout);
			}
		}
	}

	private void resetScheduledThreadPoolSize() {
		int size = AppInfo.getInt("sumk.core.schedule.thread", 0);
		ScheduledThreadPoolExecutor schedule = SumkThreadPool.scheduledExecutor();
		if (size > 0 && schedule.getCorePoolSize() != size) {
			logger.info("change Schedule ThreadPool size from {} to {}", schedule.getCorePoolSize(), size);
			schedule.setCorePoolSize(size);
		}
	}
}
