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

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.yx.bean.IOC;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.RpcGson;
import org.yx.rpc.codec.ProtocolDeserializer;
import org.yx.rpc.codec.Request;
import org.yx.util.S;

public class ServerHandler implements IoHandler {

	private final ProtocolDeserializer deserializer;

	private Logger log = Log.get("sumk.rpc.server");
	private final RequestHandler[] handlers;

	public ServerHandler(List<RequestHandler> handlers) {
		this.handlers = handlers.toArray(new RequestHandler[0]);
		deserializer = IOC.get(ProtocolDeserializer.class);
	}

	@Override
	public void sessionCreated(IoSession session) {
		log.debug("create session:{}-{}", session.getServiceAddress(), session.getId());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		long time = System.currentTimeMillis() - session.getLastIoTime();
		if (time > AppInfo.getLong(MinaServer.SOA_SESSION_IDLE, 60) * 1000) {
			log.info("rpc session {} {} for {}ms,closed by this server", session.getId(), status,
					session.getLastIoTime(), time);
			session.closeOnFlush();
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
				log.debug("session:{},message:{}", session.getId(), cause.getMessage());
				return;
			}
		}
		log.error(session + ",message:" + cause.getMessage(), cause);

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		try {
			Object obj = this.deserializer.deserialize(message);
			if (obj == null) {
				return;
			}
			if (Request.class.isInstance(obj)) {
				final Request req = (Request) obj;
				ActionContext.rpcContext(req, ActionContext.get().isTest());
				for (RequestHandler h : handlers) {
					Response ret = h.handle(req);
					if (ret != null) {
						if (log.isTraceEnabled()) {
							log.trace("req:{}\nresp:{}", S.json.toJson(obj), S.json.toJson(ret));
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
				Log.printStack("sumk.error", e);
			}
		}

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		session.closeNow();
	}

	@Override
	public void event(IoSession session, FilterEvent event) throws Exception {

	}
}