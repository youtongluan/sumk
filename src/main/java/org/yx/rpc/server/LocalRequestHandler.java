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

import java.util.List;
import java.util.Objects;

import org.yx.bean.IOC;
import org.yx.exception.SoaException;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.codec.Request;

public class LocalRequestHandler {
	public static final LocalRequestHandler inst = new LocalRequestHandler();
	private List<RequestHandler> handlers;

	public List<RequestHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<RequestHandler> handlers) {
		this.handlers = Objects.requireNonNull(handlers);
	}

	private LocalRequestHandler() {
		handlers = IOC.getBeans(RequestHandler.class);
	}

	public Response handler(Request request, RpcActionNode action) {
		Response resp = new Response();
		try {
			for (RequestHandler h : this.handlers) {
				if (h.handle(request, resp)) {
					resp.serviceInvokeMilTime(System.currentTimeMillis() - request.getStartInServer());
					return resp;
				}
			}
		} catch (Throwable e) {
			resp.exception(new SoaException(e, RpcErrorCode.SERVER_UNKNOW, "server handler error"));
		}
		long begin = request.getStartInServer();
		resp.serviceInvokeMilTime(System.currentTimeMillis() - begin);
		if (resp.exception() == null) {
			resp.exception(new SoaException(777, "没有合适的handler", "no accepted handler"));
		}
		return resp;
	}
}
