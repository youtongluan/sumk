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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketConnector;
import org.yx.common.Host;
import org.yx.log.Logs;
import org.yx.rpc.client.route.HostChecker;

public final class ReqSession {

	protected volatile IoSession session;

	private static Supplier<SocketConnector> connectorSupplier = new SocketConnectorSupplier();
	private final Host addr;
	private final Lock lock = new ReentrantLock();

	public static void setConnectorSupplier(Supplier<SocketConnector> connectorSupplier) {
		ReqSession.connectorSupplier = Objects.requireNonNull(connectorSupplier);
	}

	public static Supplier<SocketConnector> getConnectorSupplier() {
		return connectorSupplier;
	}

	public ReqSession(Host host) {
		this.addr = Objects.requireNonNull(host);
	}

	public SocketConnector getConnector() {
		return connectorSupplier.get();
	}

	private boolean ensureSession() {
		if (session != null && !session.isClosing()) {
			return true;
		}
		try {
			SocketConnector connector = this.getConnector();
			if (lock.tryLock(connector.getConnectTimeoutMillis() + 2000, TimeUnit.MILLISECONDS)) {
				try {
					if (session != null && !session.isClosing()) {
						return true;
					}
					connect(connector);
				} finally {
					lock.unlock();
				}
			}
		} catch (Exception e1) {
			Logs.rpc().error(this.addr + " - " + e1.toString(), e1);
		}

		if (session == null || session.isClosing()) {
			HostChecker.get().addDownUrl(addr);
			return false;
		}
		return true;
	}

	private void connect(SocketConnector connector) throws InterruptedException {

		if (session == null || session.isClosing()) {
			Logs.rpc().debug("create session for {}", addr);
			ConnectFuture cf = connector.connect(addr.toInetSocketAddress());

			cf.await(connector.getConnectTimeoutMillis() + 20);
			IoSession se = cf.getSession();
			if (se != null) {
				this.session = se;
				return;
			}
			cf.cancel();

		}
	}

	public WriteFuture write(Req req) {
		if (!this.ensureSession()) {
			return null;
		}
		return this.session.write(req);
	}

	public void close() {
		IoSession s = this.session;
		if (s != null && s.isConnected()) {
			this.session.closeNow();
		}
	}

	public static void init() {
		connectorSupplier.get();
	}
}
