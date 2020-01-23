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

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.rpc.RpcGson;
import org.yx.rpc.client.Req;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.SumkProtocolEncoder;

@Bean
public class ReqEncoder implements SumkMinaEncoder {

	private Logger log = Logs.rpc();

	@Override
	public boolean accept(Class<?> messageClz) {
		return messageClz == Req.class;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Req req = Req.class.cast(message);
		String jsonedArg = req.getJsonedParam();
		String[] params = req.getParamArray();
		int paramProtocal = req.paramProtocol();

		if (paramProtocal == Protocols.REQ_PARAM_JSON) {
			String json_req = String.join(Protocols.LINE_SPLIT, RpcGson.toJson(req), jsonedArg);
			if (Log.isON(log)) {
				log.trace("req in json: {}", json_req);
			}
			SumkProtocolEncoder.encodeString(paramProtocal, session, json_req, out);
			return;
		}

		if (params == null) {
			params = new String[0];
		}
		StringBuilder json_req = new StringBuilder();
		json_req.append(String.format("%02d", params.length)).append(RpcGson.toJson(req));
		for (String p : params) {
			json_req.append(Protocols.LINE_SPLIT).append(p);
		}
		if (Log.isON(log)) {
			log.trace("req in array: {}", json_req);
		}
		SumkProtocolEncoder.encodeString(paramProtocal, session, json_req, out);
	}

}
