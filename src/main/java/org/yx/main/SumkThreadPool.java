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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.yx.common.Deamon;
import org.yx.common.ThreadPools;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

public class SumkThreadPool {

	public static final ScheduledThreadPoolExecutor scheduledExecutor;

	public static final ExecutorService EXECUTOR = ThreadPools.create("sumk", "sumk.pool", 50, 500);

	private static List<Thread> deamonThreads = new ArrayList<>();
	static {
		scheduledExecutor = new ScheduledThreadPoolExecutor(AppInfo.getInt("sumk.schedule.thread", 2),
				new ThreadFactory() {
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
	}

	public static synchronized void runDeamon(Deamon deamon, String threadName) {
		if (SumkServer.isDestoryed()) {
			return;
		}
		Runnable r = () -> {
			while (true) {
				if (SumkServer.isDestoryed()) {
					try {
						Thread.interrupted();
						deamon.close();
					} catch (Exception e) {
						Log.get("sumk.log").trace(e.getMessage(), e);
					}
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
			Log.get("sumk.SYS").info("{} stoped", threadName);
		};
		Thread t = new Thread(r, threadName);
		t.setDaemon(true);
		t.start();
		deamonThreads.add(t);
	}

	public static void shutdown() {
		scheduledExecutor.shutdown();
		EXECUTOR.shutdown();
		deamonThreads.forEach(Thread::interrupt);
		deamonThreads.forEach(t -> {
			try {
				t.join(2000);
			} catch (InterruptedException e) {
			}
		});
		deamonThreads.clear();
	}
}
