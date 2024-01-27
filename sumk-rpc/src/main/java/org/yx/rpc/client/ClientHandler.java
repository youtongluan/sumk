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
package org.yx.rpc.client;

import org.yx.log.Log;
import org.yx.rpc.BusinessHandler;
import org.yx.rpc.server.Response;
import org.yx.rpc.transport.TransportChannel;

public class ClientHandler implements BusinessHandler {

	@Override
	public void received(TransportChannel channel, Object message) {
		if (message == null) {
			return;
		}
		if (message instanceof Response) {
			Response resp = (Response) message;
			LockHolder.unLockAndSetResult(resp);
			return;
		}
		Log.get("sumk.rpc.client").warn("unkown client message type:{}", message.getClass().getName());
	}

	@Override
	public void exceptionCaught(TransportChannel channel, Throwable exception) {
		Log.get("sumk.rpc.client").error(channel + " throw exception", exception);
		channel.closeNow();
	}

	@Override
	public void closed(TransportChannel channel) {
	}

}
