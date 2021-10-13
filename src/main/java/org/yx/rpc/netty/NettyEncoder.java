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

import org.yx.rpc.codec.CodecKit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.PlatformDependent;

public class NettyEncoder extends MessageToByteEncoder<Object> {

	public NettyEncoder() {
		super(PlatformDependent.directBufferPreferred());
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) throws Exception {
		if (message == null) {
			return;
		}
		NettyDataBuffer buf = new NettyDataBuffer(out, NettyDataBuffer.WRITE);
		CodecKit.encode(message, buf);
	}

}
