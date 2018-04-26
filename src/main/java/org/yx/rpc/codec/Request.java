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
package org.yx.rpc.codec;

import org.yx.exception.SumkException;
import org.yx.rpc.client.Req;

public class Request extends Req {
	/**
	 * 服务器端接收到req请求的时间
	 */
	private long startInServer = System.currentTimeMillis();

	private int protocol;

	public int protocol() {
		return this.protocol;
	}

	public void protocol(int protocol) {
		if (this.protocol != 0) {
			SumkException.throwException(34526546, "protocol has value already,value is " + this.protocol);
		}
		this.protocol = protocol;
	}

	public long getStartInServer() {
		return startInServer;
	}

}
