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
package org.yx.rpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.SoaExcutors;
import org.yx.rpc.codec.SumkCodecFactory;

public class MinaServer implements Runnable {

	public static final String SOA_SESSION_IDLE = "sumk.rpc.session.idle";
	private final String host;
	private int port;
	private IoHandler handler;
	private int acceptors;
	private SocketAcceptor acceptor;

	public void setAcceptors(int acceptors) {
		this.acceptors = acceptors;
	}

	public MinaServer(String host, int port, List<RequestHandler> handlers) {
		this.port = port;
		this.host = host;
		this.handler = new ServerHandler(handlers);
	}

	public MinaServer(int port, List<RequestHandler> handlers) {
		this.host = null;
		this.port = port;
		this.handler = new ServerHandler(handlers);
	}

	protected InetSocketAddress listenAddr(boolean randomPort) {
		if (randomPort) {
			port = 5000 + (new Random()).nextInt(5000);
		}
		if (host == null || host.trim().length() == 0) {
			return new InetSocketAddress(port);
		}
		return new InetSocketAddress(host, port);

	}

	public synchronized void run() {
		if (acceptor != null) {
			return;
		}
		try {
			acceptor = acceptors > 0 ? new NioSocketAcceptor(acceptors) : new NioSocketAcceptor();
			acceptor.setReuseAddress(AppInfo.getBoolean("sumk.rpc.port.reuse", false));
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

			chain.addLast("codec", new ProtocolCodecFilter(IOC.get(SumkCodecFactory.class)));

			chain.addLast("threadpool", new ExecutorFilter(SoaExcutors.getServerThreadPool()));

			acceptor.setHandler(handler);

			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, AppInfo.getInt(SOA_SESSION_IDLE, 600));
			if (SocketSessionConfig.class.isInstance(acceptor.getSessionConfig())) {
				SocketSessionConfig conf = (SocketSessionConfig) acceptor.getSessionConfig();
				conf.setKeepAlive(true);
				conf.setReceiveBufferSize(100);
				conf.setSendBufferSize(8192);

			}
			boolean randomPort = this.port < 1;
			for (int i = 0; i < 50; i++) {
				try {
					InetSocketAddress addr = listenAddr(randomPort);
					acceptor.bind(addr);
					Log.get("sumk.rpc").info("rpc listening on " + addr);
					break;
				} catch (IOException e) {
					if (randomPort) {
						continue;
					}
					Log.get("sumk.rpc").debug("waiting for listening to {}.{}", port, e.getMessage());
					int time = AppInfo.getInt("sumk.rpc.server.starting.sleep", 5000);
					Thread.sleep(time);
				}
			}

		} catch (Exception e) {
			Log.get("sumk.rpc").debug(e.getMessage(), e);
			acceptor = null;
			SumkException.throwException(38057306, "start mina server failed", e);
		}

	}

	public int getPort() {
		return port;
	}

	public void stop() throws IOException {
		if (this.acceptor == null) {
			return;
		}

		this.acceptor.dispose(false);
		this.acceptor = null;
	}
}
