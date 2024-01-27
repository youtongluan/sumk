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

import java.net.InetSocketAddress;

import org.yx.rpc.transport.RpcWriteFuture;
import org.yx.rpc.transport.TransportChannel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class NettyChannel implements TransportChannel {

	private final Channel channel;
	private volatile boolean close;

	private NettyChannel(Channel channel) {
		this.channel = channel;
	}

	public static NettyChannel create(Channel session) {
		Attribute<NettyChannel> attr = session.attr(AttributeKey.valueOf(TransportChannel.class.getName()));
		NettyChannel channel = attr.get();
		if (channel == null) {
			channel = new NettyChannel(session);
			NettyChannel ch2 = attr.setIfAbsent(channel);
			if (ch2 != null) {
				return ch2;
			}
		}
		return channel;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) channel.remoteAddress();
	}

	@Override
	public RpcWriteFuture write(Object message) {
		ChannelFuture future = channel.writeAndFlush(message);
		return new NettyWriteFuture(future);
	}

	@Override
	public boolean isConnected() {
		return (!close) && channel.isActive();
	}

	@Override
	public boolean isClosing() {
		if (close) {
			return true;
		}
		return !channel.isActive() || !channel.isRegistered();
	}

	@Override
	public void closeNow() {
		close = true;
		channel.close();
	}

	@Override
	public void closeOnFlush() {
		this.closeNow();
	}

	@Override
	public Object getAttribute(String key) {
		AttributeKey<Object> KEY = AttributeKey.valueOf(key);
		if (!channel.hasAttr(KEY)) {
			return null;
		}
		return channel.attr(KEY).get();
	}

	@Override
	public void setAttribute(String key, Object value) {
		AttributeKey<Object> KEY = AttributeKey.valueOf(key);
		channel.attr(KEY).set(value);
	}

	@Override
	public void removeAttribute(String key) {
		this.setAttribute(key, null);
	}

	@Override
	public String toString() {
		return String.valueOf(channel);
	}

}
