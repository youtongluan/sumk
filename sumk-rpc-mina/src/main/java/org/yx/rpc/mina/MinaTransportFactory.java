package org.yx.rpc.mina;

import org.yx.annotation.Bean;
import org.yx.common.Host;
import org.yx.rpc.transport.TransportClient;
import org.yx.rpc.transport.TransportFactory;
import org.yx.rpc.transport.TransportServer;

@Bean
public class MinaTransportFactory implements TransportFactory {

	@Override
	public TransportClient connect(Host serverAddr) {
		return new MinaClient(serverAddr);
	}

	@Override
	public TransportServer bind(String ip, int port) {
		return new MinaServer(ip, port);
	}

	@Override
	public void initClient() {
		MinaClient.connectorSupplier().get();
	}
}
