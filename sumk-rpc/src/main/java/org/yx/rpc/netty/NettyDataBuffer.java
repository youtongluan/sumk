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
package org.yx.rpc.netty;

import java.nio.charset.StandardCharsets;

import org.yx.common.codec.DataStream;
import org.yx.exception.SumkException;
import org.yx.rpc.codec.AbstractDataBuffer;

import io.netty.buffer.ByteBuf;

public class NettyDataBuffer extends AbstractDataBuffer {

	public static final int READ = 0;
	public static final int WRITE = 1;
	private final ByteBuf buf;
	private int mode;

	public NettyDataBuffer(ByteBuf buf, int mode) {
		this.buf = buf;
		this.mode = mode;
	}

	@Override
	public void flip() {
		if (mode == READ) {
			this.mode = WRITE;
		} else {
			this.mode = READ;
		}
	}

	@Override
	public void position(int pos) {
		if (mode == READ) {
			buf.readerIndex(pos);
		} else {
			buf.writerIndex(pos);
		}
	}

	@Override
	public int position() {
		return mode == READ ? buf.readerIndex() : buf.writerIndex();
	}

	private void checkMode(int expect) {
		if (mode != expect) {
			throw new SumkException(35435, "mode要为" + expect + "才正确");
		}
	}

	@Override
	public void writePrefixedString(CharSequence s, int lengthBytes) throws Exception {
		this.checkMode(WRITE);
		if (s == null) {
			s = NULL;
		}
		int pos = buf.writerIndex();
		buf.writerIndex(pos + lengthBytes);
		buf.writeCharSequence(s, StandardCharsets.UTF_8);
		int last = buf.writerIndex();
		int len = last - pos - lengthBytes;
		this.buf.writerIndex(pos);
		this.writeInt(len, lengthBytes);
		this.buf.writerIndex(last);
	}

	@Override
	public void read(byte[] dst, int offset, int length) {
		this.checkMode(READ);
		buf.readBytes(dst, offset, length);
	}

	@Override
	public void write(byte[] src, int offset, int length) {
		this.checkMode(WRITE);
		buf.writeBytes(src, offset, length);
	}

	@Override
	public String readPrefixedString(int lengthBytes) throws Exception {
		this.checkMode(READ);
		int len = this.readInt(lengthBytes);
		String s = buf.readCharSequence(len, StandardCharsets.UTF_8).toString();
		if (DataStream.NULL.equals(s)) {
			return null;
		}
		return s;
	}

	@Override
	public boolean avilable(int length) {
		if (this.mode == READ) {
			return buf.isReadable(length);
		}
		return buf.isWritable(length);
	}
}
