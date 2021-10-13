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
package org.yx.rpc.transport;

import java.net.InetSocketAddress;

public interface TransportChannel {
	InetSocketAddress getRemoteAddress();

	RpcWriteFuture write(Object message);

	boolean isConnected();

	boolean isClosing();

	void closeNow();

	void closeOnFlush();

	Object getAttribute(String key);

	void setAttribute(String key, Object value);

	void removeAttribute(String key);
}
