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
package org.yx.bean.watcher;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.common.thread.SumkExecutorService;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.main.SumkServer;
import org.yx.main.SumkThreadPool;
import org.yx.validate.Validators;

public class PluginBooter {

	public void start() {
		Validators.init();
		startBeans();
		Runtime.getRuntime().addShutdownHook(new Thread(SumkServer::stop));
	}

	private void startBeans() {
		List<Plugin> plugins = IOC.getBeans(Plugin.class);
		if (plugins == null || plugins.isEmpty()) {
			return;
		}
		for (Plugin plugin : plugins) {
			plugin.prepare();
		}
		CountDownLatch latch = new CountDownLatch(plugins.size());
		SumkExecutorService executor = SumkThreadPool.executor();
		for (Plugin plugin : plugins) {
			executor.execute(() -> {
				try {
					plugin.startAsync();
					latch.countDown();
					Logs.ioc().debug("{} startAsync finished", plugin.getClass().getSimpleName());
				} catch (Throwable e) {
					Logs.ioc().error(plugin.getClass().getSimpleName() + " start failed", e);
					System.exit(1);
				}
			});
		}
		preHotCoreThreads(executor);
		long timeout = AppInfo.getLong("sumk.start.timeout", 1000L * 600);
		try {
			if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
				Logs.ioc().error("plugins failed to start in {}ms", timeout);
				System.exit(1);
			}
		} catch (InterruptedException e) {
			Logs.ioc().error("receive InterruptedException in plugin starting");
			Thread.currentThread().interrupt();
			System.exit(1);
		}
		for (Plugin plugin : plugins) {
			plugin.afterStarted();
		}
		Logs.ioc().debug("all plugin started");
	}

	private void preHotCoreThreads(SumkExecutorService executor) {
		if (AppInfo.getBoolean("sumk.thread.prestartAllCoreThreads", true)
				&& (SumkServer.isHttpEnable() || SumkServer.isRpcEnable()) && executor instanceof ThreadPoolExecutor) {
			ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
			pool.prestartAllCoreThreads();
		}
	}

}
