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
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.rpc.RpcSettings;
import org.yx.rpc.client.AbstractTransportClient;
import org.yx.rpc.client.ClientHandler;
import org.yx.rpc.transport.TransportClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NettyClient extends AbstractTransportClient {

	private static Supplier<Bootstrap> connectorSupplier = new BootstrapSupplier();

	public static void setConnectorSupplier(Supplier<Bootstrap> connectorSupplier) {
		NettyClient.connectorSupplier = Objects.requireNonNull(connectorSupplier);
	}

	public static Supplier<Bootstrap> connectorSupplier() {
		return connectorSupplier;
	}

	public NettyClient(Host host) {
		super(host);
	}

	private void connect(Bootstrap connector, long timeout) throws InterruptedException {

		if (channel == null || channel.isClosing()) {
			Logs.rpc().debug("create session for {}", addr);
			ChannelFuture cf = connector.connect(addr.toInetSocketAddress());

			Channel se = null;
			if (cf.await(timeout)) {
				se = cf.channel();
			}
			if (se != null) {
				this.channel = NettyChannel.create(se);
				this.channel.setAttribute(TransportClient.class.getName(), this);
				Log.get("sumk.rpc.client").info("built netty connetion: {}", channel);
				return;
			}
			cf.cancel(true);
		}
	}

	@Override
	protected void connect() throws Exception {
		Bootstrap connector = connectorSupplier.get();
		long timeout = RpcSettings.clientDefaultTimeout() + 2000;
		if (lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
			try {
				if (channel != null && !channel.isClosing()) {
					return;
				}
				connect(connector, timeout);
			} finally {
				lock.unlock();
			}
		}
	}

	public static class BootstrapSupplier implements Supplier<Bootstrap> {

		private volatile Bootstrap connector;
		private EventLoopGroup workerGroup;
		private Supplier<ChannelInitializer<SocketChannel>> channelInitializerSupplier;

		public BootstrapSupplier() {
			this.channelInitializerSupplier = new DefaultChannelInitializerSupplier(new ClientHandler(), false);
			this.workerGroup = new NioEventLoopGroup(AppInfo.getInt("sumk.rpc.client.worker.count", 4),
					new DefaultThreadFactory("NettyClientWorker", true));
		}

		@Override
		public Bootstrap get() {
			Bootstrap con = this.connector;
			if (con != null) {
				return con;
			}
			return this.create();
		}

		private synchronized Bootstrap create() {
			if (this.connector != null) {
				return this.connector;
			}
			Bootstrap bootstrap = new Bootstrap();

			bootstrap.group(workerGroup).channel(NioSocketChannel.class);
			NettyKit.configClient(bootstrap);
			bootstrap.handler(this.channelInitializerSupplier.get());
			this.connector = bootstrap;
			return bootstrap;
		}
	}
}
