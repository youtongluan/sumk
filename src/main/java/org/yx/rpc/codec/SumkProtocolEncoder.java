/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderException;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.yx.conf.Profile;
import org.yx.log.Log;
import org.yx.rpc.client.Req;
import org.yx.rpc.server.Response;
import org.yx.util.GsonUtil;

public class SumkProtocolEncoder implements ProtocolEncoder {

	private Charset charset = Profile.CHARSET_DEFAULT;

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message == null) {
			return;
		}
		if (Req.class.isInstance(message)) {
			this.encodeReq(session, (Req) message, out);
		} else if (Response.class.isInstance(message)) {
			this.encodeResponse(session, (Response) message, out);
		} else if (String.class.isInstance(message)) {
			this.encodeString(0, session, (String) message, out);
		} else {
			throw new ProtocolEncoderException(message.getClass().getName() + " not support in ProtocolEncoder");
		}
	}

	private void encodeResponse(IoSession session, Response message, ProtocolEncoderOutput out)
			throws CharacterCodingException, ProtocolEncoderException {
		this.encodeString(Protocols.RESPONSE_JSON, session, GsonUtil.toJson(message), out);
	}

	/**
	 * 
	 * @param buffer
	 * @param prefixLength
	 * @throws ProtocolEncoderException
	 */
	private void putProtocol(int code, IoBuffer buffer, int prefixLength) throws ProtocolEncoderException {
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

	private void encodeReq(IoSession session, Req req, ProtocolEncoderOutput out)
			throws CharacterCodingException, ProtocolEncoderException {
		String jsonedArg = req.getJsonedParam();
		String[] params = req.getParamArray();
		req.clearParams();

		if (jsonedArg != null) {
			Log.get("SYS.RPC").trace("args:{}", jsonedArg);
			String json_req = GsonUtil.toJson(req) + Protocols.LINE_SPLIT + jsonedArg;
			this.encodeString(Protocols.REQ_PARAM_JSON, session, json_req, out);
			return;
		}

		String json_req = String.format("%02d", params.length) + GsonUtil.toJson(req);
		for (String p : params) {
			json_req += Protocols.LINE_SPLIT + p;
		}
		this.encodeString(Protocols.REQ_PARAM_ORDER, session, json_req, out);
	}

	private void encodeString(int code, IoSession session, String message, ProtocolEncoderOutput out)
			throws CharacterCodingException, ProtocolEncoderException {
		code = code | Protocols.FORMAT_JSON;
		int size = message.length();
		int prefixLength = size <= (Protocols.MAX_ONE / 3) ? 1 : size <= (Protocols.MAX_TWO / 3) ? 2 : 4;

		IoBuffer buffer = IoBuffer.allocate((int) (size * 1.2) + 10).setAutoExpand(true);
		putProtocol(code, buffer, prefixLength);

		buffer.putPrefixedString(message, prefixLength, charset.newEncoder());
		buffer.flip();

		out.write(buffer);
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

}
