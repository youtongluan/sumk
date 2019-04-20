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
package org.yx.rpc.server;

import java.io.IOException;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.yx.common.ThreadContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.RpcGson;
import org.yx.rpc.codec.Request;
import org.yx.util.JsonUtil;

public class ServerHandler extends IoHandlerAdapter {
	private Logger log = Log.get("sumk.rpc.server");
	private RequestHandler[] handlers;

	public ServerHandler(List<RequestHandler> handlers) {
		this.handlers = handlers.toArray(new RequestHandler[0]);
	}

	@Override
	public void sessionCreated(IoSession session) {
		Log.get("sumk.rpc.session").debug("create session:{}-{}", session.getServiceAddress(), session.getId());
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
			Log.get("sumk.rpc.session").debug("session:{}, idle:{},will close", session.getId(),
					session.getIdleCount(status));
			session.closeNow();
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.closeOnFlush();
		if (cause == null) {
			return;
		}
		if (cause.getClass() == IOException.class && AppInfo.getBoolean("rpc.session.print_simple_error", true)) {
			String msg = cause.getMessage();
			if (msg != null && (msg.contains("连接") || msg.contains("connection"))) {
				Log.get("sumk.rpc.session").debug("session:{},message:{}", session.getId(), cause.getMessage());
				return;
			}
		}
		Log.get("sumk.rpc.session").error("session:" + session.getId() + ",message:" + cause.getMessage(), cause);

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		try {
			if (Request.class.isInstance(message)) {
				final Request req = (Request) message;
				ThreadContext context = ThreadContext.rpcContext(req.getApi(), req.getRootSn(), req.getSn(),
						ThreadContext.get().isTest());
				context.userId(req.getUserId());
				context.setAttachments(req.getAttachments());

				for (RequestHandler h : handlers) {
					Response ret = h.handle(req);
					if (ret != null) {
						if (log.isTraceEnabled()) {
							log.trace("req:{}\nresp:{}", JsonUtil.toJson(message), JsonUtil.toJson(ret));
						}
						session.write(ret);
						return;
					}
				}
			}
		} catch (Exception e) {
			Response resp = new Response();
			resp.exception(new SoaException(RpcErrorCode.SERVER_UNKNOW, "server handler error", e));
			session.write(RpcGson.toJson(resp));
			if (!BizException.class.isInstance(e)) {
				Log.printStack(e);
			}
		}

	}
}