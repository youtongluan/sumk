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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.rpc.Host;
import org.yx.rpc.SoaExcutors;
import org.yx.rpc.codec.SumkCodecFactory;

public class ReqSession {

	protected volatile IoSession session;

	private NioSocketConnector connector;
	private Host addr;
	private final Lock lock = new ReentrantLock();

	private static final int CONNECT_TIMEOUT = AppInfo.getInt("soa.connect.timeout", 5000);

	private boolean ensureSession() {
		if (session != null && !session.isClosing()) {
			return true;
		}
		try {
			if (lock.tryLock(CONNECT_TIMEOUT + 2000, TimeUnit.MILLISECONDS)) {
				try {
					if (session != null && !session.isClosing()) {
						return true;
					}
					connect();
				} finally {
					lock.unlock();
				}
			}
		} catch (Exception e1) {
			Log.printStack(e1);
		}

		if (session == null || session.isClosing()) {
			return false;
		}
		return true;
	}

	private void connect() throws InterruptedException {
		if (connector == null || connector.isDisposing() || connector.isDisposed()) {
			Log.get("sumk.rpc").debug("create connector for {}", addr);
			connector = new NioSocketConnector(1);
			connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
			connector.setHandler(new ClientHandler());
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(SumkCodecFactory.factory()));
			connector.getFilterChain().addLast("exec", new ExecutorFilter(SoaExcutors.CLINET));
		}

		if (session == null || session.isClosing()) {
			Log.get("sumk.rpc").debug("create session for {}", addr);
			ConnectFuture cf = connector.connect(addr.toInetSocketAddress());

			cf.await(CONNECT_TIMEOUT + 1);
			IoSession se = cf.getSession();
			if (se != null) {
				this.session = se;
				return;
			}
			cf.cancel();

		}
	}

	public ReqSession(Host host) {
		addr = host;
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
			this.session.close(true);
		}
	}
}
