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

import org.yx.common.ThreadContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.exception.SoaException;
import org.yx.util.GsonUtil;
import org.yx.util.UUIDSeed;

/**
 * RPC调用的异常一般是SoaException，如果是业务类异常，会抛出BizException
 */
public final class Rpc {

	public static void init() {
		Executor.init();
	}

	private static Req createReq(String method) {
		Req req = new Req();
		req.setStart(System.currentTimeMillis());
		String sn = UUIDSeed.random();
		ThreadContext context = ThreadContext.get();
		req.setFullSn(sn, context.getRootSn(), context.getContextSn());
		req.setMethod(method);
		req.setSrc(AppInfo.appId());
		return req;
	}

	/**
	 * 根据参数顺序调用rpc方法
	 * 
	 * @param method
	 * @param args
	 *            支持泛型，比如List<Integer>,List<String>之类的。但不提倡使用泛型
	 * @return
	 * @throws SoaException
	 * @throws BizException
	 */
	public static String call(String method, Object... args) {
		Req req = createReq(method);
		String[] params = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			params[i] = GsonUtil.toJson(args[i]);
		}
		req.setParamArray(params);
		return Executor.call(req);
	}

	/**
	 * 
	 * @param method
	 * @param arg
	 *            用json序列化的参数对象
	 * @return
	 * @throws SoaException
	 * @throws BizException
	 */
	public static String callInJson(String method, String arg) {
		Req req = createReq(method);
		req.setJsonedParam(arg);
		return Executor.call(req);
	}

	/**
	 * 
	 * @param method
	 * @param arg
	 * @return
	 * @throws SoaException
	 * @throws BizException
	 */
	public static String callInMap(String method, String arg) {
		return callInJson(method, GsonUtil.toJson(arg));
	}

	/**
	 * 根据参数顺序<b>异步</b>调用rpc方法
	 * 
	 * @param method
	 * @param args
	 *            支持泛型，比如List<Integer>,List<String>之类的。但不提倡使用泛型
	 * @return
	 * @throws SoaException
	 */
	public static RpcFuture callAsync(String method, Object... args) {
		Req req = createReq(method);
		String[] params = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			params[i] = GsonUtil.toJson(args[i]);
		}
		req.setParamArray(params);
		return Executor.callAsync(req);
	}

	/**
	 * 
	 * @param method
	 * @param arg
	 *            用json序列化的参数对象
	 * @return
	 */
	public static RpcFuture callInJsonAsync(String method, String arg) {
		Req req = createReq(method);
		req.setJsonedParam(arg);
		return Executor.callAsync(req);
	}

	/**
	 * @param method
	 * @param arg
	 * @return
	 * @throws SoaException
	 */
	public static RpcFuture callInMapAsync(String method, String arg) {
		return callInJsonAsync(method, GsonUtil.toJson(arg));
	}

	/**
	 * 根据参数顺序<b>异步</b>调用rpc方法
	 * 
	 * @throws SoaException
	 */
	public static RpcFuture callAsync(String method, Object[] args, long writeTimeout) {
		Req req = createReq(method);
		String[] params = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			params[i] = GsonUtil.toJson(args[i]);
		}
		req.setParamArray(params);
		return Executor.callAsync(req, writeTimeout);
	}

	/**
	 * 
	 * @param method
	 * @param arg
	 *            用json序列化的参数对象
	 * @return
	 * @throws SoaException
	 */
	public static RpcFuture callInJsonAsync(String method, String arg, long writeTimeout) {
		Req req = createReq(method);
		req.setJsonedParam(arg);
		return Executor.callAsync(req, writeTimeout);
	}

	/**
	 * 
	 * @param method
	 * @param arg
	 * @param writeTimeout
	 * @return
	 * @throws SoaException
	 */
	public static RpcFuture callInMapAsync(String method, String arg, long writeTimeout) {
		return callInJsonAsync(method, GsonUtil.toJson(arg), writeTimeout);
	}

}
