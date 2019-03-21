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

import org.yx.bean.IOC;
import org.yx.exception.SoaException;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.codec.Request;

public class LocalRequestHandler {
	public static final LocalRequestHandler inst = new LocalRequestHandler();
	private List<RequestHandler> handlers;

	public List<RequestHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<RequestHandler> handlers) {
		this.handlers = handlers;
	}

	private LocalRequestHandler() {
		handlers = IOC.getBeans(RequestHandler.class);
	}

	public Response handler(Request request, RpcActionNode action) {
		request.protocol(request.paramProtocol());
		for (RequestHandler h : this.handlers) {
			Response resp = h.handle(request);
			if (resp != null) {
				return resp;
			}
		}
		throw new SoaException(777, "没有合适的handler", "no accepted handler");
	}
}
