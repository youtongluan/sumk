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

import java.util.Objects;

import org.yx.common.Host;
import org.yx.common.context.LogContext;
import org.yx.exception.CodeException;
import org.yx.util.S;

public abstract class AbstractRpcFuture implements RpcFuture {

	protected final RpcLocker locker;

	public AbstractRpcFuture(RpcLocker locker) {
		this.locker = Objects.requireNonNull(locker);
	}

	@Override
	public <T> T getOrException(Class<T> clz) throws CodeException {
		String json = this.getOrException();
		if (json == null) {
			return null;
		}
		return S.json().fromJson(json, clz);
	}

	@Override
	public String getOrException() throws CodeException {
		RpcResult resp = this.awaitForRpcResult();
		resp.throwIfException();
		return resp.json();
	}

	@Override
	public <T> T opt(Class<T> clz) throws CodeException {
		String json = this.opt();
		if (json == null) {
			return null;
		}
		return S.json().fromJson(json, clz);
	}

	@Override
	public String opt() throws CodeException {
		RpcResult resp = this.awaitForRpcResult();
		return resp.json();
	}

	@Override
	public Host getServer() {
		return this.locker.url();
	}

	public LogContext getOriginLogContext() {
		return this.locker.originLogContext();
	}

	@Override
	public String getRequestId() {
		return this.locker.req.getSn();
	}
}
