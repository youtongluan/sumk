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

import java.util.Map;
import java.util.function.Consumer;

import org.apache.mina.core.future.WriteFuture;
import org.yx.conf.AppInfo;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.Host;
import org.yx.rpc.RpcCode;
import org.yx.rpc.client.route.Routes;
import org.yx.rpc.client.route.RpcRoute;
import org.yx.util.Assert;
import org.yx.util.GsonUtil;

public final class Sender {

	private static enum ParamType {
		JSONARRAY, JSON
	}

	private final String api;
	private Object params;
	private ParamType paramType;
	private int totalTimeout;

	private long totalStart;

	private Consumer<RpcResult> callback;

	Sender(String api) {
		this.api = api;
	}

	public Sender totalTimeout(int timeout) {
		this.totalTimeout = timeout;
		return this;
	}

	public Sender callback(Consumer<RpcResult> callback) {
		this.callback = callback;
		return this;
	}

	public Sender paramInArray(Object... args) {
		String[] params = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			params[i] = GsonUtil.toJson(args[i]);
		}
		this.params = params;
		this.paramType = ParamType.JSONARRAY;
		return this;
	}

	public Sender paramInJson(String json) {
		this.params = json;
		this.paramType = ParamType.JSON;
		return this;
	}

	public Sender callInMap(Map<String, ?> map) {
		this.params = GsonUtil.toJson(map);
		this.paramType = ParamType.JSON;
		return this;
	}

	/**
	 * 本方法调用之后，不允许再调用本对象的任何方法<BR>
	 * 
	 * @return 用无论是否成功，都会返回future。如果失败的话，异常包含在future中。<BR>
	 *         通信异常是SoaException；如果是业务类异常，则是BizException
	 */
	public RpcFuture send() {
		Assert.notEmpty(api, "api cannot be empty");
		Assert.notNull(this.paramType, "param have not been set");
		this.totalStart = System.currentTimeMillis();
		Req req = Rpc.createReq(this.api);
		if (this.paramType == ParamType.JSONARRAY) {
			req.setParamArray((String[]) this.params);
		} else {
			req.setJsonedParam((String) this.params);
		}
		if (this.totalTimeout < 1) {
			this.totalTimeout = AppInfo.getInt("soa.timeout", 30000);
		}
		RpcFuture f = sendAsync(req, this.totalStart + this.totalTimeout);
		if (f.getClass() == ErrorRpcFuture.class) {
			ErrorRpcFuture errorFuture = ErrorRpcFuture.class.cast(f);
			errorFuture.locker.wakeup(errorFuture.rpcResult());
		}
		return f;
	}

	private RpcFuture sendAsync(Req req, long endTime) {
		String api = req.getApi();
		RpcRoute route = Routes.getRoute(api);
		final RpcLocker locker = new RpcLocker(req, callback);
		if (route == null) {
			SoaException ex = new SoaException(RpcCode.NO_ROUTE, "can not find route for " + api, (String) null);
			return new ErrorRpcFuture(ex, locker);
		}
		Host url = route.getUrl();
		if (url == null) {
			SoaException ex = new SoaException(RpcCode.NO_NODE_AVAILABLE, "route for " + api + " are all disabled",
					(String) null);
			return new ErrorRpcFuture(ex, locker);
		}
		locker.url(url);
		WriteFuture f = null;
		try {
			ReqSession session = ReqSessionHolder.getSession(url);
			LockHolder.register(locker, endTime);
			f = session.write(req);
		} catch (Exception e) {
			Log.printStack("sumk.SOA", e);
		}
		if (f == null) {
			SoaException ex = new SoaException(RpcCode.SEND_FAILED, url + " can not connect", (String) null);
			return new ErrorRpcFuture(ex, locker);
		}
		f.addListener(locker);
		return new RpcFutureImpl(locker);
	}

}
