package org.yx.rpc.mina;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.rpc.client.AbstractTransportClient;
import org.yx.rpc.client.ClientHandler;
import org.yx.rpc.transport.TransportClient;

public final class MinaClient extends AbstractTransportClient {

	private static Supplier<SocketConnector> connectorSupplier = new SocketConnectorSupplier();

	public static void setConnectorSupplier(Supplier<SocketConnector> connectorSupplier) {
		MinaClient.connectorSupplier = Objects.requireNonNull(connectorSupplier);
	}

	public static Supplier<SocketConnector> connectorSupplier() {
		return connectorSupplier;
	}

	public MinaClient(Host host) {
		super(host);
	}

	private void connect(SocketConnector connector) throws InterruptedException {

		if (channel == null || channel.isClosing()) {
			Logs.rpc().debug("create session for {}", addr);
			ConnectFuture cf = connector.connect(addr.toInetSocketAddress());

			cf.await(connector.getConnectTimeoutMillis() + 20);
			IoSession se = cf.getSession();
			if (se != null) {
				this.channel = MinaChannel.create(se);
				this.channel.setAttribute(TransportClient.class.getName(), this);
				return;
			}
			cf.cancel();
		}
	}

	@Override
	protected void connect() throws Exception {
		SocketConnector connector = connectorSupplier.get();
		if (lock.tryLock(connector.getConnectTimeoutMillis() + 2000, TimeUnit.MILLISECONDS)) {
			try {
				if (channel != null && !channel.isClosing()) {
					return;
				}
				connect(connector);
			} finally {
				lock.unlock();
			}
		}
	}

	public static class SocketConnectorSupplier implements Supplier<SocketConnector> {

		private volatile SocketConnector connector;

		@Override
		public SocketConnector get() {
			SocketConnector con = this.connector;
			if (con != null && !con.isDisposing() && !con.isDisposed()) {
				return con;
			}
			return this.create();
		}

		private synchronized SocketConnector create() {
			if (connector != null && !connector.isDisposing() && !connector.isDisposed()) {
				return connector;
			}
			try {
				NioSocketConnector con = new NioSocketConnector(
						AppInfo.getInt("sumk.rpc.client.poolsize", Runtime.getRuntime().availableProcessors() + 1));
				con.setConnectTimeoutMillis(AppInfo.getInt("sumk.rpc.connect.timeout", 5000));
				MinaKit.config(con.getSessionConfig(), false);
				con.setHandler(createClientHandler());
				con.getFilterChain().addLast("codec",
						new ProtocolCodecFilter(new MinaProtocolEncoder(), new MinaProtocolDecoder()));
				this.connector = con;
				return con;
			} catch (Exception e) {
				Logs.rpc().error(e.getMessage(), e);
				throw new SumkException(5423654, "create connector error", e);
			}
		}

		protected IoHandler createClientHandler() {
			return new MinaHandler(new ClientHandler());
		}
	}

}
