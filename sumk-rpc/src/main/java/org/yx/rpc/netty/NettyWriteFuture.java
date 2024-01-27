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
package org.yx.rpc.netty;

import java.util.Objects;

import org.yx.rpc.transport.RpcWriteFuture;
import org.yx.rpc.transport.RpcWriteListener;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyWriteFuture implements RpcWriteFuture {
	private final ChannelFuture future;

	public NettyWriteFuture(ChannelFuture future) {
		this.future = Objects.requireNonNull(future);
	}

	@Override
	public boolean isWritten() {
		return future.isSuccess();
	}

	@Override
	public Throwable getException() {
		return future.cause();
	}

	@Override
	public void addListener(RpcWriteListener listener) {
		future.addListener(new NettyWriteListener(Objects.requireNonNull(listener), this));
	}

	private static final class NettyWriteListener implements GenericFutureListener<ChannelFuture> {
		private final RpcWriteListener listener;
		private final NettyWriteFuture writeFuture;

		public NettyWriteListener(RpcWriteListener listener, NettyWriteFuture writeFuture) {
			this.listener = listener;
			this.writeFuture = writeFuture;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			listener.afterWrited(writeFuture);
		}

	}
}
