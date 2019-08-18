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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.yx.conf.AppInfo;

public class ThreadPools {

	public static ExecutorService create(String type, int core, int max) {
		return create(type, type, core, max);
	}

	public static SumkExecutorService create(String type, String configPrefix, int core, int max) {
		return create(type, configPrefix, core, max, 30000);
	}

	public static SumkExecutorService create(String type, String configPrefix, int core, int max, int aliveTime) {
		if (configPrefix.endsWith(".")) {
			configPrefix = configPrefix.substring(0, configPrefix.length() - 1);
		}
		core = AppInfo.getInt(configPrefix + ".threadpool.core", core);
		AbortPolicyQueue abortPolicyQueue = new AbortPolicyQueue(
				AppInfo.getInt(configPrefix + ".threadpool.maxqueue", 100000), core);
		ThresholdThreadPool pool = new ThresholdThreadPool(core, AppInfo.getInt(configPrefix + ".threadpool.max", max),
				AppInfo.getInt(configPrefix + ".threadpool.alive", aliveTime), TimeUnit.MILLISECONDS, abortPolicyQueue,
				new DefaultThreadFactory(type), abortPolicyQueue,
				AppInfo.getInt(configPrefix + ".threshold", ThresholdExecutor.DEFAULT_THRESHOLD));
		abortPolicyQueue.pool = pool;
		if (AppInfo.getBoolean(configPrefix + ".threadpool.allowCoreThreadTimeOut", false)) {
			pool.allowCoreThreadTimeOut(true);
		}
		return pool;
	}

	private static class AbortPolicyQueue extends LinkedBlockingQueue<Runnable> implements RejectedExecutionHandler {

		private SumkExecutorService pool;
		private static final long serialVersionUID = 1L;
		private int softCapacity;

		AbortPolicyQueue(int capacity, int softCapacity) {
			super(capacity);
			this.softCapacity = softCapacity;
		}

		@Override
		public boolean offer(Runnable r) {
			int size = this.size();
			if (size > softCapacity) {
				return false;
			}

			return super.offer(r);
		}

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			if (pool.isShutdown()) {
				throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString()
						+ ",because of Thread pool shutdowned");
			}

			if (PriorityRunnable.class == r.getClass()
					&& PriorityRunnable.class.cast(r).priority() < pool.threshold()) {
				String msg = new StringBuilder().append("Task ").append(r.toString()).append(" rejected from ")
						.append(e.toString()).append(", because of ").append(PriorityRunnable.class.cast(r).priority())
						.append(" lower than ").append(pool.threshold()).toString();
				throw new RejectedExecutionException(msg);
			}
			if (!super.offer(r)) {
				throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
			}
		}
	}

	private static class DefaultThreadFactory implements ThreadFactory {
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		DefaultThreadFactory(String type) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = type + "-";
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
			t.setDaemon(true);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
