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

import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.yx.common.Daemon;
import org.yx.common.thread.SumkExecutorService;
import org.yx.common.thread.ThreadPools;
import org.yx.common.thread.ThresholdThreadPool;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.exception.ErrorCode;
import org.yx.exception.SimpleBizException;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;

public class SumkThreadPool {

	public static final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(
			AppInfo.getInt("sumk.schedule.thread", 2), new ThreadFactory() {
				private final AtomicInteger threadNumber = new AtomicInteger(1);

				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setName("sumk-task-" + threadNumber.getAndIncrement());
					t.setDaemon(true);
					if (t.getPriority() != Thread.NORM_PRIORITY) {
						t.setPriority(Thread.NORM_PRIORITY);
					}
					return t;
				}
			});

	public static final SumkExecutorService EXECUTOR = ThreadPools.create("sumk", "sumk.pool", 50, 500);

	public static final BizException THREAD_THRESHOLD_OVER = new SimpleBizException(ErrorCode.THREAD_THRESHOLD_OVER,
			"系统限流降级");

	private static List<Thread> deamonThreads = new ArrayList<>();
	static {
		scheduledExecutor.scheduleAtFixedRate(() -> {

			int threshold = AppInfo.getInt("sumk.threadpool.threshold", -1);
			if (threshold > 0) {
				EXECUTOR.threshold(threshold);
				return;
			}
			if (!ThresholdThreadPool.class.isInstance(EXECUTOR)) {
				return;
			}
			ThresholdThreadPool pool = (ThresholdThreadPool) EXECUTOR;
			threshold = pool.getPoolSize() + pool.getQueue().size();
			EXECUTOR.threshold(threshold);
			Log.get("sumk.thread").trace("set pool threshold to {}", threshold);
		}, 0, 5, TimeUnit.SECONDS);
	}

	public static synchronized void runDeamon(Daemon deamon, String threadName) {
		if (SumkServer.isDestoryed()) {
			return;
		}
		Runnable r = () -> {
			while (true) {
				if (SumkServer.isDestoryed()) {
					break;
				}
				try {
					if (!deamon.run()) {
						break;
					}
				} catch (Exception e) {
					if (InterruptedException.class.isInstance(e) || ClosedByInterruptException.class.isInstance(e)) {
						Thread.interrupted();
						continue;
					}
					Log.printStack(e);
				}
			}
			try {
				Thread.interrupted();
				deamon.close();
			} catch (Exception e) {
				ConsoleLog.get("sumk.SYS").error(e.getMessage(), e);
			}
			ConsoleLog.get("sumk.SYS").info("{} stoped", threadName);
		};
		Thread t = new Thread(r, threadName);
		t.setDaemon(true);
		t.start();
		deamonThreads.add(t);
	}

	public static void shutdown() {
		scheduledExecutor.shutdown();
		EXECUTOR.shutdown();
		deamonThreads.forEach(t -> t.interrupt());
		deamonThreads.forEach(t -> {
			try {
				t.join(AppInfo.getLong("sumk.thread.jointime", 3000));
			} catch (InterruptedException e) {
			}
		});
		deamonThreads.clear();
	}
}
