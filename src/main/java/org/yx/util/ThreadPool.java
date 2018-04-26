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
package org.yx.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.yx.main.SumkThreadPool;

public final class ThreadPool {
	private static ExecutorService executor = SumkThreadPool.EXECUTOR;

	public static void execute(Runnable command) {
		executor.execute(command);
	}

	public static <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	public static <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	public static Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

}
