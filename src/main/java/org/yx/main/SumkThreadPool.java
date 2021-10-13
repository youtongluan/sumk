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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.yx.common.thread.SumkExecutorService;
import org.yx.common.thread.ThreadPools;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.log.RawLog;
import org.yx.util.kit.Asserts;

public final class SumkThreadPool {

	private static boolean daemon;

	public static void setDaemon(boolean daemon) {
		SumkThreadPool.daemon = daemon;
	}

	public static ThreadFactory createThreadFactory(String pre) {
		return new ThreadFactory() {
			private final AtomicInteger threadNumber = new AtomicInteger(1);

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r, pre + threadNumber.getAndIncrement());
				t.setDaemon(daemon);
				if (t.getPriority() != Thread.NORM_PRIORITY) {
					t.setPriority(Thread.NORM_PRIORITY);
				}
				return t;
			}
		};
	}

	private static final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(
			Integer.getInteger("sumk.thread.schedule.size", 1), r -> {
				Thread t = new Thread(r, "sumk-task");
				t.setDaemon(true);
				if (t.getPriority() != Thread.NORM_PRIORITY) {
					t.setPriority(Thread.NORM_PRIORITY);
				}
				return t;
			});

	static ScheduledThreadPoolExecutor scheduledExecutor() {
		return scheduledExecutor;
	}

	public static SumkExecutorService executor() {
		return ThreadPools.DEFAULT_EXECUTOR;
	}

	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable job, long delayMS, long periodMS) {
		if (SumkServer.isDestoryed()) {
			return null;
		}
		Asserts.requireTrue(periodMS > 0, "delayMils要大于0 才行");
		if (periodMS < 100) {
			RawLog.warn("sumk.thread", job + "加入到作业中(短作业)，定时间隔为" + periodMS + "ms");
		} else {
			RawLog.info("sumk.thread", job + "加入到作业中，定时间隔为" + periodMS + "ms");
		}
		SynchronizedRunner single = new SynchronizedRunner(job);
		Runnable task = () -> {

			if (single.isBusy()) {
				return;
			}
			try {
				executor().execute(single);
				single.working = 1;
			} catch (Exception e) {
				RawLog.error("sumk.thread", "添加定时任务失败", e);
			}
		};
		return scheduledExecutor.scheduleAtFixedRate(task, delayMS, periodMS, TimeUnit.MILLISECONDS);
	}

	public static ScheduledFuture<?> schedule(Runnable job, long delayMS) {
		return scheduledExecutor.schedule(() -> executor().execute(job), delayMS, TimeUnit.MILLISECONDS);
	}

	public static void shutdown() {
		scheduledExecutor.shutdown();
		executor().shutdown();
	}

	public static Runnable synchronize(Runnable target) {
		return new SynchronizedRunner(target);
	}

	private static final class SynchronizedRunner implements Runnable {

		private final ReentrantLock lock;
		private final Runnable target;

		int working;

		public SynchronizedRunner(Runnable target) {
			this.target = target;
			this.lock = new ReentrantLock();
		}

		@Override
		public void run() {
			if (!lock.tryLock()) {
				this.working = 0;
				return;
			}

			try {
				target.run();
			} catch (Throwable e) {
				RawLog.error("sumk.thread", target + "执行失败，" + e.getLocalizedMessage(), e);
			} finally {
				this.working = 0;
				lock.unlock();
			}
		}

		public boolean isBusy() {
			if (lock.isLocked()) {
				return true;
			}
			if (this.working != 0) {
				this.working++;
				if (this.working > 50) {
					this.working = 0;
				}
				return true;
			}
			return false;
		}
	}

	static void scheduleThreadPoolMonitor() {
		long period = AppInfo.getLong("sumk.threadpool.task.period", 10_000);

		scheduledExecutor.scheduleAtFixedRate(new ThreadPoolReSeter(), period, period, TimeUnit.MILLISECONDS);
	}

	private static class ThreadPoolReSeter implements Runnable {

		private Logger logger = Log.get("sumk.thread");

		@Override
		public void run() {
			try {
				resetCurrentThreshold();
				resetThreadPoolSize();
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}

		private void resetCurrentThreshold() {

			int threshold = AppInfo.getInt("sumk.core.threadpool.threshold", 0);
			SumkExecutorService executor = SumkThreadPool.executor();
			if (threshold > 0) {
				executor.threshold(threshold);
				return;
			}

			threshold = executor.getPoolSize() + executor.getQueued();
			executor.threshold(threshold);
			logger.trace("set pool threshold to {}", threshold);
		}

		private void resetThreadPoolSize() {
			SumkExecutorService pool = SumkThreadPool.executor();
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

			String v = AppInfo.get("sumk.core.threadpool.allowCoreThreadTimeOut", null);
			if (v != null) {
				boolean allowCoreTimeout = "1".equals(v) || "true".equalsIgnoreCase(v);
				if (allowCoreTimeout != pool.allowsCoreThreadTimeOut()) {
					logger.info("change ThreadPool allowsCoreThreadTimeOut from {} to {}",
							pool.allowsCoreThreadTimeOut(), allowCoreTimeout);
					pool.allowCoreThreadTimeOut(allowCoreTimeout);
				}
			}
		}

	}
}
