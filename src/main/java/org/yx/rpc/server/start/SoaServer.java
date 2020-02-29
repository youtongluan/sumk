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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.yx.bean.IOC;
import org.yx.common.Host;
import org.yx.common.Lifecycle;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.rpc.Profile;
import org.yx.rpc.RpcActions;
import org.yx.rpc.ZKConst;
import org.yx.rpc.data.ZkDataOperators;
import org.yx.rpc.server.MinaServer;
import org.yx.rpc.server.RequestHandler;
import org.yx.util.ZkClientHelper;

public class SoaServer implements Lifecycle {

	private volatile boolean started = false;
	private MinaServer server;
	private String zkUrl;
	private Host host;
	private boolean enable;
	private final String SOA_ROOT;
	private Logger logger = Log.get("sumk.rpc.server");

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
		this.SOA_ROOT = AppInfo.get("sumk.rpc.server.route", "sumk.rpc.server.zk.route", ZKConst.SUMK_SOA_ROOT);
		this.init(port);
	}

	protected int startServer(String ip, int port) throws Exception {
		List<RequestHandler> handlers = IOC.getBeans(RequestHandler.class);
		server = new MinaServer(ip, port, handlers);
		server.run();
		return server.getPort();
	}

	private byte[] createZkPathData() throws Exception {
		List<String> methods = RpcActions.publishSoaSet();
		final Map<String, String> map = new HashMap<>();
		for (String method : methods) {

			map.put(ZKConst.METHODS + "." + method, AppInfo.get("sumk.rpc.method." + method));
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
			} catch (IOException e) {
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
			System.exit(1);
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
			logger.debug("register zk by ip:{},port:{}", ip_zk, port_zk);

			this.host = Host.create(ip_zk, port_zk);
			zkUrl = AppInfo.getServerZKUrl();
			ZkClient client = ZkClientHelper.getZkClient(zkUrl);
			ZkClientHelper.makeSure(client, SOA_ROOT);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			System.exit(1);
		}

	}
}
