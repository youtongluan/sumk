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
package org.yx.rpc.server.start;

import java.util.Optional;

import org.slf4j.Logger;
import org.yx.base.Lifecycle;
import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.main.SumkServer;
import org.yx.rpc.registry.RegistryFactory;
import org.yx.rpc.registry.server.RegistryServer;
import org.yx.rpc.transport.TransportServer;
import org.yx.rpc.transport.Transports;

public class SoaServer implements Lifecycle {

	private volatile boolean started = false;
	private final Logger logger = Log.get("sumk.rpc.server");
	private TransportServer server;
	private final Optional<RegistryServer> registry;
	private Host listenHost;

	public SoaServer(RegistryFactory registryFactory) {
		this.registry = registryFactory.registryServer();
	}

	protected Host startServer() throws Exception {
		String ip = SumkServer.soaHost();
		int port = SumkServer.soaPort();
		server = Transports.factory().bind(ip, port);
		server.start();
		return Host.create(ip, server.getPort());
	}

	@Override
	public synchronized void stop() {
		if (this.registry.isPresent()) {
			try {
				registry.get().stop();
			} catch (Exception e) {
				Logs.rpc().error("注册中心停止失败", e);
			}
		}

		if (this.server != null) {
			try {
				this.server.stop();
			} catch (Exception e) {
				logger.error("rpc的网络服务器停止失败", e);
			}
		}
		started = false;
	}

	@Override
	public synchronized void start() {
		if (started) {
			return;
		}
		try {
			this.listenHost = startServer();
		} catch (Exception e1) {
			logger.error("soa端口监听失败", e1);
			throw new SumkException(35334545, "soa服务启动失败");
		}
		if (this.registry.isPresent()) {
			try {
				this.registry.get().start(getHostInRegistry());
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
				throw new SumkException(35334546, "soa服务启动失败");
			}
		} else {
			logger.warn("注册中心没有配置，所以没有发起注册");
		}

		started = true;
	}

	/**
	 * 获取写到注册中心的地址
	 * 
	 * @return 写到注册中心的地址
	 */
	protected Host getHostInRegistry() {
		String ip_zk = soaHostInRegistry();
		if (ip_zk == null) {
			ip_zk = listenHost.ip();
		}
		int port_zk = soaPortInRegistry();
		if (port_zk < 1) {
			port_zk = listenHost.port();
		}

		return Host.create(ip_zk, port_zk);

	}

	private String soaHostInRegistry() {
		return AppInfo.get("sumk.rpc.registry.host", null);
	}

	private int soaPortInRegistry() {
		return AppInfo.getInt("sumk.rpc.registry.port", -1);
	}
}
