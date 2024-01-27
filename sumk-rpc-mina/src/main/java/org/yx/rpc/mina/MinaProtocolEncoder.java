package org.yx.rpc.mina;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.yx.rpc.codec.CodecKit;

public class MinaProtocolEncoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message == null) {
			return;
		}
		MinaDataBuffer buf = new MinaDataBuffer(createIoBuffer(2048));
		CodecKit.encode(message, buf);
		IoBuffer ioBuf = buf.buffer();
		if (ioBuf.remaining() > 0) {
			out.write(ioBuf);
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

	private static IoBuffer createIoBuffer(int strLength) {
		return IoBuffer.allocate(strLength).setAutoExpand(true).order(ByteOrder.BIG_ENDIAN);
	}
}
