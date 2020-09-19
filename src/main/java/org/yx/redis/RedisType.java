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
package org.yx.redis;

public enum RedisType {
	SIMPLE, CLUSTER,
	/**
	 * 实验性功能
	 */
	SENTINEL, OTHER;

	public boolean accept(String type) {
		if (type == null || type.isEmpty()) {
			return false;
		}
		if (this.name().equalsIgnoreCase(type)) {
			return true;
		}
		if (String.valueOf(this.ordinal()).equals(type)) {
			return true;
		}
		return false;
	}
}
