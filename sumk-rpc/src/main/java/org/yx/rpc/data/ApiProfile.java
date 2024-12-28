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
package org.yx.rpc.data;

import org.yx.util.StringUtil;

public class ApiProfile {
	private final String name;
	private Integer weight;

	public ApiProfile(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Integer getWeight() {
		return weight;
	}

	void setWeight(String w) {
		if (StringUtil.isEmpty(w)) {
			return;
		}
		this.weight = Integer.valueOf(w);
	}

	@Override
	public String toString() {
		return name;
	}

}
