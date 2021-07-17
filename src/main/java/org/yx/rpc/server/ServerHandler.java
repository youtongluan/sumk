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
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.InnerRpcUtil;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.codec.ProtocolDeserializer;
import org.yx.rpc.codec.Request;
import org.yx.rpc.log.RpcLogs;

public class ServerHandler implements IoHandler {

	private final ProtocolDeserializer deserializer;

	private final Logger log;
	private final RequestHandler[] handlers;

	public ServerHandler(List<RequestHandler> handlers) {
		this.log = Log.get("sumk.rpc.server");
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
		if (time > AppInfo.getLong("sumk.rpc.server.idle", 1000L * 60 * 10)) {
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
		Response resp = new Response();
		Request req = null;
		try {
			Object obj = this.deserializer.deserialize(message);
			if (obj == null) {
				return;
			}
			message = null;
			if (obj instanceof Request) {
				req = (Request) obj;
				resp.setClientAcceptedProtocol(req.getAcceptResponseTypes());
				InnerRpcUtil.rpcContext(req, req.isTest());
				for (RequestHandler h : handlers) {
					if (h.handle(req, resp)) {
						resp.serviceInvokeMilTime(System.currentTimeMillis() - req.getStartInServer());
						session.write(resp);
						return;
					}
				}
			}
		} catch (Throwable e) {
			Log.get("sumk.rpc.server").error(e.toString(), e);
			long begin = 0;
			if (req != null) {
				begin = req.getStartInServer();
				resp.sn(req.getSn());
			}
			resp.serviceInvokeMilTime(System.currentTimeMillis() - begin);
			resp.exception(new SoaException(e, RpcErrorCode.SERVER_UNKNOW, "server handler error"));
			session.write(resp);
		} finally {
			RpcLogs.serverLog(req, resp);
			ActionContext.remove();
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