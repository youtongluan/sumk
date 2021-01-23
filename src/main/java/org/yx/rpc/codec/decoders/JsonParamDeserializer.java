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
package org.yx.rpc.codec.decoders;

import org.yx.annotation.Bean;
import org.yx.rpc.Profile;
import org.yx.rpc.RpcJson;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.ReqParamType;
import org.yx.rpc.codec.Request;

@Bean
public class JsonParamDeserializer implements SumkMinaDeserializer<Request> {

	@Override
	public boolean accept(int protocol) {
		return Protocols.hasFeature(protocol, ReqParamType.REQ_PARAM_JSON);
	}

	@Override
	public Request decode(int protocol, byte[] data) throws Exception {
		int splitIndex = DeSerializeKits.nextSplitIndex(data, 0);

		String reqJson = new String(data, 0, splitIndex, Profile.UTF8);
		Request req = RpcJson.operator().fromJson(reqJson, Request.class);
		splitIndex = DeSerializeKits.nextSplitIndex(data, splitIndex + 1);
		int startIndex = splitIndex + 1;
		int length = data.length - startIndex;
		if (length < 0) {
			return req;
		}
		String json = length == 0 ? "" : new String(data, startIndex, length, Profile.UTF8);
		req.setParams(ReqParamType.REQ_PARAM_JSON, json);
		return req;
	}

}
