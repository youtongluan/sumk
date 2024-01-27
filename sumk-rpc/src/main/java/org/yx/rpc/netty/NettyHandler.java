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

import org.yx.log.Logs;
import org.yx.rpc.BusinessHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyHandler implements ChannelInboundHandler {

	private final BusinessHandler handler;

	public NettyHandler(BusinessHandler handler) {
		this.handler = handler;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Logs.rpc().info("{} : channelActive", ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.handler.closed(NettyChannel.create(ctx.channel()));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		this.handler.received(NettyChannel.create(ctx.channel()), msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) {
			Logs.rpc().info("{} : will close because of idle", ctx);
			ctx.close();
			return;
		}

		if (evt instanceof ChannelInputShutdownEvent) {
			Logs.rpc().info("{} : evict ChannelInputShutdownEvent", ctx);
			ctx.close();
			return;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		this.handler.exceptionCaught(NettyChannel.create(ctx.channel()), cause);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Logs.rpc().debug("{} : handlerAdded", ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Logs.rpc().debug("{} : handlerRemoved", ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		Logs.rpc().debug("{} : channelRegistered", ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Logs.rpc().debug("{} : channelUnregistered", ctx);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		Logs.rpc().debug("{} channelWritabilityChanged", ctx);
	}

}
