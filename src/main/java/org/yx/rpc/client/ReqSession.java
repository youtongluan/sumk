/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.yx.exception.ConnectionException;
import org.yx.log.Log;
import org.yx.rpc.Host;
import org.yx.rpc.codec.SumkCodecFactory;

public class ReqSession {

	protected volatile IoSession session;

	private NioSocketConnector connector;
	private Host addr;
	private Lock lock = new ReentrantLock();

	private static final int CONNECT_TIMEOUT = 5000;

	private void ensureSession() {
		if (session != null && !session.isClosing()) {
			return;
		}
		boolean locked = false;
		try {
			locked = lock.tryLock(CONNECT_TIMEOUT + 10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (locked) {
			try {
				connect();
			} finally {
				lock.unlock();
			}
		}

		if (session == null || session.isClosing()) {
			throw new ConnectionException(6454345, "error in create channel", addr);
		}
	}

	private void connect() {
		if (connector == null || connector.isDisposing() || connector.isDisposed()) {
			Log.get("sumk.rpc").debug("create connector for {}", addr);
			connector = new NioSocketConnector(1);
			connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
			connector.setHandler(new ClientHandler());
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(SumkCodecFactory.factory()));
		}

		if (session == null || session.isClosing()) {
			Log.get("sumk.rpc").debug("create session for {}", addr);
			ConnectFuture cf = connector.connect(addr.toInetSocketAddress());

			cf.awaitUninterruptibly(CONNECT_TIMEOUT + 1);
			try {
				IoSession se = cf.getSession();
				if (se != null) {
					this.session = se;
					return;
				}
				cf.cancel();
			} catch (Exception e) {
				throw new ConnectionException(7311234, "error in create channel.cause by " + e.getMessage(), addr);
			}

		}
	}

	public ReqSession(Host host) {
		addr = host;
	}

	public WriteFuture write(Req req) {
		this.ensureSession();
		return this.session.write(req);
	}

	public void close() {
		this.session.close(true);
	}
}
