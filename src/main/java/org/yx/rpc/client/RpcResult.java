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

import org.yx.exception.BizException;
import org.yx.exception.CodeException;
import org.yx.exception.SoaException;
import org.yx.rpc.RpcErrorCode;
import org.yx.util.S;

public final class RpcResult {
	static RpcResult timeout(Req req) {
		if (req == null) {
			return new RpcResult(null, new SoaException(RpcErrorCode.TIMEOUT, "服务处理超时", "req is null"));
		}
		long timeout = System.currentTimeMillis() - req.getStart();
		String msg = "timeout in " + timeout + "ms,sn=" + req.getSn();
		SoaException exception = new SoaException(RpcErrorCode.TIMEOUT, "服务处理超时", msg);
		return new RpcResult(null, exception);
	}

	static RpcResult sendFailed(Req req, Throwable e) {
		return new RpcResult(null, parseException(e));
	}

	static CodeException parseException(Throwable e) {
		if (e == null) {
			return null;
		}
		if (!(e instanceof CodeException)) {
			return new SoaException(e, RpcErrorCode.UNKNOW, e.getMessage());
		}
		if (SoaException.class == e.getClass()) {
			SoaException ex = (SoaException) e;
			if (ex.isBizException()) {
				return BizException.create(ex.getCode(), ex.getMessage());
			}
		}
		return (CodeException) e;
	}

	final String json;
	final CodeException exception;

	public RpcResult(String json, CodeException exception) {
		this.json = exception != null ? null : json;
		this.exception = parseException(exception);
	}

	public CodeException exception() {
		return exception;
	}

	public void throwIfException() throws CodeException {
		if (this.exception != null) {
			throw exception;
		}
	}

	public String json() {
		return json;
	}

	public <T> T optResult(Class<T> clz) {
		if (this.json == null) {
			return null;
		}
		return S.json().fromJson(json, clz);
	}
}
