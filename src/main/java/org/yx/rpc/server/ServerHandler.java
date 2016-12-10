package org.yx.rpc.server;

import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.yx.exception.BizException;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.util.GsonUtil;

public class ServerHandler extends IoHandlerAdapter {
	private List<RequestHandler> handlers;

	public ServerHandler(List<RequestHandler> handlers) {
		super();
		this.handlers = handlers;
	}

	public void addHandler(RequestHandler h) {
		this.handlers.add(h);
	}

	@Override
	public void sessionCreated(IoSession session) {
		Log.get("SYS.51").debug("create session:{}", session.getId());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		if (session.getIdleCount(status) > 10000) {
			Log.get("SYS.52").debug("session:{}, idle:{},will close", session.getId(), session.getIdleCount(status));
			session.close(true);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		Log.get("SYS.53").error("session:" + session.getId() + ",message:" + cause.getMessage(), cause);
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		try {
			for (RequestHandler h : handlers) {
				if (h.accept(message)) {
					Object ret = h.received(message);
					session.write(ret);
					return;
				}
			}
		} catch (Exception e) {
			Response resp = new Response();
			resp.setException(new SoaException(45345, "server handler error", e));
			session.write(GsonUtil.toJson(resp));
			if (!BizException.class.isInstance(e)) {
				Log.printStack(e);
			}
		}

	}
}