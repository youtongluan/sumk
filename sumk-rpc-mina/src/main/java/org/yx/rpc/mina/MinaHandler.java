package org.yx.rpc.mina;

import java.util.Objects;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.yx.log.Logs;
import org.yx.rpc.BusinessHandler;

public class MinaHandler implements IoHandler {

	private final BusinessHandler handler;

	public MinaHandler(BusinessHandler handler) {
		this.handler = Objects.requireNonNull(handler);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		Logs.rpc().debug("open session:{}-{}", session.getServiceAddress(), session.getId());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		this.handler.closed(MinaChannel.create(session));
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		long time = System.currentTimeMillis() - session.getLastIoTime();
		Logs.rpc().info("rpc session {} for {}ms,closed by this server", session, time);
		session.closeOnFlush();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		this.handler.exceptionCaught(MinaChannel.create(session), cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		this.handler.received(MinaChannel.create(session), message);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		session.closeNow();
	}

	@Override
	public void event(IoSession arg0, FilterEvent arg1) throws Exception {

	}

}
