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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.rpc.server.RequestHandler;
import org.yx.rpc.server.ServerHandler;
import org.yx.rpc.transport.TransportServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NettyServer implements TransportServer {

	private final String host;
	private int port;
	private final Supplier<ChannelInitializer<SocketChannel>> channelInitSupplier;
	private ChannelFuture future;

	public NettyServer(String host, int port, List<RequestHandler> handlers) {
		this.port = port;
		this.host = host;
		this.channelInitSupplier = createChannelInitializer(handlers);
	}

	protected Supplier<ChannelInitializer<SocketChannel>> createChannelInitializer(List<RequestHandler> handlers) {
		return new DefaultChannelInitializerSupplier(new ServerHandler(IOC.getBeans(RequestHandler.class)), true);
	}

	protected InetSocketAddress listenAddr(boolean randomPort) {
		if (randomPort) {
			int start = AppInfo.getInt("sumk.rpc.port.start", 10000);
			int end = AppInfo.getInt("sumk.rpc.port.end", 60000);
			port = start + ThreadLocalRandom.current().nextInt(end - start);
		}
		if (host == null || host.trim().length() == 0) {
			return new InetSocketAddress(port);
		}
		return new InetSocketAddress(host, port);
	}

	public synchronized void start() {
		if (future != null) {
			Logs.rpc().info("server已经启动，绑定到{}端口", port);
			return;
		}

		ServerBootstrap b = new ServerBootstrap();
		try {
			EventLoopGroup bossGroup = new NioEventLoopGroup(AppInfo.getInt("sumk.rpc.server.boss.count", 1),
					new DefaultThreadFactory("NettyServerBoss", true));

			EventLoopGroup workerGroup = new NioEventLoopGroup(AppInfo.getInt("sumk.rpc.server.worker.count", 4),
					new DefaultThreadFactory("NettyServerWorker", true));

			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(channelInitSupplier.get());
			NettyKit.configServer(b);
			boolean randomPort = this.port < 1;
			for (int i = 0; i < 50; i++) {
				try {
					InetSocketAddress addr = listenAddr(randomPort);
					ChannelFuture f = b.bind(addr).sync();
					Log.get("sumk.rpc.server").info("rpc(netty) listening on " + addr);
					this.future = f;
					this.port = addr.getPort();
					break;
				} catch (Exception e) {
					if (randomPort) {
						Log.get("sumk.rpc.server").info("{} was occupied,try another port...", this.port);
						continue;
					}
					Log.get("sumk.rpc.server").info("waiting for listening to {}: {}", port, e);
					int time = AppInfo.getInt("sumk.rpc.server.starting.sleep", 5000);
					LockSupport.parkUntil(System.currentTimeMillis() + time);
				}
			}
		} catch (Throwable e) {
			Log.get("sumk.rpc.server").error(e.getLocalizedMessage(), e);
		}
		if (this.future == null) {
			throw new SumkException(38057306, "start netty server failed");
		}
	}

	public int getPort() {
		return port;
	}

	public void stop() throws IOException {
		if (this.future == null) {
			return;
		}
		future.channel().close();
	}
}
