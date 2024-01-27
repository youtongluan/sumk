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
package org.yx.rpc.spec;

public class SoaClientConfigSpec {

	private final int timeout;
	private final int tryCount;

	public SoaClientConfigSpec(int timeout, int tryCount) {
		this.timeout = timeout;
		this.tryCount = tryCount;
	}

	public int timeout() {
		return this.timeout;
	}

	public int tryCount() {
		return this.tryCount;
	}
}
