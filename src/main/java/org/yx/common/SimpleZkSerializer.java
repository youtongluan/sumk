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
package org.yx.common;

import java.nio.charset.Charset;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.yx.util.S;

public class SimpleZkSerializer implements ZkSerializer {

	private Charset charset;

	public SimpleZkSerializer(Charset charset) {
		this.charset = charset;
	}

	@Override
	public byte[] serialize(Object data) throws ZkMarshallingError {
		if (data == null) {
			return null;
		}
		if (byte[].class.isInstance(data)) {
			return (byte[]) data;
		}
		if (String.class.isInstance(data)) {
			return ((String) data).getBytes(charset);
		}
		return S.json.toJson(data).getBytes(charset);
	}

	@Override
	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		return bytes;
	}

}
