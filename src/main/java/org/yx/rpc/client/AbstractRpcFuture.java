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

import org.yx.conf.AppInfo;
import org.yx.exception.CodeException;
import org.yx.util.GsonUtil;

public abstract class AbstractRpcFuture implements RpcFuture {

	private static final String FUTURE_TIMEOUT = "soa.future.timeout";

	private static final int DEFAULT_TIMEOUT = 1000 * 30;

	public <T> T get(Class<T> clz, long timeout) throws CodeException {
		String json = this.get(timeout);
		if (json == null) {
			return null;
		}
		return GsonUtil.fromJson(json, clz);
	}

	public String get() throws CodeException {
		return get(AppInfo.getInt(FUTURE_TIMEOUT, DEFAULT_TIMEOUT));
	}

	public String opt() {
		return opt(AppInfo.getInt(FUTURE_TIMEOUT, DEFAULT_TIMEOUT));
	}

	public <T> T get(Class<T> clz) throws CodeException {
		return get(clz, AppInfo.getInt(FUTURE_TIMEOUT, DEFAULT_TIMEOUT));
	}

	public <T> T opt(Class<T> clz) {
		return opt(clz, AppInfo.getInt(FUTURE_TIMEOUT, DEFAULT_TIMEOUT));
	}

	public <T> T opt(Class<T> clz, long timeout) {
		String json = this.opt(timeout);
		if (json == null) {
			return null;
		}
		return GsonUtil.fromJson(json, clz);
	}

	public String opt(long timeout) {
		RpcResult resp = rpcResult(timeout);
		return resp.optJsonResult();
	}

	public String get(long timeout) throws CodeException {
		RpcResult resp = rpcResult(timeout);
		return resp.getJsonResult();
	}
}
