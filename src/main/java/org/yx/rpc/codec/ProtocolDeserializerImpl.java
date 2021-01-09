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
package org.yx.rpc.codec;

import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.yx.annotation.Bean;
import org.yx.annotation.Inject;
import org.yx.exception.SumkException;
import org.yx.rpc.codec.decoders.SumkMinaDeserializer;

@Bean
public class ProtocolDeserializerImpl implements ProtocolDeserializer {

	@Inject
	private SumkMinaDeserializer<?>[] decoders;

	@Override
	public Object deserialize(Object message) throws Exception {
		if (message == null) {
			return null;
		}
		if (!(message instanceof ProtocolObject)) {
			throw new SumkException(458223, message.getClass().getName() + " is error type");
		}
		ProtocolObject obj = ProtocolObject.class.cast(message);
		if (obj.getData().length == 0) {
			return null;
		}
		int protocol = obj.getProtocol();
		for (SumkMinaDeserializer<?> decoder : this.decoders) {
			if (decoder.accept(protocol)) {
				return decoder.decode(protocol, obj.getData());
			}
		}
		throw new ProtocolDecoderException("no sumk decoder:" + Integer.toHexString(protocol));
	}

}
