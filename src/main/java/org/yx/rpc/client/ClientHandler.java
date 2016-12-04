package org.yx.rpc.client;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.server.Response;

public class ClientHandler implements IoHandler {

	@Override
	public void sessionCreated(IoSession session) throws Exception {

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		Log.printStack(cause);

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message == null) {
			return;
		}
		if (Response.class.isInstance(message)) {
			Response resp = (Response) message;
			RequestLocker.unLockAndSetResult(resp);
			return;
		}
		SumkException.throwException(458223, message.getClass().getName() + " has not deserialized");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void inputClosed(IoSession session) throws Exception {

	}

}
