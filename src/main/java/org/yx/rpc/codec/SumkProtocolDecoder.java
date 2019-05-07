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
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.yx.annotation.Bean;
import org.yx.annotation.Inject;
import org.yx.conf.Profile;
import org.yx.log.Log;
import org.yx.rpc.codec.decoders.SumkMinaDecoder;

@Bean
public class SumkProtocolDecoder extends CumulativeProtocolDecoder {

	private static final Charset charset = Profile.CHARSET_DEFAULT;

	@Inject
	private SumkMinaDecoder[] decoders;

	private Object deserialize(int protocol, String message) throws ProtocolDecoderException {
		for (SumkMinaDecoder decoder : this.decoders) {
			if (decoder.accept(protocol)) {
				return decoder.decode(protocol, message);
			}
		}
		throw new ProtocolDecoderException("no sumk decoder:" + Integer.toHexString(protocol));
	}

	protected boolean innerDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws CharacterCodingException, ProtocolDecoderException {
		int protocol = in.getInt();
		int prefixLength = 0, maxDataLength = 0;
		if ((protocol & Protocols.ONE) != 0) {
			prefixLength = 1;
			maxDataLength = 0xFF;
		} else if ((protocol & Protocols.TWO) != 0) {
			prefixLength = 2;
			maxDataLength = 0xFFFF;
		} else if ((protocol & Protocols.FOUR) != 0) {
			prefixLength = 4;
			maxDataLength = Protocols.MAX_LENGTH;
		} else {
			throw new ProtocolDecoderException("error byte length protocol," + Integer.toHexString(protocol));
		}
		if (in.prefixedDataAvailable(prefixLength, maxDataLength)) {
			if ((protocol & Protocols.FORMAT_JSON) != 0) {
				String msg = in.getPrefixedString(prefixLength, charset.newDecoder());
				if (msg == null || msg.isEmpty()) {
					return true;
				}
				out.write(deserialize(protocol, msg));
				return true;
			}

			throw new ProtocolDecoderException("error format protocol," + Integer.toHexString(protocol));
		}
		return false;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws ProtocolDecoderException, CharacterCodingException {
		if (in.remaining() < 8) {
			return false;
		}
		int pos = in.position();
		int protocol = in.getInt(pos);
		if ((protocol & 0xFF000000) != Protocols.MAGIC) {
			int position = in.position();
			Log.get("sumk.rpc").trace(in.getString(Profile.CHARSET_DEFAULT.newDecoder()));
			in.position(position);
			throw new ProtocolDecoderException("error magic," + Integer.toHexString(protocol));
		}
		boolean ret = false;
		try {
			ret = this.innerDecode(session, in, out);
		} finally {
			if (!ret) {
				in.position(pos);
			}
		}
		return ret;
	}

}
