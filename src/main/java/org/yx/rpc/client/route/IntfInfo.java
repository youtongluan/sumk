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
package org.yx.rpc.client.route;

public class IntfInfo {
	private String n;
	private Integer w;
	private Integer c;

	public String getName() {
		return n;
	}

	public void setName(String name) {
		this.n = name;
	}

	public Integer getWeight() {
		return w;
	}

	public void setWeight(Integer weight) {
		this.w = weight;
	}

	public Integer getClientCount() {
		return c;
	}

	public void setClientCount(Integer clientCount) {
		this.c = clientCount;
	}

}
