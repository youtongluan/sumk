package org.yx.rpc.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.server.RequestHandler;
import org.yx.rpc.server.ServerHandler;
import org.yx.rpc.transport.TransportServer;

public class MinaServer implements TransportServer {

	private final String host;
	private int port;
	private IoHandler handler;
	private int acceptors;
	private SocketAcceptor acceptor;

	public void setAcceptors(int acceptors) {
		this.acceptors = acceptors;
	}

	public MinaServer(String host, int port) {
		this.port = port;
		this.host = host;
		this.handler = createServerHandler();
	}

	protected IoHandler createServerHandler() {
		return new MinaHandler(new ServerHandler(IOC.getBeans(RequestHandler.class)));
	}

	protected InetSocketAddress listenAddr(boolean randomPort) {
		if (randomPort) {// 1万到6万之间
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
		if (acceptor != null) {
			return;
		}
		try {
			acceptor = acceptors > 0 ? new NioSocketAcceptor(acceptors) : new NioSocketAcceptor();
			acceptor.setReuseAddress(AppInfo.getBoolean("sumk.rpc.port.reuse", false));
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

			chain.addLast("codec", new ProtocolCodecFilter(new MinaProtocolEncoder(), new MinaProtocolDecoder()));

			acceptor.setHandler(handler);
			MinaKit.config(acceptor.getSessionConfig(), true);
			boolean randomPort = this.port < 1;
			for (int i = 0; i < 50; i++) {
				try {
					InetSocketAddress addr = listenAddr(randomPort);
					acceptor.bind(addr);
					Log.get("sumk.rpc.server").info("rpc(mina) listening on " + addr);
					break;
				} catch (IOException e) {
					if (randomPort) {
						Log.get("sumk.rpc.server").info("{} was occupied,try another port...", this.port);
						continue;
					}
					Log.get("sumk.rpc.server").debug("waiting for listening to {}.{}", port, e.getMessage());
					int time = AppInfo.getInt("sumk.rpc.server.starting.sleep", 5000);
					LockSupport.parkUntil(System.currentTimeMillis() + time);
				}
			}

		} catch (Exception e) {
			Log.get("sumk.rpc.server").debug(e.getLocalizedMessage(), e);
			acceptor = null;
			throw new SumkException(38057306, "start mina server failed", e);
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
