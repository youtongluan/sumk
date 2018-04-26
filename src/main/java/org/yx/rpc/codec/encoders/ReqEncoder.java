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
import org.yx.log.Log;
import org.yx.rpc.client.Req;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.SumkProtocolEncoder;
import org.yx.util.GsonUtil;

public class ReqEncoder implements SumkMinaEncoder {

	@Override
	public boolean accept(Class<?> messageClz) {
		return messageClz == Req.class;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Req req = Req.class.cast(message);
		String jsonedArg = req.getJsonedParam();
		String[] params = req.getParamArray();
		req.clearParams();

		if (jsonedArg != null) {
			Log.get("sumk.rpc").trace("args:{}", jsonedArg);
			String json_req = GsonUtil.toJson(req).concat(Protocols.LINE_SPLIT).concat(jsonedArg);
			SumkProtocolEncoder.encodeString(Protocols.REQ_PARAM_JSON, session, json_req, out);
			return;
		}

		StringBuilder json_req = new StringBuilder();
		json_req.append(String.format("%02d", params.length)).append(GsonUtil.toJson(req));
		for (String p : params) {
			json_req.append(Protocols.LINE_SPLIT).append(p);
		}
		SumkProtocolEncoder.encodeString(Protocols.REQ_PARAM_ORDER, session, json_req, out);
	}

}
