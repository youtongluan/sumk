package org.yx.rpc.registry;

import java.util.Optional;

import org.yx.base.Ordered;
import org.yx.rpc.registry.client.RegistryClient;
import org.yx.rpc.registry.server.RegistryServer;

/**
 * 微服务注册中心。注册中心的地址由实现类决定，但是写入到注册中心的内容不受注册中心实现类的影响
 *
 */
public interface RegistryFactory extends Ordered {

	Optional<RegistryServer> registryServer();

	Optional<RegistryClient> registryClient();
}
