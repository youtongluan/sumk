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

import org.yx.annotation.Bean;
import org.yx.rpc.codec.StreamAble;
import org.yx.rpc.transport.DataBuffer;

@Bean
public class StreamAbleEncoder extends AbstractEncoder<StreamAble> {

	@Override
	public void encodeBody(StreamAble req, DataBuffer buffer) throws Exception {
		req.writeTo(buffer);
	}

	@Override
	protected StreamAble convert(Object message) {
		if (message instanceof StreamAble) {
			return (StreamAble) message;
		}
		return null;
	}

	@Override
	protected int getMessageType(StreamAble req) {
		return req.getMessageType();
	}
}
