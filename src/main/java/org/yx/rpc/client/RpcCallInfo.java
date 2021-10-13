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

public class RpcCallInfo {
	private final RpcResult result;
	private final Host server;
	private final String sn;

	public RpcCallInfo(String sn, RpcResult result, Host server) {
		this.sn = sn;
		this.result = result;
		this.server = server;
	}

	/**
	 * 返回值不为null
	 * 
	 * @return 本次请求的唯一编码
	 */
	public String getRequestId() {
		return this.sn;
	}

	/**
	 * 如果是本地调用，返回local:0
	 * 
	 * @return 被调用的服务端地址
	 */
	public Host getServer() {
		return server;
	}

	public RpcResult getResult() {
		return result;
	}

	public CodeException exception() {
		return result == null ? null : result.exception;
	}

	public String json() {
		return result == null ? null : result.json;
	}

	public <T> T optResult(Class<T> clz) {
		return result == null ? null : result.optResult(clz);
	}

}
