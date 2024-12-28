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
package org.yx.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import org.yx.bean.watcher.BootWatcher;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.main.SumkServer;

public abstract class ParallelBootWatcher implements BootWatcher {

	/**
	 * ioc启动处理
	 * 
	 * @param clz 这个clz是原始的类
	 * @throws Exception 处理时发生的异常
	 */
	protected abstract void handle(Class<?> clz) throws Exception;

	@Override
	public List<Class<?>> publish(List<Class<?>> sortedClasses, Predicate<String> optional) throws Exception {
		ExecutorService executor = createExecutor();
		try {
			parallelPublish(sortedClasses, optional, executor);
		} finally {
			executor.shutdown();
		}
		return null;
	}

	protected void serialPublish(List<Class<?>> sortedClasses, Predicate<String> optional) throws Exception {
		for (Class<?> clz : sortedClasses) {
			BootCallable c = new BootCallable(clz, optional, this);
			c.call();
		}
	}

	protected void parallelPublish(List<Class<?>> scanedClasses, Predicate<String> optional, ExecutorService executor)
			throws InterruptedException, ExecutionException, TimeoutException {
		List<Future<Boolean>> futures = new ArrayList<>(scanedClasses.size());
		for (Class<?> clz : scanedClasses) {
			BootCallable c = new BootCallable(clz, optional, this);
			Future<Boolean> f = executor.submit(c);
			futures.add(f);
		}
		for (Future<Boolean> f : futures) {
			f.get(SumkServer.startTimeout(), TimeUnit.MILLISECONDS);
		}
	}

	protected ExecutorService createExecutor() {
		int poolSize = AppInfo.getInt("sumk.ioc.booter.threads", 4);
		if (poolSize < 1) {
			poolSize = 1;
		}
		ThreadPoolExecutor excutor = new ThreadPoolExecutor(poolSize, poolSize, 10L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		excutor.allowCoreThreadTimeOut(true);
		return excutor;
	}

	private static class BootCallable implements Callable<Boolean> {

		private final Class<?> clz;
		private final Predicate<String> optional;
		private final ParallelBootWatcher watcher;

		public BootCallable(Class<?> clz, Predicate<String> optional, ParallelBootWatcher watcher) {
			this.clz = clz;
			this.optional = optional;
			this.watcher = watcher;
		}

		@Override
		public Boolean call() throws Exception {
			try {
				watcher.handle(clz);
			} catch (Throwable e) {
				String c = clz.getName();

				if ((e instanceof LinkageError || e instanceof ClassNotFoundException)
						&& (c.startsWith("org.yx.") || optional.test(c))) {

					Logs.ioc().debug("{} ignored in {} publish because: [{}]", c, watcher.getClass().getName(), e);
					return Boolean.TRUE;
				}
				Logs.ioc().error(c + " 在 " + watcher.getClass().getName() + "中执行失败", e);
				throw e;
			}
			return Boolean.TRUE;
		}
	}

}
