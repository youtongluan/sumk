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

import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderException;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.rpc.Profile;
import org.yx.rpc.RpcJson;
import org.yx.rpc.client.Req;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.ReqParamType;
import org.yx.rpc.codec.SumkProtocolEncoder;

@Bean
public class ReqEncoder implements SumkMinaEncoder {

	private Logger log = Logs.rpc();

	private String NULL = "null";

	@Override
	public boolean accept(Class<?> messageClz) {
		return messageClz == Req.class;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Req req = (Req) message;
		String jsonedArg = req.getJsonedParam();
		String[] params = req.getParamArray();

		if (req.hasFeature(ReqParamType.REQ_PARAM_JSON)) {
			this.encodeRequestString(ReqParamType.REQ_PARAM_JSON, out, new String[] { RpcJson.operator().toJson(req),
					Protocols.LINE_SPLIT + Protocols.LINE_SPLIT, jsonedArg });
			return;
		}

		if (params == null) {
			params = new String[0];
		}
		int paramLength = params.length;
		List<String> order_req = new ArrayList<>(paramLength * 2 + 3);
		if (paramLength > 99) {
			throw new SumkException(456543, "微服务参数太多，最多只有99个");
		}
		order_req.add(RpcJson.operator().toJson(req));
		order_req.add(Protocols.LINE_SPLIT + Protocols.LINE_SPLIT);
		String strParamLength = new String(new byte[] { (byte) paramLength }, AppInfo.UTF8);
		order_req.add(strParamLength);
		for (int i = 0; i < paramLength; i++) {
			order_req.add(params[i]);
			if (i != paramLength - 1) {
				order_req.add(Protocols.LINE_SPLIT);
			}
		}
		if (Log.isON(log)) {
			log.trace("req in array: {}", order_req);
		}
		this.encodeRequestString(ReqParamType.REQ_PARAM_ORDER, out, order_req.toArray(new String[order_req.size()]));
	}

	protected void encodeRequestString(int code, ProtocolEncoderOutput out, CharSequence[] message)
			throws CharacterCodingException, ProtocolEncoderException {
		int strLength = 0;

		for (CharSequence m : message) {
			strLength += m == null ? NULL.length() : m.length();
		}

		IoBuffer buffer = SumkProtocolEncoder.createIoBuffer(strLength);
		SumkProtocolEncoder.putRequestProtocol(code, buffer);
		buffer.position(8);

		for (CharSequence m : message) {
			if (m == null) {
				buffer.putString(NULL, Profile.UTF8.newEncoder());
				continue;
			}

			buffer.putString(m, Profile.UTF8.newEncoder());
		}
		int pos = buffer.position();
		buffer.position(4);
		buffer.putInt(pos - 8);
		buffer.position(pos);
		buffer.flip();

		out.write(buffer);
	}

}
