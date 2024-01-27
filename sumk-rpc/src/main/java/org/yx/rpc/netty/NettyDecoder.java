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

import java.util.List;

import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.rpc.codec.CodecKit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NettyDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {

			Object message = CodecKit.decode(new NettyDataBuffer(in, NettyDataBuffer.READ));
			if (message == null) {
				return;
			}
			out.add(message);
		} catch (Exception e) {
			Logs.rpc().error("数据解析出错", e);
			throw new SumkException(-346456319, "数据解析出错," + e.getMessage());
		}
	}

}
