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

import org.yx.common.Host;
import org.yx.exception.CodeException;

public interface RpcFuture {

	/**
	 * 等待返回，直到超时
	 * 
	 * @return 如果没有异常，就返回信息，如果发生了异常，就抛出异常
	 */
	String getOrException() throws CodeException;

	<T> T getOrException(Class<T> clz) throws CodeException;

	/**
	 * 等待返回，直到超时
	 * 
	 * @return 如果发生异常就返回null
	 * 
	 */
	String opt();

	<T> T opt(Class<T> clz);

	RpcResult awaitForRpcResult();

	/**
	 * 这个方法不会等待
	 * 
	 * @return 如果已经收到返回值，就返回。否则返回null
	 */
	RpcResult rpcResult();

	String getRequestId();

	Host getServer();
}
