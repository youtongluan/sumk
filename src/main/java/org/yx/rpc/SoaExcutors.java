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
package org.yx.rpc;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import org.yx.main.SumkThreadPool;

public final class SoaExcutors {

	private static ExecutorService serverThreadPool = SumkThreadPool.executor();
	private static ExecutorService clientThreadPool = SumkThreadPool.executor();

	public static ExecutorService getServerThreadPool() {
		return serverThreadPool;
	}

	public static void setServerThreadPool(ExecutorService serverThreadPool) {
		SoaExcutors.serverThreadPool = Objects.requireNonNull(serverThreadPool);
	}

	public static ExecutorService getClientThreadPool() {
		return clientThreadPool;
	}

	public static void setClientThreadPool(ExecutorService clientThreadPool) {
		SoaExcutors.clientThreadPool = Objects.requireNonNull(clientThreadPool);
	}
}
