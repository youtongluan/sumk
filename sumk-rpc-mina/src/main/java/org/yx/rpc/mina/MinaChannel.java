package org.yx.rpc.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;
import org.yx.annotation.doc.NotNull;
import org.yx.rpc.transport.RpcWriteFuture;
import org.yx.rpc.transport.TransportChannel;

public class MinaChannel implements TransportChannel {
	private final IoSession session;

	private MinaChannel(@NotNull IoSession session) {
		this.session = session;
	}

	public static MinaChannel create(IoSession session) {
		MinaChannel channel = (MinaChannel) session.getAttribute(TransportChannel.class.getName());
		if (channel == null) {
			channel = new MinaChannel(session);
			channel.setAttribute(TransportChannel.class.getName(), channel);
		}
		return channel;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) session.getRemoteAddress();
	}

	@Override
	public boolean isConnected() {
		return session.isConnected();
	}

	@Override
	public void closeNow() {
		session.closeNow();
	}

	@Override
	public void closeOnFlush() {
		session.closeOnFlush();
	}

	@Override
	public Object getAttribute(String key) {
		return session.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		session.setAttribute(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		session.removeAttribute(key);
	}

	@Override
	public String toString() {
		return "mina:" + session;
	}

	@Override
	public RpcWriteFuture write(Object message) {
		return new MinaWriteFuture(session.write(message));
	}

	@Override
	public boolean isClosing() {
		return session.isClosing();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((session == null) ? 0 : session.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MinaChannel other = (MinaChannel) obj;
		if (session == null) {
			if (other.session != null)
				return false;
		} else if (!session.equals(other.session))
			return false;
		return true;
	}

}
