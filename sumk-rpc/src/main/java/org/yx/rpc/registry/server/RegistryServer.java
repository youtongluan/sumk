package org.yx.rpc.registry.server;

import org.yx.base.Ordered;
import org.yx.common.Host;

/**
 * 微服务注册中心
 *
 */
public interface RegistryServer extends Ordered {

	/**
	 * 开启微服务对注册中心的注册及变更监听
	 * 
	 * @param serverHost 注册中心里的本机地址
	 */
	void start(Host serverHost);

	void stop();
}
