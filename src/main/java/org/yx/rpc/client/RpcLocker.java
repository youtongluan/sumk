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

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.Host;
import org.yx.rpc.RpcCode;
import org.yx.rpc.SoaExcutors;
import org.yx.rpc.client.route.HostChecker;
import org.yx.util.Assert;

public final class RpcLocker implements IoFutureListener<WriteFuture> {

	private final AtomicReference<RpcResult> result = new AtomicReference<>();

	final Req req;
	private Host url;
	final Consumer<RpcResult> callback;

	private final AtomicReference<Thread> awaitThread = new AtomicReference<>();

	RpcLocker(Req req, Consumer<RpcResult> callback) {
		this.req = req;
		this.callback = callback;
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

	public void wakeup(RpcResult result) {
		Assert.notNull(result, "result cannot be null");
		if (this.isWaked()) {
			return;
		}
		if (!this.result.compareAndSet(null, result)) {
			return;
		}
		Thread thread = awaitThread.getAndSet(null);
		if (thread != null) {
			LockSupport.unpark(thread);
		}
		if (this.callback != null) {
			try {
				callback.accept(result);
			} catch (Throwable e) {
				Log.printStack(e);
			}
		}
	}

	@Override
	public void operationComplete(final WriteFuture future) {
		if (future.getException() == null) {
			return;
		}
		SoaExcutors.CLINET.execute(() -> {
			if (LockHolder.remove(req.getSn()) == null) {
				return;
			}
			if (url != null) {
				HostChecker.get().addDownUrl(url);
			}
			wakeup(RpcResult.sendFailed(req, future.getException()));
		});
	}

	public RpcResult awaitForResponse() {
		RpcResult rpcResult = this.result.get();
		if (rpcResult != null) {
			return rpcResult;
		}
		Thread currentThread = Thread.currentThread();
		if (!awaitThread.compareAndSet(null, currentThread)) {
			throw new SoaException(RpcCode.TIMEOUT, "cannot await twice", new TimeoutException());
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