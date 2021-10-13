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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.yx.common.Host;
import org.yx.common.Lifecycle;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.main.StartContext;
import org.yx.rpc.Profile;
import org.yx.rpc.RpcActions;
import org.yx.rpc.ZKConst;
import org.yx.rpc.data.ZkDataOperators;
import org.yx.rpc.transport.TransportServer;
import org.yx.rpc.transport.Transports;
import org.yx.util.ZkClientHelper;

public class SoaServer implements Lifecycle {

	private volatile boolean started = false;
	private TransportServer server;
	private String zkUrl;
	private Host host;
	private boolean enable;
	private final String SOA_ROOT;
	private final Logger logger = Log.get("sumk.rpc.server");

	private static boolean soaServerEnable() {
		return AppInfo.getBoolean("sumk.rpc.server.register", true);
	}

	private final IZkStateListener stateListener = new IZkStateListener() {
		@Override
		public void handleStateChanged(KeeperState state) throws Exception {
			logger.debug("zk state changed:{}", state);
		}

		@Override
		public void handleNewSession() throws Exception {
			byte[] data = createZkPathData();
			ZkClientHelper.getZkClient(zkUrl).createEphemeral(fullPath(), data);
			logger.debug("handleNewSession");
		}

		@Override
		public void handleSessionEstablishmentError(Throwable error) throws Exception {
			logger.error("SessionEstablishmentError#" + error.getMessage(), error);
		}

	};

	private String fullPath() {
		StringBuilder sb = new StringBuilder().append(SOA_ROOT).append('/')
				.append(ZkDataOperators.inst().getName(host));
		return sb.toString();
	}

	private final Runnable zkUnRegister = () -> {
		ZkClient client = ZkClientHelper.getZkClient(zkUrl);
		client.unsubscribeStateChanges(stateListener);
		client.delete(fullPath());
	};

	private final Runnable zkRegister = () -> {
		ZkClient client = ZkClientHelper.getZkClient(zkUrl);
		byte[] data = null;
		try {
			data = createZkPathData();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return;
		}

		zkUnRegister.run();
		client.createEphemeral(fullPath(), data);
		client.subscribeStateChanges(stateListener);
	};

	public SoaServer(int port) {
		this.SOA_ROOT = AppInfo.get("sumk.rpc.zk.root.server", "sumk.rpc.zk.root", ZKConst.SUMK_SOA_ROOT);
		this.init(port);
	}

	protected int startServer(String ip, int port) throws Exception {
		server = Transports.factory().bind(ip, port);
		server.start();
		return server.getPort();
	}

	private byte[] createZkPathData() throws Exception {
		List<String> apis = RpcActions.publishSoaSet();
		final Map<String, String> map = new HashMap<>();
		for (String api : apis) {

			map.put(ZKConst.METHODS + api, AppInfo.get("sumk.rpc.api." + api));
		}
		map.put(ZKConst.FEATURE, Profile.featureInHex());
		map.put(ZKConst.START, String.valueOf(System.currentTimeMillis()));
		map.put(ZKConst.WEIGHT, AppInfo.get("sumk.rpc.weight", "100"));

		return ZkDataOperators.inst().serialize(host, map);
	}

	@Override
	public synchronized void stop() {
		try {
			ZkClient client = ZkClientHelper.remove(zkUrl);
			if (client != null) {
				client.unsubscribeAll();
				client.delete(fullPath());
				client.close();
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		if (this.server != null) {
			try {
				this.server.stop();
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
		started = false;
	}

	@Override
	public synchronized void start() {
		if (started || host == null) {
			return;
		}
		if (this.zkUrl == null) {
			logger.warn("##因为没有配置{},所以就没有将微服务注册到注册中心##", Const.ZK_URL);
			return;
		}
		logger.debug("register zk by addr : {}", host);
		try {
			if (this.enable) {
				this.zkRegister.run();
			} else {
				this.zkUnRegister.run();
			}
			AppInfo.addObserver(info -> {
				if (!SoaServer.this.started) {
					logger.debug("soa server unstarted");
					return;
				}
				boolean serverEnable = soaServerEnable();
				if (serverEnable == enable) {
					return;
				}
				try {
					if (serverEnable) {
						SoaServer.this.zkRegister.run();
						logger.info("soa server enabled");
					} else {
						SoaServer.this.zkUnRegister.run();
						logger.info("soa server disabled!!!");
					}
					enable = serverEnable;
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			});
			started = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new SumkException(-35334546, "soa服务启动失败");
		}

	}

	protected void init(int port) {
		try {
			enable = soaServerEnable();
			String ip = StartContext.soaHost();
			port = startServer(ip, port);

			String ip_zk = StartContext.soaHostInzk();
			if (ip_zk == null) {
				ip_zk = ip;
			}
			int port_zk = StartContext.soaPortInZk();
			if (port_zk < 1) {
				port_zk = port;
			}

			this.host = Host.create(ip_zk, port_zk);
			zkUrl = AppInfo.getServerZKUrl();
			if (zkUrl != null) {
				ZkClient client = ZkClientHelper.getZkClient(zkUrl);
				ZkClientHelper.makeSure(client, SOA_ROOT);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new SumkException(-353451436, "soa服务初始化失败");
		}

	}
}
