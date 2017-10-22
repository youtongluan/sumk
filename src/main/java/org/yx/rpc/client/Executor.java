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
import java.util.concurrent.ConcurrentHashMap;

import org.yx.conf.AppInfo;
import org.yx.exception.ConnectionException;
import org.yx.exception.SoaException;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.RpcCode;
import org.yx.rpc.RpcUtils;
import org.yx.rpc.client.route.HostChecker;
import org.yx.rpc.client.route.ZkRouteParser;

public class Executor {

	private static Map<String, Long> TimeoutMap = new ConcurrentHashMap<>();
	private static long DEFAULT_TIMEOUT;

	private static volatile boolean strated;

	public static synchronized void init() {
		if (strated) {
			return;
		}
		try {
			DEFAULT_TIMEOUT = AppInfo.getInt("soa.timeout", 30000);
			String zkUrl = AppInfo.getZKUrl();
			Log.get("sumk.rpc").info("zkUrl:{}", zkUrl);
			ZkRouteParser.get(zkUrl).readRouteAndListen();
			strated = true;
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

	public static String call(Req req) {
		ReqResp reqResp;
		try {

			reqResp = ReqSender.send(req, TimeoutMap.getOrDefault(RpcUtils.getAppId(req.getApi()), DEFAULT_TIMEOUT));
		} catch (Throwable e) {
			if (SoaException.class.isInstance(e)) {
				throw SoaException.class.cast(e);
			}
			if (ConnectionException.class.isInstance(e)) {
				HostChecker.get().addDownUrl(ConnectionException.class.cast(e).getHost());
			}
			Log.printStack(e);
			throw new SoaException(RpcCode.UNKNOW, e.getMessage(), e);
		}
		reqResp.getResp().checkException();
		return reqResp.getResp().getJson();
	}

	public static RpcFuture callAsync(Req req, long writeTimeout) {
		RespFuture reqResp;
		try {
			reqResp = ReqSender.sendAsync(req, writeTimeout);
		} catch (Throwable e) {
			if (ConnectionException.class.isInstance(e)) {
				HostChecker.get().addDownUrl(ConnectionException.class.cast(e).getHost());
			}
			return new ErrorRpcFuture(e);
		}
		return new RpcFutureImpl(reqResp);
	}

	public static RpcFuture callAsync(Req req) {
		return callAsync(req, AppInfo.getInt("soa.write.async.timeout", -1));
	}
}
