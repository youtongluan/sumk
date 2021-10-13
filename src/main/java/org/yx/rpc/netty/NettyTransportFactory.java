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

import java.util.Map;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.rpc.server.RequestHandler;
import org.yx.rpc.transport.TransportClient;
import org.yx.rpc.transport.TransportFactory;
import org.yx.rpc.transport.TransportServer;

@Bean
public class NettyTransportFactory implements TransportFactory {

	public NettyTransportFactory() {
		String prefix = "io.netty.";
		Map<String, String> map = AppInfo.subMap(prefix);
		for (String key : map.keySet()) {
			System.setProperty(prefix + key, map.get(key));
		}
	}

	@Override
	public void initClient() {
		NettyClient.connectorSupplier().get();
	}

	@Override
	public TransportClient connect(Host serverAddr) {
		return new NettyClient(serverAddr);
	}

	@Override
	public TransportServer bind(String ip, int port) {
		return new NettyServer(ip, port, IOC.getBeans(RequestHandler.class));
	}

	@Override
	public int order() {
		return TransportFactory.super.order() + 1000;
	}
}
