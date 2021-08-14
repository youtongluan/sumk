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

public class InjectSpec {
	private final boolean allowEmpty;

	private final String value;

	public InjectSpec(String value, boolean allowEmpty) {
		this.value = value;
		this.allowEmpty = allowEmpty;
	}

	public String value() {
		return value;
	}

	/**
	 * 数组、集合类型不可能为null，但是可以为空
	 * 
	 * @return 如果为true，表示可以为null或空集合
	 */
	public boolean allowEmpty() {
		return this.allowEmpty;
	}
}
