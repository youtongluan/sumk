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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.yx.common.Host;
import org.yx.log.Logs;
import org.yx.rpc.client.route.HostChecker;
import org.yx.rpc.transport.RpcWriteFuture;
import org.yx.rpc.transport.TransportChannel;
import org.yx.rpc.transport.TransportClient;

public abstract class AbstractTransportClient implements TransportClient {

	protected volatile TransportChannel channel;
	protected final Host addr;
	protected final Lock lock = new ReentrantLock();

	public AbstractTransportClient(Host host) {
		this.addr = Objects.requireNonNull(host);
	}

	private boolean ensureSession() {
		if (channel != null && !channel.isClosing()) {
			return true;
		}
		try {
			connect();
		} catch (Exception e1) {
			Logs.rpc().error(this.addr + " - " + e1.toString(), e1);
		}

		if (channel == null || channel.isClosing()) {
			HostChecker.get().addDownUrl(addr);
			return false;
		}
		return true;
	}

	protected abstract void connect() throws Exception;

	@Override
	public RpcWriteFuture write(Req req) {
		if (!this.ensureSession()) {
			return null;
		}
		return this.channel.write(req);
	}

	@Override
	public void closeIfPossibble() {
		TransportChannel s = null;
		if (lock.tryLock()) {
			try {
				s = this.channel;
				this.channel = null;
			} finally {
				lock.unlock();
			}
		} else {
			Logs.rpc().warn("关闭rpc连接时获取锁失败-{}", this);
			return;
		}
		if (s != null && s.isConnected()) {
			s.closeOnFlush();
		}
	}

	@Override
	public boolean isIdle() {
		TransportChannel s = this.channel;
		return s == null || s.isClosing();
	}

	@Override
	public final Host getRemoteAddr() {
		return this.addr;
	}

	@Override
	public final TransportChannel getChannel() {
		return this.channel;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [addr=" + addr + ", session=" + channel + "]";
	}

}
