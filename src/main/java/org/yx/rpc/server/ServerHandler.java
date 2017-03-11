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
		Log.get("sumk.SYS").debug("create session:{}-{}", session.getServiceAddress(), session.getId());
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
			Log.get("sumk.SYS").debug("session:{}, idle:{},will close", session.getId(), session.getIdleCount(status));
			session.close(true);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		Log.get("sumk.SYS").error("session:" + session.getId() + ",message:" + cause.getMessage(), cause);
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