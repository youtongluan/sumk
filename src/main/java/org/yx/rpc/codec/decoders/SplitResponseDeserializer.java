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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.yx.annotation.Bean;
import org.yx.exception.SoaException;
import org.yx.rpc.Profile;
import org.yx.rpc.RpcJson;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.server.Response;
import org.yx.util.S;

@Bean
public class SplitResponseDeserializer implements SumkMinaDeserializer<Response> {

	@Override
	public boolean accept(int protocol) {
		return Protocols.hasFeature(protocol, Protocols.RESPONSE_SPLIT);
	}

	@Override
	public Response decode(int protocol, byte[] data) {
		int index = DeSerializeKits.nextSplitIndex(data, 0);
		Response resp = new Response(new String(data, 0, index, Profile.UTF8));
		if (index == data.length) {
			return resp;
		}

		ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);
		buf.position(index + 1);
		int len = buf.getInt();
		index += 5;
		if (len != Integer.MIN_VALUE) {
			if (len > 0) {
				resp.json(new String(data, index, len, Profile.UTF8));
			} else {
				len &= Integer.MAX_VALUE;
				byte[] bs = Arrays.copyOfRange(data, ++index, len);
				resp.json(RpcJson.operator().toJson(bs));
			}
			index += len;
		}
		buf.position(index);
		len = buf.getInt();
		index += 4;
		if (len != Integer.MIN_VALUE) {
			resp.json(new String(data, index, data.length - index, Profile.UTF8));
			resp.exception(S.json().fromJson(new String(data, index, len, Profile.UTF8), SoaException.class));
		}
		return resp;
	}

}
