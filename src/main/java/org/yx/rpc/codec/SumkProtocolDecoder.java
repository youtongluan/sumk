package org.yx.rpc.codec;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.yx.conf.Profile;
import org.yx.log.Log;

public class SumkProtocolDecoder extends CumulativeProtocolDecoder {

	private Charset charset = Profile.CHARSET_DEFAULT;
	private MessageDeserializer msgDeserializer = new MessageDeserializer();

	protected boolean doDecodeString(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws CharacterCodingException, ProtocolDecoderException {
		int pos = in.position();
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
			throw new ProtocolDecoderException("error transport protocol," + Integer.toHexString(protocol));
		}
		if (in.prefixedDataAvailable(prefixLength, maxDataLength)) {
			String msg = in.getPrefixedString(prefixLength, charset.newDecoder());
			if (msg == null || msg.isEmpty()) {
				return true;
			}
			out.write(msgDeserializer.deserialize(protocol, msg));
			return true;
		}
		in.position(pos);
		return false;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws ProtocolDecoderException, CharacterCodingException {
		if (in.remaining() < 4) {
			return false;
		}
		int protocol = in.getInt(in.position());
		if ((protocol & 0xFF000000) != Protocols.MAGIC) {
			int position = in.position();
			Log.get("SYS.RPC").trace(in.getString(Profile.CHARSET_DEFAULT.newDecoder()));
			in.position(position);
			throw new ProtocolDecoderException("error magic," + Integer.toHexString(protocol));
		}
		if ((protocol & Protocols.FORMAT_JSON) != 0) {
			return this.doDecodeString(session, in, out);
		}

		throw new ProtocolDecoderException("error transport protocol," + Integer.toHexString(protocol));
	}

}
