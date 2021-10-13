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

public class SoaSpec {

	private final String value;
	private final String cnName;
	private final boolean appIdPrefix;
	private final int toplimit;
	private final boolean publish;

	public SoaSpec(String value, String cnName, boolean appIdPrefix, int toplimit, boolean publish) {
		this.value = value;
		this.cnName = cnName;
		this.appIdPrefix = appIdPrefix;
		this.toplimit = toplimit;
		this.publish = publish;
	}

	public String value() {
		return this.value;
	}

	public String cnName() {
		return this.cnName;
	}

	public boolean appIdPrefix() {
		return this.appIdPrefix;
	}

	public int toplimit() {
		return this.toplimit;
	}

	public boolean publish() {
		return this.publish;
	}
}
