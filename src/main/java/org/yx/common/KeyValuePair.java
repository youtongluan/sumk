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
package org.yx.common;

import java.util.Objects;

public class KeyValuePair<T> {
	private final String key;
	private final T value;

	public KeyValuePair(String key, T value) {
		this.key = Objects.requireNonNull(key);
		this.value = value;
	}

	public String key() {
		return key;
	}

	public T value() {
		return value;
	}
}