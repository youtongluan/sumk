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

import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.log.Log;
import org.yx.rpc.codec.CodecKit;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.server.Response;
import org.yx.rpc.transport.DataBuffer;

@Bean
public class ResponseDecoder implements DataDecoder {

	private Logger log = Log.get(CodecKit.LOG_NAME);

	@Override
	public boolean accept(int protocol) {
		return Protocols.hasFeature(protocol, Protocols.RESPONSE);
	}

	@Override
	public Object decode(int protocol, DataBuffer data, int dataSize) throws Exception {
		if (log.isTraceEnabled()) {
			log.trace("decode {} bytes to Request", dataSize);
		}
		Response req = new Response();
		req.readFrom(data);
		return req;
	}

}
