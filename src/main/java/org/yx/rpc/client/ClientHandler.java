package org.yx.rpc.client;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.yx.log.Log;
import org.yx.rpc.server.Response;
import org.yx.util.GsonUtil;

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
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		Log.get("SYS.4").error(cause);

	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String msg=(String)message;
		Log.get("SYS.5").trace(msg);
		String[] msgs=msg.split("\r",-1);
		Response resp=GsonUtil.fromJson(msgs[0], Response.class);
		RequestLocker.unLockAndSetResult(resp);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		

	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		
		
	}

}
