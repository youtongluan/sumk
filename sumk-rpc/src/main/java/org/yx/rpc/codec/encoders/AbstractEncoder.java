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
package org.yx.rpc.codec.encoders;

import org.yx.conf.Const;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.transport.DataBuffer;

public abstract class AbstractEncoder<T> implements DataEncoder {

	@Override
	public final boolean encode(Object message, DataBuffer buffer) throws Exception {
		T obj = this.convert(message);
		if (obj == null) {
			return false;
		}
		int start = buffer.position();
		buffer.writeInt(Protocols.MAGIC | Const.SUMK_VERSION | this.getMessageType(obj), 4);
		buffer.position(start + 8);
		this.encodeBody(obj, buffer);
		this.putLength(buffer, start + 4);
		return true;
	}

	protected abstract int getMessageType(T req);

	protected abstract void encodeBody(T message, DataBuffer buffer) throws Exception;

	protected abstract T convert(Object message);

	private void putLength(DataBuffer buffer, int start) {
		int limit = buffer.position();
		buffer.position(start);
		buffer.writeInt(limit - start - 4, 4);
		buffer.position(limit);
	}
}
