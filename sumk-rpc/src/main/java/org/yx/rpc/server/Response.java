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
package org.yx.rpc.server;

import java.io.Serializable;

import org.yx.common.util.S;
import org.yx.exception.SoaException;
import org.yx.exception.SumkException;
import org.yx.rpc.codec.CodecKit;
import org.yx.rpc.codec.DataStream;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.StreamAble;

public class Response implements StreamAble, Serializable {

	private static final long serialVersionUID = 1L;
	private static final byte BODY_TYPE_JSON = 1;
	private static final byte BODY_TYPE_EXCEPTION = 2;

	private String sn;
	private String json;
	private SoaException exception;

	private long _ms = -1;

	private int _clientProtocol;

	public String sn() {
		return sn;
	}

	public String json() {
		return json;
	}

	public long serviceInvokeMilTime() {
		return _ms;
	}

	public void sn(String sn) {
		this.sn = sn;
	}

	public void json(String json) {
		this.json = json;
	}

	public void serviceInvokeMilTime(long ms) {
		this._ms = ms;
	}

	public Response() {
	}

	public Response(String sn) {
		this.sn = sn;
	}

	public SoaException exception() {
		return exception;
	}

	public void exception(SoaException exception) {
		this.exception = exception;
	}

	public boolean isSuccess() {
		return this.exception == null;
	}

	public int getClientProtocol() {
		return _clientProtocol;
	}

	public void setClientProtocol(int clientAcceptedProtocol) {
		this._clientProtocol = clientAcceptedProtocol;
	}

	public void writeTo(DataStream s) throws Exception {
		s.writeInt(1, 1);
		s.writePrefixedString(this.sn, 1);

		s.writeInt(0, 1);
		if (this.exception != null) {
			s.writeInt(BODY_TYPE_EXCEPTION, 1);
			String strException = S.json().toJson(exception);
			s.writePrefixedString(strException, 4);
			return;
		}

		s.writeInt(BODY_TYPE_JSON, 1);
		s.writePrefixedString(json, 4);

	}

	public void readFrom(DataStream s) throws Exception {

		int size = s.readInt(1);
		this.sn = s.readPrefixedString(1);
		CodecKit.skipPrefixedString(s, size - 1);

		size = s.readInt(1);
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				s.readPrefixedString(1);
				s.readPrefixedString(2);
			}
		}

		int type = s.readInt(1);
		switch (type) {
		case BODY_TYPE_JSON:
			this.json = s.readPrefixedString(4);
			break;
		case BODY_TYPE_EXCEPTION:
			String ex = s.readPrefixedString(4);
			this.exception = S.json().fromJson(ex, SoaException.class);
			break;
		default:
			throw new SumkException(45432239, "不支持resp的body类型：" + type);
		}
	}

	@Override
	public int getMessageType() {
		return Protocols.RESPONSE;
	}
}
