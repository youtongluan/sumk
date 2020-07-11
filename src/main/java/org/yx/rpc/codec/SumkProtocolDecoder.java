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

import org.apache.mina.core.buffer.BufferDataException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.yx.annotation.Bean;
import org.yx.log.Logs;
import org.yx.rpc.Profile;

@Bean
public class SumkProtocolDecoder extends CumulativeProtocolDecoder {

	protected boolean innerDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws CharacterCodingException, ProtocolDecoderException {
		int protocol = in.getInt();
		if ((protocol & 0xFF000000) != Protocols.MAGIC) {
			if (Logs.rpc().isTraceEnabled()) {
				Logs.rpc().trace(in.getString(Profile.UTF8.newDecoder()));
			}
			throw new ProtocolDecoderException("error magic," + Integer.toHexString(protocol));
		}
		int prefixLength = 4, maxDataLength = Protocols.MAX_LENGTH;

		if (in.remaining() < prefixLength) {
			return false;
		}
		int dataSize = in.getInt();
		if (dataSize < 0 || dataSize > maxDataLength) {
			throw new BufferDataException("dataLength: " + dataSize);
		}
		if (in.remaining() < dataSize) {
			return false;
		}

		byte[] bs = new byte[dataSize];
		in.get(bs);
		out.write(new ProtocolObject(protocol, bs));
		return true;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws ProtocolDecoderException, CharacterCodingException {
		if (in.remaining() < 5) {
			return false;
		}
		final int pos = in.position();
		if (!this.innerDecode(session, in, out)) {
			in.position(pos);
			return false;
		}
		return true;
	}

}
