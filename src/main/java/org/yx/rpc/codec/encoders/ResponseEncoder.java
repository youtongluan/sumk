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
package org.yx.rpc.codec.encoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.yx.annotation.Bean;
import org.yx.rpc.Profile;
import org.yx.rpc.RpcJson;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.SumkProtocolEncoder;
import org.yx.rpc.server.Response;

@Bean
public class ResponseEncoder implements SumkMinaEncoder {

	@Override
	public boolean accept(Class<?> messageClz) {
		return messageClz == Response.class;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Response resp = Response.class.cast(message);
		String sn = resp.sn();
		if (sn == null) {
			sn = "";
		}
		String json = resp.json();
		String strException = resp.exception() == null ? null : RpcJson.operator().toJson(resp.exception());
		int strLength = sn.length();
		if (json != null) {
			strLength += json.length();
		}
		if (strException != null) {
			strLength += strException.length();
		}

		IoBuffer buffer = SumkProtocolEncoder.createIoBuffer(strLength);
		SumkProtocolEncoder.putResponseProtocol(Protocols.RESPONSE_SPLIT, buffer);
		buffer.position(8);

		buffer.putString(sn, Profile.UTF8.newEncoder()).put(Protocols.LINE_SPLIT_BYTE);
		SumkProtocolEncoder.putPrefixedString(json, buffer);
		SumkProtocolEncoder.putPrefixedString(strException, buffer);
		int limit = buffer.limit();
		buffer.position(4);
		buffer.putInt(limit - 8);
		buffer.position(limit);
		buffer.flip();

		out.write(buffer);
	}

}
