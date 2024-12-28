package org.yx.rpc.registry.client;

public interface RegistryClient {

	void watch() throws Exception;

	void stop();
}
