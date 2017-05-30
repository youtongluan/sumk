/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import org.yx.common.Deamon;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

public class SumkThreadPool {

	public static final ScheduledThreadPoolExecutor scheduledExecutor;
	private static List<Thread> deamonThreads = new ArrayList<>();

	public static synchronized void runDeamon(Deamon runnable, String threadName) {
		if (SumkServer.isDestoryed()) {
			return;
		}
		Runnable r = () -> {
			while (true) {
				try {
					if (SumkServer.isDestoryed()) {
						break;
					}
					runnable.run();
				} catch (InterruptedException e1) {

				} catch (Exception e) {
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

	static {
		scheduledExecutor = new ScheduledThreadPoolExecutor(AppInfo.getInt("sumk.schedule.thread", 1),
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						t.setDaemon(true);
						return t;
					}

				});
	}

	public static void shutdown() {
		scheduledExecutor.shutdown();
		deamonThreads.forEach(Thread::interrupt);
		deamonThreads.clear();
	}
}
