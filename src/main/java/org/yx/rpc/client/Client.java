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

import org.yx.common.Host;
import org.yx.common.context.ActionContext;
import org.yx.common.route.Router;
import org.yx.exception.SoaException;
import org.yx.log.Logs;
import org.yx.rpc.InnerRpcUtil;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.RpcActions;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.RpcJson;
import org.yx.rpc.RpcSettings;
import org.yx.rpc.client.route.HostChecker;
import org.yx.rpc.client.route.RpcRoutes;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.LocalRequestHandler;
import org.yx.rpc.server.Response;
import org.yx.rpc.transport.RpcWriteFuture;
import org.yx.rpc.transport.TransportClient;
import org.yx.util.UUIDSeed;

public final class Client {

	private static final Host LOCAL = Host.create("local", 0);
	private static final AtomicInteger COUNTER = new AtomicInteger();
	private final String api;
	private Object params;
	private ParamType paramType;
	private int totalTimeout;

	private Host[] directUrls;

	private boolean backup;
	private int tryCount;
	private Consumer<RpcCallInfo> callback;

	public Client(String api) {
		this.api = Objects.requireNonNull(api).trim();
		this.totalTimeout = RpcSettings.clientDefaultTimeout();
		this.tryCount = RpcSettings.clientTryCount();
	}

	public Client directUrls(Host... urls) {
		this.directUrls = urls;
		return this;
	}

	/**
	 * 设置发送的尝试次数。只有发送失败会重试，其它的不会
	 * 
	 * @param tryCount 尝试次数，包含第一次发送
	 * @return 当前对象
	 */
	public Client tryCount(int tryCount) {
		this.tryCount = tryCount > 0 ? tryCount : 1;
		return this;
	}

	/**
	 * 设置直连url不能用的时候，是否使用注册中心上的地址
	 * 
	 * @param backup 失败时是否启用注册中心上的地址
	 * @return 当前对象
	 */
	public Client backup(boolean backup) {
		this.backup = backup;
		return this;
	}

	public Client timeout(int timeout) {
		this.totalTimeout = timeout > 0 ? timeout : 1;
		return this;
	}

	public Client callback(Consumer<RpcCallInfo> callback) {
		this.callback = callback;
		return this;
	}

	public Client paramInArray(Object... args) {
		if (args == null) {
			args = new String[0];
		}
		String[] params = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (arg == null) {
				params[i] = null;
			} else if (arg.getClass() == String.class) {
				params[i] = (String) arg;
			} else {
				params[i] = RpcJson.client().toJson(arg);
			}
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
		return paramInJson(RpcJson.client().toJson(map));
	}

	protected Req createReq() {
		Req req = new Req();
		ActionContext context = ActionContext.current();
		if (context.isTest()) {
			req.setTest(true);
		}
		req.setStart(System.currentTimeMillis());
		String sn = UUIDSeed.seq18();
		req.setFullSn(sn, context.traceId(), context.nextSpanId());
		req.setUserId(context.userId());
		req.setApi(this.api);
		req.setFrom(Rpc.appId());

		req.setAttachments(context.attachmentView());
		return req;
	}

	/**
	 * 本方法调用之后，不允许再调用本对象的任何方法<BR>
	 * 
	 * @return 用无论是否成功，都会返回future。如果失败的话，异常包含在future中。<BR>
	 *         通信异常是SoaException；如果是业务类异常，则是BizException
	 */
	public RpcFuture execute() {
		Objects.requireNonNull(this.paramType, "param have not been set");
		Req req = this.createReq();
		long endTime = req.getStart() + this.totalTimeout;
		req.setParams(this.paramType.protocol(), this.params);
		int count = this.tryCount;
		while (true) {
			RpcFuture f = sendAsync(req, endTime);
			if (f.getClass() == ErrorRpcFuture.class) {
				ErrorRpcFuture errorFuture = (ErrorRpcFuture) f;
				RpcLocker locker = errorFuture.locker;
				LockHolder.remove(locker.req.getSn());
				if (--count > 0 && errorFuture.rpcResult().exception().getCode() == RpcErrorCode.SEND_FAILED
						&& System.currentTimeMillis() + 5 < endTime) {
					locker.discard(errorFuture.rpcResult());
					Logs.rpc().warn("无法发送数据到{}，重试rpc请求", locker.url());
					continue;
				}
				locker.wakeupAndLog(errorFuture.rpcResult());
			}
			return f;
		}
	}

	private Host selectDirectUrl() {
		int index = COUNTER.incrementAndGet();
		if (index < 0) {
			COUNTER.set((int) (System.nanoTime() & 0xff));
			index = COUNTER.incrementAndGet();
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

	private RpcFuture sendAsync(final Req req, final long endTime) {
		final RpcLocker locker = new RpcLocker(req, callback);
		Host url = null;
		if (this.directUrls != null && this.directUrls.length > 0) {
			url = selectDirectUrl();
			if (url == null && !this.backup) {
				SoaException ex = new SoaException(RpcErrorCode.NO_NODE_AVAILABLE,
						"all directUrls is disabled:" + Arrays.toString(this.directUrls), null);
				return new ErrorRpcFuture(ex, locker);
			}
		}
		if (url == null) {

			Router<Host> route = RpcRoutes.getRoute(api);
			RpcFuture future = this.tryLocalHandler(req, locker, route);
			if (future != null) {
				return future;
			}

			if (route == null) {
				SoaException ex = new SoaException(RpcErrorCode.NO_ROUTE, "can not find route for " + api, null);
				return new ErrorRpcFuture(ex, locker);
			}
			url = route.select();
		}
		if (url == null) {
			SoaException ex = new SoaException(RpcErrorCode.NO_NODE_AVAILABLE, "route for " + api + " are all disabled",
					null);
			return new ErrorRpcFuture(ex, locker);
		}
		locker.url(url);
		req.setServerProtocol(RpcRoutes.getServerProtocol(url));
		RpcWriteFuture f = null;
		try {
			TransportClient reqSession = TransportClientHolder.getSession(url);
			LockHolder.register(locker, endTime);
			f = reqSession.write(req);
		} catch (Exception e) {
			Logs.rpc().error(e.getLocalizedMessage(), e);
		}
		if (f == null) {
			SoaException ex = new SoaException(RpcErrorCode.SEND_FAILED, url + " can not connect", null);
			return new ErrorRpcFuture(ex, locker);
		}
		f.addListener(locker);
		return new RpcFutureImpl(locker);
	}

	private RpcFuture tryLocalHandler(Req req, RpcLocker locker, Router<Host> route) {
		RpcActionNode node = RpcActions.getActionNode(api);
		if (node == null) {
			return null;
		}

		if (RpcSettings.disableLocalRoute() && route != null) {
			return null;
		}

		Request request = new Request(req);
		req = null;

		ActionContext context = ActionContext.current().clone();
		try {
			InnerRpcUtil.rpcContext(request, context.isTest());
			locker.url(LOCAL);
			Response resp = LocalRequestHandler.inst.handler(request, node);
			ActionContext.store(context);
			locker.wakeupAndLog(new RpcResult(resp.json(), resp.exception()));
		} finally {
			ActionContext.store(context);
		}
		return new RpcFutureImpl(locker);
	}

}
