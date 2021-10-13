package org.yx.rpc.netty;

import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.rpc.RpcSettings;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;

public final class NettyKit {
	public static void configServer(ServerBootstrap b) {
		b.option(ChannelOption.SO_BACKLOG, AppInfo.getInt("sumk.rpc.backlog", 128))
				.option(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false))
				.childOption(ChannelOption.ALLOCATOR, getByteBufAllocator())
				.childOption(ChannelOption.TCP_NODELAY, AppInfo.getBoolean("sumk.rpc.tcp.nodelay", true))
				.childOption(ChannelOption.SO_REUSEADDR, AppInfo.getBoolean("sumk.rpc.port.reuse", false));
		NettyConfigSetter setter = IOC.getFirstBean(NettyConfigSetter.class, true);
		if (setter != null) {
			setter.configServer(b);
		}
	}

	public static void configClient(Bootstrap b) {
		b.option(ChannelOption.SO_KEEPALIVE, AppInfo.getBoolean("sumk.rpc.keepalive", true))
				.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.ALLOCATOR, getByteBufAllocator())
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, RpcSettings.clientDefaultTimeout());
		NettyConfigSetter setter = IOC.getFirstBean(NettyConfigSetter.class, true);
		if (setter != null) {
			setter.configClient(b);
		}
	}

	private static ByteBufAllocator getByteBufAllocator() {

		if (AppInfo.getBoolean("sumk.rpc.memory.pool", true)) {
			return PooledByteBufAllocator.DEFAULT;
		}
		return UnpooledByteBufAllocator.DEFAULT;
	}
}
