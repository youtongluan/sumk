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

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.codec.ProtocolDeserializer;
import org.yx.rpc.server.Response;

public class ClientHandler implements IoHandler {

	private final ProtocolDeserializer deserializer;

	public ClientHandler() {
		deserializer = IOC.get(ProtocolDeserializer.class);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		long time = System.currentTimeMillis() - session.getLastIoTime();
		if (time > AppInfo.getLong(Const.SOA_SESSION_IDLE, 60) * 1000) {
			Log.get("sumk.rpc.client").info("rpc session {} {} for {}ms,closed by this client", session.getId(), status,
					session.getLastIoTime(), time);
			session.closeOnFlush();
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		Log.get("sumk.rpc.client").error(session + " throw exception", cause);

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Object obj = this.deserializer.deserialize(message);
		if (obj == null) {
			return;
		}
		if (Response.class.isInstance(obj)) {
			Response resp = (Response) obj;
			LockHolder.unLockAndSetResult(resp);
			return;
		}
		throw new SumkException(458223, obj.getClass().getName() + " has not deserialized");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		session.closeNow();
	}

	@Override
	public void event(IoSession arg0, FilterEvent arg1) throws Exception {

	}

}
