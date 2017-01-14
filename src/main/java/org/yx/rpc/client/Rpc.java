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
import org.yx.exception.SoaException;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.RpcUtils;
import org.yx.rpc.SourceSn;
import org.yx.rpc.client.route.ZkRouteParser;
import org.yx.util.GsonUtil;
import org.yx.util.UUIDSeed;

public final class Rpc {
	private static String getAppId() {
		return AppInfo.getAppId();
	}

	private static Map<String, Long> TimeoutMap = new ConcurrentHashMap<>();
	private static long DEFAULT_TIMEOUT;
	private static boolean strated;

	public static synchronized void init() {
		if (strated) {
			return;
		}
		try {
			DEFAULT_TIMEOUT = AppInfo.getInt("RPC.TIMEOUT", 30000);
			String zkUrl = AppInfo.getZKUrl();
			Log.get("SYS.2").info("zkUrl:{}", zkUrl);
			new ZkRouteParser().parse(zkUrl);
			strated = true;
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

	private static Req createReq(String method) {
		Req req = new Req();
		req.setStart(System.currentTimeMillis());
		String sn = UUIDSeed.random();
		req.setSn(sn);
		String sn0 = SourceSn.getSn0();
		if (sn0 != null) {
			req.setSn0(sn);
		}
		req.setMethod(method);
		req.setSrc(getAppId());
		return req;
	}

	private static String call0(Req req) {
		ReqResp reqResp;
		try {
			reqResp = ReqSender.send(req, TimeoutMap.getOrDefault(RpcUtils.getAppId(req.getMethod()), DEFAULT_TIMEOUT));
		} catch (Throwable e) {
			Log.printStack(e);
			throw new SoaException(1022, e.getMessage(), e);
		}
		reqResp.getResp().checkException();
		return reqResp.getResp().getJson();
	}

	/**
	 * 根据参数顺序调用rpc方法
	 * 
	 * @param method
	 * @param args
	 *            支持泛型，比如List<Integer>,List<String>之类的。但不提倡使用泛型
	 * @return
	 */
	public static String call(String method, Object... args) {
		Req req = createReq(method);
		String[] params = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			params[i] = GsonUtil.toJson(args[i]);
		}
		req.setParamArray(params);
		return call0(req);
	}

	/**
	 * 
	 * @param method
	 * @param arg
	 *            用json序列化的参数对象
	 * @return
	 */
	public static String callInJson(String method, String arg) {
		Req req = createReq(method);
		req.setJsonedParam(arg);
		return call0(req);
	}

	public static String callInMap(String method, String arg) {
		return callInJson(method, GsonUtil.toJson(arg));
	}
}
