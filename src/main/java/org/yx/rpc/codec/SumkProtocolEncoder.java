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

import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderException;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.yx.annotation.Bean;
import org.yx.annotation.Inject;
import org.yx.conf.AppInfo;
import org.yx.conf.Profile;
import org.yx.rpc.codec.encoders.SumkMinaEncoder;

@Bean
public class SumkProtocolEncoder implements ProtocolEncoder {

	@Inject
	private SumkMinaEncoder[] encoders;

	private static int CHAR_BYTE = AppInfo.getInt("soa.charbyte", 3);

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message == null) {
			return;
		}
		if (String.class.isInstance(message)) {
			encodeString(0, session, (String) message, out);
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

	private static void putProtocol(int code, IoBuffer buffer, int prefixLength) throws ProtocolEncoderException {
		switch (prefixLength) {
		case 1:
			buffer.putInt(Protocols.ONE | code | Protocols.MAGIC);

			break;
		case 2:
			buffer.putInt(Protocols.TWO | code | Protocols.MAGIC);

			break;
		case 4:
			buffer.putInt(Protocols.FOUR | code | Protocols.MAGIC);

			break;
		default:
			throw new ProtocolEncoderException("error size");
		}
	}

	public static void encodeString(int code, IoSession session, CharSequence message, ProtocolEncoderOutput out)
			throws CharacterCodingException, ProtocolEncoderException {
		code = code | Protocols.FORMAT_JSON;
		int size = message.length();
		int prefixLength = size <= (Protocols.MAX_ONE / CHAR_BYTE) ? 1
				: size <= (Protocols.MAX_TWO / CHAR_BYTE) ? 2 : 4;

		IoBuffer buffer = IoBuffer.allocate((int) (size * 1.5) + 10).setAutoExpand(true);
		putProtocol(code, buffer, prefixLength);

		buffer.putPrefixedString(message, prefixLength, Profile.UTF8.newEncoder());
		buffer.flip();

		out.write(buffer);
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

}
