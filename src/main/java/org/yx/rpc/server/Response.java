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
package org.yx.rpc.server;

import org.yx.exception.BizException;
import org.yx.exception.SoaException;

public class Response {

	private String sn;
	private String json;
	private SoaException exception;

	private Long ms;

	public String getSn() {
		return sn;
	}

	public String getJson() {
		return json;
	}

	public Long getMs() {
		return ms;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setMs(Long ms) {
		this.ms = ms;
	}

	public Response() {
		super();
	}

	public Response(String sn) {
		super();
		this.sn = sn;
	}

	public SoaException getException() {
		return exception;
	}

	public void setException(SoaException exception) {
		this.exception = exception;
	}

	public void checkException() {
		if (this.exception == null) {
			return;
		}
		if (BizException.class.getName().equals(exception.getExceptionClz())) {
			BizException.throwException(this.exception.getCode(), this.getException().getMessage(), this.exception);
		}
		throw exception;
	}

}
