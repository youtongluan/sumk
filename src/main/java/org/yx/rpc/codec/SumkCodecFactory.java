package org.yx.rpc.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class SumkCodecFactory implements ProtocolCodecFactory {

	private static SumkCodecFactory inst = new SumkCodecFactory();

	public static SumkCodecFactory factory() {
		return inst;
	}

	private ProtocolEncoder encoder = new SumkProtocolEncoder();

	private ProtocolDecoder decoder = new SumkProtocolDecoder();

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

}
