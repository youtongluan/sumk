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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;

public class ThreadPools {

	public static final SumkExecutorService DEFAULT_EXECUTOR = ThreadPools.create("sumk", 50, 500,
			Integer.getInteger("sumk.thread.pool.keepAliveTime", 30000));

	public static SumkExecutorService create(String name, int core, int max, int aliveTime) {
		AbortPolicyQueue abortPolicyQueue = new AbortPolicyQueue(AppInfo.getInt("sumk.threadpool.maxqueue", 100000),
				core);
		ThresholdThreadPool pool = new ThresholdThreadPool(core, max, aliveTime, TimeUnit.MILLISECONDS,
				abortPolicyQueue, SumkThreadPool.createThreadFactory(name + "-"), abortPolicyQueue, 0);
		pool.setCorePoolSize(core);
		abortPolicyQueue.pool = pool;
		return pool;
	}

	private static final class AbortPolicyQueue extends LinkedBlockingQueue<Runnable>
			implements RejectedExecutionHandler {

		private static final long serialVersionUID = 1L;
		private SumkExecutorService pool;
		private int softCapacity;

		AbortPolicyQueue(int capacity, int softCapacity) {
			super(capacity);
			this.softCapacity = softCapacity;
		}

		@Override
		public boolean offer(Runnable r) {
			if (this.size() > softCapacity) {
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
			int waiting = this.size();
			if (waiting % 10 == 0 && Log.get("sumk.thread").isWarnEnabled()) {
				Log.get("sumk.thread").warn("task is busy,waiting size:{}", waiting);
			}
			if (!super.offer(r)) {
				throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
			}
		}
	}

	private static class ThresholdThreadPool extends ThreadPoolExecutor implements SumkExecutorService {

		private int threshold;

		public ThresholdThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
				BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler,
				int threshold) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
			this.threshold = threshold;
		}

		public void execute(Runnable command, int toplimit) {
			if (toplimit < threshold) {
				String msg = new StringBuilder().append("Task ").append(toString())
						.append(" discarded, because toplimit ").append(toplimit).append(" lower than ")
						.append(threshold).toString();
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

		@Override
		public void setCorePoolSize(int corePoolSize) {
			super.setCorePoolSize(corePoolSize);
			AbortPolicyQueue q = (AbortPolicyQueue) this.getQueue();
			q.softCapacity = (int) (corePoolSize * 0.7);
		}

		@Override
		public int getQueued() {
			return this.getQueue().size();
		}
	}
}
