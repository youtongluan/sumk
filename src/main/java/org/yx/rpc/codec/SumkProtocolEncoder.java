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

import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderException;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.yx.annotation.Bean;
import org.yx.annotation.Inject;
import org.yx.rpc.Profile;
import org.yx.rpc.codec.encoders.SumkMinaEncoder;

@Bean
public class SumkProtocolEncoder implements ProtocolEncoder {

	@Inject
	private SumkMinaEncoder[] encoders;

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message == null) {
			return;
		}
		Class<?> clz = message.getClass();
		for (SumkMinaEncoder encoder : this.encoders) {
			if (encoder.accept(clz)) {
				encoder.encode(session, message, out);
				return;
			}
		}
		throw new ProtocolEncoderException(message.getClass().getName() + " not support in ProtocolEncoder");
	}

	public static void putRequestProtocol(int code, IoBuffer buffer) throws ProtocolEncoderException {
		buffer.putInt(Protocols.MAGIC | Protocols.REQUEST | code);
	}

	public static void putResponseProtocol(int code, IoBuffer buffer) throws ProtocolEncoderException {
		buffer.putInt(Protocols.MAGIC | Protocols.RESPONSE | code);
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

	public static void putPrefixedString(CharSequence msg, IoBuffer buffer) throws CharacterCodingException {
		if (msg == null) {
			buffer.putInt(Integer.MIN_VALUE);
			return;
		}
		buffer.putPrefixedString(msg, 4, Profile.UTF8.newEncoder());
	}

	public static IoBuffer createIoBuffer(int strLength) {

		return IoBuffer.allocate(Math.max(strLength, strLength << 1)).setAutoExpand(true).order(ByteOrder.BIG_ENDIAN);
	}
}
