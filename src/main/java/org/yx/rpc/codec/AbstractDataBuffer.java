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

import org.yx.rpc.transport.DataBuffer;

public abstract class AbstractDataBuffer implements DataBuffer {

	@Override
	public void writeBytes(byte[] bs) {
		this.write(bs, 0, bs.length);
	}

	@Override
	public void writeInt(int x, int bytes) {
		switch (bytes) {
		case 1:
			if (x > 0xFF) {
				throw new IllegalArgumentException(x + "无法序列化到1个字节里");
			}
			this.writeBytes(new byte[] { (byte) x });
			return;
		case 2:
			if (x > 0xFFFF) {
				throw new IllegalArgumentException(x + "无法序列化到2个字节里");
			}
			this.writeBytes(new byte[] { (byte) (x >> 8), (byte) x });
			return;
		case 3:
			if (x > 0xFFFFFF) {
				throw new IllegalArgumentException(x + "无法序列化到3个字节里");
			}
			this.writeBytes(new byte[] { (byte) (x >> 16), (byte) (x >> 8), (byte) x });
			return;
		case 4:
			this.writeBytes(new byte[] { (byte) (x >> 24), (byte) (x >> 16), (byte) (x >> 8), (byte) x });
			return;
		}
		throw new IllegalArgumentException("bytes必须在1-4之间");
	}

	@Override
	public int readInt(int bytes) {
		if (bytes > 4 || bytes <= 0) {
			throw new IllegalArgumentException("bytes必须在1-4之间");
		}
		byte[] bs = new byte[4];
		this.read(bs, 4 - bytes, bytes);
		return (bs[0] << 24) | ((bs[1] & 0xff) << 16) | ((bs[2] & 0xff) << 8) | ((bs[3] & 0xff));

	}

}
