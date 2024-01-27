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

import org.yx.exception.SumkException;

public class ZKPathData {
	private String name;
	private byte[] data;

	public ZKPathData(String name, byte[] data) {
		if (name.contains("/")) {
			throw new SumkException(23543534, name + "应该只是当前目录，而不是全路径");
		}
		this.name = name;
		this.data = data;
	}

	public String name() {
		return name;
	}

	public byte[] data() {
		return data;
	}

}
