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

import org.yx.exception.CodeException;
import org.yx.exception.SoaException;
import org.yx.rpc.RpcErrorCode;

public class ErrorRpcFuture extends AbstractRpcFuture {

	private final RpcResult rpcResult;

	public ErrorRpcFuture(Throwable e, RpcLocker locker) {
		super(locker);
		CodeException exception = e instanceof CodeException ? (CodeException) e
				: new SoaException(e, RpcErrorCode.UNKNOW, e.getMessage());
		this.rpcResult = new RpcResult(null, exception);
	}

	public RpcResult awaitForRpcResult() {
		return this.rpcResult;
	}

	@Override
	public RpcResult rpcResult() {
		return this.rpcResult;
	}
}
