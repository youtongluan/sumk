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
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.main.StartContext;
import org.yx.rpc.BusinessHandler;
import org.yx.rpc.InnerRpcUtil;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.codec.Request;
import org.yx.rpc.log.RpcLogs;
import org.yx.rpc.transport.TransportChannel;

public class ServerHandler implements BusinessHandler {

	private final Logger log;
	private final RequestHandler[] handlers;
	private final Executor executor;

	public ServerHandler(List<RequestHandler> handlers) {
		this.log = Log.get("sumk.rpc.server");
		this.handlers = handlers.toArray(new RequestHandler[handlers.size()]);
		this.executor = StartContext.inst().getExecutor("sumk.rpc.server.executor");
	}

	@Override
	public void received(TransportChannel session, Object message) {
		this.executor.execute(() -> {
			Response resp = new Response();
			Request req = null;
			try {
				if (message == null) {
					return;
				}
				if (message instanceof Request) {
					req = (Request) message;
					InnerRpcUtil.rpcContext(req, req.isTest());
					for (RequestHandler h : handlers) {
						if (h.handle(req, resp)) {
							resp.serviceInvokeMilTime(System.currentTimeMillis() - req.getStartInServer());
							session.write(resp);
							return;
						}
					}
				}
				Log.get("sumk.rpc.server").warn("unkown message type:{}", message.getClass().getName());
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
		});
	}

	@Override
	public void exceptionCaught(TransportChannel session, Throwable cause) {
		session.closeOnFlush();
		if (cause == null) {
			return;
		}
		if (cause.getClass() == IOException.class && AppInfo.getBoolean("rpc.session.print_simple_error", true)) {
			String msg = cause.getMessage();
			if (msg != null && (msg.contains("连接") || msg.contains("connection"))) {
				log.debug("session:{},message:{}", session, cause.getMessage());
				return;
			}
		}
		log.error(session + ",message:" + cause.getMessage(), cause);
	}

	@Override
	public void closed(TransportChannel session) {
		session.closeNow();

	}
}