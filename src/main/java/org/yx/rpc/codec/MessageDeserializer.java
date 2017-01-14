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
