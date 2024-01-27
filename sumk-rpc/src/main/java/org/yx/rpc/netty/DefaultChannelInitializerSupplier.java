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

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.rpc.BusinessHandler;
import org.yx.rpc.RpcSettings;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class DefaultChannelInitializerSupplier implements Supplier<ChannelInitializer<SocketChannel>> {

	private final ChannelHandler handler;
	private final boolean isServer;

	public DefaultChannelInitializerSupplier(BusinessHandler handler, boolean isServer) {
		this.handler = new NettyHandler(handler);
		this.isServer = isServer;
	}

	@Override
	public ChannelInitializer<SocketChannel> get() {
		return new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {

				ch.pipeline()
						.addLast(new IdleStateHandler(
								isServer ? RpcSettings.maxServerIdleTime() : RpcSettings.maxClientIdleTime(),
								AppInfo.getLong("sumk.rpc.idle.write", 0), 0, TimeUnit.MILLISECONDS))
						.addLast(new NettyEncoder()).addLast(new NettyDecoder()).addLast(handler);
				Logger log = Log.get(isServer ? "sumk.rpc.server" : "sumk.rpc.client");
				if (log.isDebugEnabled()) {
					ByteBufAllocator alloc = ch.alloc();
					if (alloc != null) {
						log.debug("netty channel alloc: {}", alloc);
					}
				}
			}
		};
	}
}
