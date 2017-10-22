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
import org.yx.util.GsonUtil;

public class RpcResult {

	private final String json;
	private final CodeException exception;

	RpcResult(String json, CodeException exception) {
		super();
		this.json = exception != null ? null : json;
		this.exception = exception;
	}

	/**
	 * 获取返回值，如果有异常，会抛出异常
	 * 
	 * @return
	 * @throws CodeException
	 */
	public String getJsonResult() throws CodeException {
		this.checkException();
		return json;
	}

	public <T> T getResult(Class<T> clz) throws CodeException {
		this.checkException();
		if (this.json == null) {
			return null;
		}
		return GsonUtil.fromJson(json, clz);
	}

	public CodeException exception() {
		return exception;
	}

	private void checkException() {
		if (this.exception != null) {
			throw this.exception;
		}
	}

	public String optJsonResult() {
		return json;
	}

	public <T> T optResult(Class<T> clz) {
		if (this.json == null) {
			return null;
		}
		return GsonUtil.fromJson(json, clz);
	}

}
