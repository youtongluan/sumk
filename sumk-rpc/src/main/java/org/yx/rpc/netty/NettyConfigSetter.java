package org.yx.rpc.netty;

import org.yx.base.Ordered;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;

public interface NettyConfigSetter extends Ordered {
	void configServer(ServerBootstrap b);

	void configClient(Bootstrap b);
}
