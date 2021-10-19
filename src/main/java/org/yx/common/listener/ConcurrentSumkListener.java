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
package org.yx.common.listener;

import java.util.List;
import java.util.concurrent.Executor;

import org.yx.common.context.ActionContext;
import org.yx.util.S;

public abstract class ConcurrentSumkListener implements SumkListener {

	private final Executor executor;

	public ConcurrentSumkListener() {
		Executor ex = this.createExecutor();
		this.executor = ex == null ? S.executor() : ex;
	}

	@Override
	public void listenBatch(List<?> events) throws Exception {
		executor.execute(ActionContext.wrapExecutable(() -> this.asyncListenBatch(events)));
	}

	@Override
	public void listen(Object event) throws Exception {
		executor.execute(ActionContext.wrapExecutable(() -> this.asyncListen(event)));
	}

	public Executor executor() {
		return this.executor;
	}

	/**
	 * 创建线程池，只在初始化的时候调用一次。 如果想要单线程排队执行，使用Executors.newSingleThreadExecutor()就可以
	 * 
	 * @return 线程池，如果返回null就使用系统默认的线程池
	 */
	protected Executor createExecutor() {
		return null;
	}

	protected void asyncListenBatch(List<?> events) throws Exception {
		for (Object event : events) {
			this.listen(event);
		}
	}

	protected abstract void asyncListen(Object event) throws Exception;

}
