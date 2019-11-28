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
package org.yx.rpc.client;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.apache.mina.core.future.WriteFuture;
import org.yx.common.Host;
import org.yx.common.SumkLogs;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.exception.SoaException;
import org.yx.rpc.RpcActionHolder;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.RpcGson;
import org.yx.rpc.client.route.HostChecker;
import org.yx.rpc.client.route.Routes;
import org.yx.rpc.client.route.RpcRoute;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.LocalRequestHandler;
import org.yx.rpc.server.Response;
import org.yx.util.Asserts;
import org.yx.util.S;

import com.google.gson.JsonElement;

public final class Client {

	private static enum ParamType {
		JSONARRAY, JSON
	}

	private static final Host LOCAL = Host.create("local", 0);
	private final String api;
	private Object params;
	private ParamType paramType;
	private int totalTimeout;

	private long totalStart;

	private Host[] directUrls;

	private boolean backup;
	private static AtomicInteger counter = new AtomicInteger();
	private Consumer<RpcResult> callback;

	Client(String api) {
		this.api = api;
	}

	public Client directUrls(Host... urls) {
		this.directUrls = urls;
		return this;
	}

	public Client backup(boolean backup) {
		this.backup = backup;
		return this;
	}

	public Client timeout(int timeout) {
		this.totalTimeout = timeout;
		return this;
	}

	public Client callback(Consumer<RpcResult> callback) {
		this.callback = callback;
		return this;
	}

	public Client paramInArray(Object... args) {
		String[] params = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			params[i] = RpcGson.toJson(args[i]);
		}
		this.params = params;
		this.paramType = ParamType.JSONARRAY;
		return this;
	}

	public Client paramInJson(String json) {
		this.params = json;
		this.paramType = ParamType.JSON;
		return this;
	}

	public Client paramInMap(Map<String, ?> map) {
		this.params = S.json.toJson(map);
		this.paramType = ParamType.JSON;
		return this;
	}

	/**
	 * 本方法调用之后，不允许再调用本对象的任何方法<BR>
	 * 
	 * @return 用无论是否成功，都会返回future。如果失败的话，异常包含在future中。<BR>
	 *         通信异常是SoaException；如果是业务类异常，则是BizException
	 */
	public RpcFuture execute() {
		Asserts.notEmpty(api, "api cannot be empty");
		Objects.requireNonNull(this.paramType, "param have not been set");
		this.totalStart = System.currentTimeMillis();
		Req req = Rpc.req(this.api);
		if (this.paramType == ParamType.JSONARRAY) {
			req.setParamArray((String[]) this.params);
		} else {
			req.setJsonedParam((String) this.params);
		}
		if (this.totalTimeout < 1) {
			this.totalTimeout = AppInfo.getInt("sumk.rpc.timeout", 30000);
		}
		RpcFuture f = sendAsync(req, this.totalStart + this.totalTimeout);
		if (f.getClass() == ErrorRpcFuture.class) {
			ErrorRpcFuture errorFuture = ErrorRpcFuture.class.cast(f);
			RpcLocker locker = errorFuture.locker;
			LockHolder.remove(locker.req.getSn());
			locker.wakeup(errorFuture.rpcResult());
		}
		return f;
	}

	private Host useDirectUrl() {
		int index = counter.incrementAndGet();
		if (index < 0) {
			counter.set((int) (System.nanoTime() & 0xff));
			index = counter.incrementAndGet();
		}
		for (int i = 0; i < this.directUrls.length; i++) {
			index %= directUrls.length;
			Host url = this.directUrls[index];
			if (!HostChecker.get().isDowned(url)) {
				return url;
			}
		}
		return null;
	}

	private RpcFuture sendAsync(Req req, long endTime) {
		final RpcLocker locker = new RpcLocker(req, callback);
		Host url = null;
		if (this.directUrls != null && this.directUrls.length > 0) {
			url = useDirectUrl();
			if (url == null && !this.backup) {
				SoaException ex = new SoaException(RpcErrorCode.NO_NODE_AVAILABLE,
						"all directUrls is disabled:" + Arrays.toString(this.directUrls), (String) null);
				return new ErrorRpcFuture(ex, locker);
			}
		}
		if (url == null) {

			RpcRoute route = Routes.getRoute(api);
			RpcFuture future = this.tryLocalHandler(req, locker, route);
			if (future != null) {
				return future;
			}

			if (route == null) {
				SoaException ex = new SoaException(RpcErrorCode.NO_ROUTE, "can not find route for " + api,
						(String) null);
				return new ErrorRpcFuture(ex, locker);
			}
			url = route.getUrl();
		}
		if (url == null) {
			SoaException ex = new SoaException(RpcErrorCode.NO_NODE_AVAILABLE, "route for " + api + " are all disabled",
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
			SumkLogs.RPC_LOG.error(e.toString(), e);
		}
		if (f == null) {
			SoaException ex = new SoaException(RpcErrorCode.SEND_FAILED, url + " can not connect", (String) null);
			return new ErrorRpcFuture(ex, locker);
		}
		f.addListener(locker);
		return new RpcFutureImpl(locker);
	}

	private RpcFuture tryLocalHandler(Req req, RpcLocker locker, RpcRoute route) {
		RpcActionNode node = RpcActionHolder.getActionNode(api);
		if (node == null) {
			return null;
		}

		if (AppInfo.getBoolean("sumk.rpc.localroute.disable", false) && route != null) {
			return null;
		}

		JsonElement json = RpcGson.getGson().toJsonTree(req);
		Request request = RpcGson.getGson().fromJson(json, Request.class);

		request.setJsonedParam(req.getJsonedParam());
		request.setParamArray(req.getParamArray());
		req = null;

		ActionContext context = ActionContext.get().clone();
		try {
			ActionContext.rpcContext(request, context.isTest());
			locker.url(LOCAL);
			Response resp = LocalRequestHandler.inst.handler(request, node);
			ActionContext.recover(context);
			locker.wakeup(new RpcResult(resp.json(), resp.exception(), request.getSn()));
		} finally {
			ActionContext.recover(context);
		}
		return new RpcFutureImpl(locker);
	}

}
