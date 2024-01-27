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
package org.yx.rpc.client;

import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

import org.yx.base.context.ActionContext;
import org.yx.base.context.LogContext;
import org.yx.common.Host;
import org.yx.exception.SoaException;
import org.yx.log.Logs;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.client.route.HostChecker;
import org.yx.rpc.log.RpcLog;
import org.yx.rpc.log.RpcLogs;
import org.yx.rpc.transport.RpcWriteFuture;
import org.yx.rpc.transport.RpcWriteListener;

public final class RpcLocker implements RpcWriteListener {

	private final AtomicReference<RpcResult> result = new AtomicReference<>();

	final Req req;
	private Host url;
	final Consumer<RpcCallInfo> callback;
	private final LogContext originLogContext;

	private final AtomicReference<Thread> awaitThread = new AtomicReference<>();

	RpcLocker(Req req, Consumer<RpcCallInfo> callback) {
		this.req = Objects.requireNonNull(req);
		this.callback = callback;
		this.originLogContext = ActionContext.current().logContext();
	}

	public LogContext originLogContext() {
		return this.originLogContext;
	}

	public void url(Host url) {
		this.url = url;
	}

	public Host url() {
		return url;
	}

	public boolean isWaked() {
		return this.result.get() != null;
	}

	public void wakeupAndLog(RpcResult result) {
		this.wakeup0(result, true);
	}

	public void discard(RpcResult result) {
		this.wakeup0(result, false);
	}

	private void wakeup0(RpcResult result, boolean finish) {
		Objects.requireNonNull(result, "result cannot be null");
		if (this.isWaked()) {
			return;
		}
		if (!this.result.compareAndSet(null, result)) {
			return;
		}
		if (!finish) {
			return;
		}

		long receiveTime = System.currentTimeMillis();
		Thread thread = awaitThread.getAndSet(null);
		if (thread != null) {
			LockSupport.unpark(thread);
		}
		RpcLogs.clientLog(new RpcLog(this.url, this.req, this.originLogContext, result, receiveTime));
		if (this.callback != null) {
			ActionContext old = ActionContext.current().clone();
			try {
				ActionContext.store(ActionContext.newContext(this.originLogContext));
				RpcCallInfo info = new RpcCallInfo(this.req.getSn(), this.result.get(), this.url);
				callback.accept(info);
			} catch (Throwable e) {
				Logs.rpc().error(e.getLocalizedMessage(), e);
			} finally {
				ActionContext.store(old);
			}
		}
	}

	@Override
	public void afterWrited(final RpcWriteFuture future) {
		if (future.getException() == null) {
			return;
		}
		Rpc.clientExecutor().execute(() -> {
			if (LockHolder.remove(req.getSn()) == null) {
				return;
			}
			if (url != null) {
				HostChecker.get().addDownUrl(url);
			}
			wakeupAndLog(RpcResult.sendFailed(req, future.getException()));
		});
	}

	public RpcResult awaitForResponse() {
		RpcResult rpcResult = this.result.get();
		if (rpcResult != null) {
			return rpcResult;
		}
		Thread currentThread = Thread.currentThread();
		if (!awaitThread.compareAndSet(null, currentThread)) {
			throw SoaException.create(RpcErrorCode.TIMEOUT, "cannot await twice", new TimeoutException());
		}
		while (result.get() == null) {

			LockSupport.parkUntil(System.currentTimeMillis() + 10000);
		}

		rpcResult = this.result.get();
		if (rpcResult == null) {
			rpcResult = RpcResult.timeout(req);
		}
		return rpcResult;
	}
}