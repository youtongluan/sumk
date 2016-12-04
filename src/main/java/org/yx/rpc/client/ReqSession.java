package org.yx.rpc.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.yx.log.Log;
import org.yx.rpc.codec.SumkCodecFactory;

public class ReqSession {

	protected IoSession session;
	private NioSocketConnector connector;
	private InetSocketAddress addr;

	private void ensureSession() {
		if (session != null && !session.isClosing()) {
			return;
		}
		synchronized (this) {
			if (connector == null || connector.isDisposing() || connector.isDisposed()) {
				Log.get("SYS.RPC").debug("create connector for {}", addr);
				connector = new NioSocketConnector(1);
				connector.setConnectTimeoutMillis(5000);
				connector.setHandler(new ClientHandler());
				connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(SumkCodecFactory.factory()));
			}

			if (session == null || session.isClosing()) {
				Log.get("SYS.RPC").debug("create session for {}", addr);
				ConnectFuture cf = connector.connect(addr);

				cf.awaitUninterruptibly(connector.getConnectTimeoutMillis() + 1);
				IoSession se = cf.getSession();
				if (se != null) {
					this.session = se;
					return;
				}
				cf.cancel();
				throw new RuntimeException("创建连接失败");
			}
		}
	}

	public ReqSession(String ip, int port) {
		addr = new InetSocketAddress(ip, port);
	}

	public WriteFuture write(Req req) {
		this.ensureSession();
		return this.session.write(req);
	}

	public void close() {
		this.session.close(true);
	}
}
