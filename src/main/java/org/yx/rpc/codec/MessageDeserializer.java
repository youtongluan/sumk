package org.yx.rpc.codec;

import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.yx.rpc.server.Response;
import org.yx.util.GsonUtil;

public class MessageDeserializer {
	private Request byJsonParam(int protocol, String message) throws ProtocolDecoderException {
		String[] msgs = message.split(Protocols.LINE_SPLIT, -1);
		if (msgs.length < 2) {
			throw new ProtocolDecoderException("error jsoned param req");
		}
		Request req = GsonUtil.fromJson(msgs[0], Request.class);
		req.setJsonedParam(msgs[1]);
		req.protocol = protocol;
		return req;
	}

	public Object deserialize(int protocol, String message) throws ProtocolDecoderException {
		if (Protocols.hasFeature(protocol, Protocols.REQ_PARAM_JSON)) {
			return this.byJsonParam(protocol, message);
		}
		if (Protocols.hasFeature(protocol, Protocols.REQ_PARAM_ORDER)) {
			return this.byOrderedParam(protocol, message);
		}
		if (Protocols.hasFeature(protocol, Protocols.RESPONSE_JSON)) {
			return this.parseResponse(protocol, message);
		}
		throw new ProtocolDecoderException("error req protocol:" + Integer.toHexString(protocol));
	}

	private Response parseResponse(int protocol, String message) {
		return GsonUtil.fromJson(message, Response.class);
	}

	private Request byOrderedParam(int protocol, String message) throws ProtocolDecoderException {
		String argLength = message.substring(0, 2);
		message = message.substring(2);
		String[] msgs = message.split(Protocols.LINE_SPLIT, -1);
		if (msgs.length < 2) {
			throw new ProtocolDecoderException("error ordered param req");
		}
		Request req = GsonUtil.fromJson(msgs[0], Request.class);
		int len = Integer.parseInt(argLength);
		if (len > 0) {
			String[] params = new String[len];
			for (int i = 0; i < len; i++) {
				params[i] = msgs[i + 1];
			}
			req.setParamArray(params);
		}
		req.protocol = protocol;
		return req;
	}
}
