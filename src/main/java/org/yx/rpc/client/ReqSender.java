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
package org.yx.rpc.client;

import org.apache.mina.core.future.WriteFuture;
import org.yx.exception.ConnectionException;
import org.yx.exception.SoaException;
import org.yx.rpc.Host;
import org.yx.rpc.client.route.Routes;
import org.yx.rpc.client.route.WeightedRoute;

class ReqSender {

	/**
	 * 同步发送
	 * 
	 * @param req
	 * @param timeout
	 * @return
	 * @throws Throwable
	 */
	public static ReqResp send(Req req, long timeout) throws Throwable {
		String method = req.getMethod();
		WeightedRoute route = Routes.getRoute(method);
		if (route == null) {
			SoaException.throwException(2353454, "can not find route for " + method, null);
		}
		Host url = route.getUrl();
		if (url == null) {
			SoaException.throwException(345234, "route for " + method + " are all disabled", null);
		}
		ReqSession session = ReqSessionHolder.getSession(url);
		RespFuture future = RequestLocker.register(req);
		WriteFuture f = session.write(req);

		if (f.getException() != null) {
			throw new ConnectionException(345, f.getException().getMessage(), route.getUrl());
		}
		return future.getResponse(timeout);
	}

	public static RespFuture sendAsync(Req req) {
		String method = req.getMethod();
		WeightedRoute route = Routes.getRoute(method);
		if (route == null) {
			SoaException.throwException(2353454, "can not find route for " + method, null);
		}
		ReqSession session = ReqSessionHolder.getSession(route.getUrl());
		RespFuture future = RequestLocker.register(req);
		WriteFuture f = session.write(req);

		if (f.getException() != null) {
			throw new ConnectionException(345, f.getException().getMessage(), route.getUrl());
		}
		return future;
	}
}
