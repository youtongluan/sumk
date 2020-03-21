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
package org.yx.rpc.server;

import org.yx.exception.SoaException;

public class Response {

	private String sn;
	private String json;
	private SoaException exception;

	private long ms = -1;

	public String sn() {
		return sn;
	}

	public String json() {
		return json;
	}

	public long serviceInvokeMilTime() {
		return ms;
	}

	public void sn(String sn) {
		this.sn = sn;
	}

	public void json(String json) {
		this.json = json;
	}

	public void serviceInvokeMilTime(long ms) {
		this.ms = ms;
	}

	public Response() {
	}

	public Response(String sn) {
		this.sn = sn;
	}

	public SoaException exception() {
		return exception;
	}

	public void exception(SoaException exception) {
		this.exception = exception;
	}

	public boolean isSuccess() {
		return this.exception == null;
	}

}
