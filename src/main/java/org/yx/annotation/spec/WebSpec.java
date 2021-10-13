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
package org.yx.annotation.spec;

import java.util.Objects;

import org.yx.http.MessageType;

public class WebSpec {

	private final String value;
	private final String cnName;
	private final boolean requireLogin;
	private final MessageType requestType;
	private final boolean sign;
	private final MessageType responseType;
	private final String[] tags;
	private final int toplimit;
	private final String[] method;

	public WebSpec(String value, String cnName, boolean requireLogin, MessageType requestType, boolean sign,
			MessageType responseType, String[] tags, int toplimit, String[] method) {
		this.value = Objects.requireNonNull(value);
		this.cnName = Objects.requireNonNull(cnName);
		this.requireLogin = requireLogin;
		this.requestType = Objects.requireNonNull(requestType);
		this.sign = sign;
		this.responseType = Objects.requireNonNull(responseType);
		this.tags = Objects.requireNonNull(tags);
		this.toplimit = toplimit;
		this.method = Objects.requireNonNull(method);
	}

	public String value() {
		return this.value;
	}

	public String cnName() {
		return this.cnName;
	}

	public boolean requireLogin() {
		return this.requireLogin;
	}

	public MessageType requestType() {
		return this.requestType;
	}

	public boolean sign() {
		return this.sign;
	}

	public MessageType responseType() {
		return this.responseType;
	}

	public String[] tags() {
		return this.tags;
	}

	public int toplimit() {
		return this.toplimit;
	}

	public String[] method() {
		return this.method;
	}
}
