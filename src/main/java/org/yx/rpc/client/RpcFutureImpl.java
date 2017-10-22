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

import org.yx.exception.CodeException;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.RpcCode;
import org.yx.rpc.server.Response;

public class RpcFutureImpl extends AbstractRpcFuture {
	private RespFuture target;

	private volatile RpcResult result;

	public RpcFutureImpl(RespFuture target) {
		this.target = target;
	}

	public RpcResult rpcResult(long timeout) {
		if (this.result != null) {
			return this.result;
		}
		synchronized (this) {
			if (this.result == null) {
				try {
					Response r = target.getResponse(timeout).getResp();
					this.result = new RpcResult(r.getJson(), r.getException());
				} catch (Exception e) {
					CodeException ex = CodeException.class.isInstance(e) ? CodeException.class.cast(e)
							: new SoaException(RpcCode.UNKNOW, e.getMessage(), e);
					this.result = new RpcResult(null, ex);
					Log.get("sumk.rpc.async").debug(e.getMessage(), e);
				}
			}
		}
		return this.result;
	}
}
