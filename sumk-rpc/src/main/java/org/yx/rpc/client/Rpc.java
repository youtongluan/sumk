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

import java.util.Map;
import java.util.concurrent.Executor;

import org.yx.common.util.S;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.main.SumkServer;
import org.yx.rpc.RpcSettings;
import org.yx.rpc.client.route.ZkRouteParser;
import org.yx.rpc.transport.Transports;
import org.yx.util.SumkThreadPool;
import org.yx.util.Task;

public final class Rpc {
	private Rpc() {
	}

	private static volatile boolean strated;

	private static String appId;

	static String appId() {
		return appId;
	}

	private static Executor clientExecutor = SumkThreadPool.executor();

	public static Executor clientExecutor() {
		return clientExecutor;
	}

	public static void resetStatus() {
		strated = false;
	}

	public static synchronized void init() {
		if (strated) {
			return;
		}
		try {
			appId = AppInfo.appId("sumk");
			RpcSettings.init();
			Rpc.clientExecutor = SumkServer.getExecutor("sumk.rpc.client.executor");
			String zkUrl = AppInfo.getClinetZKUrl();
			if (zkUrl != null) {
				Logs.rpc().info("rpc client zkUrl:{}", zkUrl);
				ZkRouteParser.get(zkUrl).readRouteAndListen();
			} else {
				Logs.rpc().warn("##因为没有配置{},所以只能调用本机的微服务接口##", Const.ZK_URL);
			}
			Transports.init();
			Transports.factory().initClient();
			Task.scheduleAtFixedRate(TransportClientHolder::cleanReqSession, 60_000, 60_000);
			strated = true;
		} catch (Exception e) {
			throw SumkException.wrap(e);
		}
	}

	public static Client create(String api) {
		return new Client(api);
	}

	/**
	 * 根据参数顺序调用rpc方法<BR>
	 * 参数对象是原始的参数对象，不需要进行gson转化
	 * 
	 * @param api  注册的接口名称，如a.b.c
	 * @param args 支持泛型，比如List&lt;Integer&gt;之类的。但不提倡使用泛型。
	 * @return json格式的服务器响应结果
	 * @throws org.yx.exception.SoaException rpc异常
	 * @throws org.yx.exception.BizException 业务异常
	 */
	public static String call(String api, Object... args) {
		return callAsync(api, args).getOrException();
	}

	public static String callInMap(String api, Map<String, ?> map) {
		return callInMapAsync(api, map).getOrException();
	}

	private static <T> T fromJson(String json, Class<T> resultClz) {
		if (json == null) {
			return null;
		}
		return S.json().fromJson(json, resultClz);
	}

	public static <T> T invoke(Class<T> resultClz, String api, Object... args) {
		return fromJson(call(api, args), resultClz);
	}

	public static <T> T invokeInMap(Class<T> resultClz, String api, Map<String, ?> map) {
		return fromJson(callInMap(api, map), resultClz);
	}

	/**
	 * 根据参数顺序<b>异步</b>调用rpc方法
	 * 
	 * @param api  注册的接口名称，如a.b.c
	 * @param args 支持泛型，比如List&lt;Integer&gt;之类的。但不提倡使用泛型
	 * @return json格式的服务器响应结果
	 */
	public static RpcFuture callAsync(String api, Object... args) {
		return create(api).paramInArray(args).execute();
	}

	public static RpcFuture callInMapAsync(String api, Map<String, ?> map) {
		return create(api).paramInMap(map).execute();
	}

}
