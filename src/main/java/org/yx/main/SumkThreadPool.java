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
import java.util.concurrent.atomic.AtomicInteger;

import org.yx.common.JobStep;
import org.yx.common.thread.SumkExecutorService;
import org.yx.common.thread.ThreadPools;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.exception.ErrorCode;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;

public final class SumkThreadPool {

	private static boolean daemon;

	public static boolean isDaemon() {
		return daemon;
	}

	public static void setDaemon(boolean daemon) {
		SumkThreadPool.daemon = daemon;
	}

	private static final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(2,
			new ThreadFactory() {
				private final AtomicInteger threadNumber = new AtomicInteger(1);

				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setName("sumk-task-" + threadNumber.getAndIncrement());
					t.setDaemon(daemon);
					if (t.getPriority() != Thread.NORM_PRIORITY) {
						t.setPriority(Thread.NORM_PRIORITY);
					}
					return t;
				}
			});

	public static ScheduledThreadPoolExecutor scheduledExecutor() {
		return scheduledExecutor;
	}

	public static SumkExecutorService executor() {
		return ThreadPools.DEFAULT_EXECUTOR;
	}

	public static final BizException THREAD_THRESHOLD_OVER = BizException.create(ErrorCode.THREAD_THRESHOLD_OVER,
			"系统限流降级");

	private static final List<Thread> jobThreads = new ArrayList<>();

	public static synchronized void loop(JobStep step, String threadName) {
		if (SumkServer.isDestoryed()) {
			return;
		}
		Runnable r = () -> {
			while (true) {
				if (SumkServer.isDestoryed()) {
					break;
				}
				try {
					if (!step.run()) {
						break;
					}
				} catch (Exception e) {
					if (InterruptedException.class.isInstance(e) || ClosedByInterruptException.class.isInstance(e)) {
						Thread.interrupted();
						continue;
					}
					Log.printStack("sumk.error", e);
				}
			}
			try {
				Thread.interrupted();
				step.close();
			} catch (Exception e) {
				ConsoleLog.get("sumk.SYS").error(e.getMessage(), e);
			}
			ConsoleLog.get("sumk.SYS").info("{} stoped", threadName);
		};
		Thread t = new Thread(r, threadName);
		t.setDaemon(daemon);
		t.start();
		jobThreads.add(t);
	}

	public static void shutdown() {
		scheduledExecutor.shutdown();
		executor().shutdown();
		jobThreads.forEach(t -> t.interrupt());
		jobThreads.forEach(t -> {
			try {
				t.join(AppInfo.getLong("sumk.thread.jointime", 3000));
			} catch (InterruptedException e) {
			}
		});
		jobThreads.clear();
	}
}
