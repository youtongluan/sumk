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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.yx.bean.IOC;
import org.yx.common.Lifecycle;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.conf.Profile;
import org.yx.log.Log;
import org.yx.rpc.RpcActionHolder;
import org.yx.rpc.ZKConst;
import org.yx.rpc.server.MinaServer;
import org.yx.rpc.server.RequestHandler;
import org.yx.util.CollectionUtil;
import org.yx.util.ZkClientHelper;

public class SOAServer implements Lifecycle {

	private volatile boolean started = false;
	private MinaServer server;
	private String zkUrl;
	private String path;
	private boolean enable;

	private static boolean soaServerEnable() {
		return AppInfo.getBoolean("sumk.rpc.server.register", true);
	}

	private final IZkStateListener stateListener = new IZkStateListener() {
		@Override
		public void handleStateChanged(KeeperState state) throws Exception {
			Log.get("sumk.rpc.zk").debug("zk state changed:{}", state);
		}

		@Override
		public void handleNewSession() throws Exception {
			ZkClientHelper.getZkClient(zkUrl).createEphemeral(path, createZkRouteData());
			Log.get("sumk.rpc.zk").debug("handleNewSession");
		}

		@Override
		public void handleSessionEstablishmentError(Throwable error) throws Exception {
			Log.get("sumk.rpc.zk").error("SessionEstablishmentError#" + error.getMessage(), error);
		}

	};
	private final Runnable zkRegister = new ZKResiter();
	private final Runnable zkUnRegister = new ZKUnResiter();

	public SOAServer(int port) {
		this.init(port);
	}

	protected int startServer(String ip, int port) throws Exception {
		List<RequestHandler> handlers = IOC.getBeans(RequestHandler.class);
		server = new MinaServer(ip, port, handlers);
		server.run();
		return server.getPort();
	}

	private String createZkRouteData() {
		Collection<String> methodSet = RpcActionHolder.publishSoaSet();
		String[] methods = methodSet.toArray(new String[methodSet.size()]);
		final Map<String, String> map = new HashMap<>();
		if (methods.length > 0) {
			for (String method : methods) {
				map.put(ZKConst.METHODS + "." + method, AppInfo.get("sumk.rpc.methods." + method));
			}
		}
		map.put(ZKConst.FEATURE, Profile.featureInHex());
		map.put(ZKConst.START, String.valueOf(System.currentTimeMillis()));
		map.put(ZKConst.WEIGHT, AppInfo.get("sumk.rpc.weight", "100"));
		map.put(ZKConst.CLIENT_COUNT, AppInfo.get("sumk.rpc.client.count", "1000"));

		String zkData = CollectionUtil.saveMapToText(map, "\n", "=");
		return zkData;
	}

	@Override
	public synchronized void stop() {
		try {
			ZkClient client = ZkClientHelper.remove(zkUrl);
			if (client != null) {
				client.unsubscribeAll();
				client.delete(path);
				client.close();
			}
		} catch (Exception e) {
		}

		if (this.server != null) {
			try {
				this.server.stop();
			} catch (IOException e) {
				Log.printStack("sumk.rpc", e);
			}
		}
		started = false;
	}

	public class ZKResiter implements Runnable {

		@Override
		public void run() {
			zkUnRegister.run();
			ZkClient client = ZkClientHelper.getZkClient(zkUrl);
			String zkData = createZkRouteData();
			client.createEphemeral(path, zkData);
			client.subscribeStateChanges(stateListener);
			Log.get("sumk.rpc.zk").debug("server zk data:\n{}", zkData);
		}

	}

	public class ZKUnResiter implements Runnable {

		@Override
		public void run() {
			ZkClient client = ZkClientHelper.getZkClient(zkUrl);
			client.unsubscribeStateChanges(stateListener);
			client.delete(path);
		}

	}

	@Override
	public synchronized void start() {
		if (started || path == null) {
			return;
		}
		try {
			if (this.enable) {
				this.zkRegister.run();
			} else {
				this.zkUnRegister.run();
			}
			AppInfo.addObserver(info -> {
				if (!SOAServer.this.started) {
					Log.get("sumk.rpc.zk").debug("soa server unstarted");
					return;
				}
				boolean serverEnable = soaServerEnable();
				if (serverEnable == enable) {
					return;
				}
				try {
					if (serverEnable) {
						SOAServer.this.zkRegister.run();
						Log.get("sumk.rpc").info("soa server enabled");
					} else {
						SOAServer.this.zkUnRegister.run();
						Log.get("sumk.rpc").info("soa server disabled!!!");
					}
					enable = serverEnable;
				} catch (Exception e) {
					Log.printStack("sumk.error", e);
				}
			});
			started = true;
		} catch (Exception e) {
			Log.printStack("sumk.error", e);
			System.exit(-1);
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

			path = ZKConst.SOA_ROOT + "/" + ip_zk + ":" + port_zk;
			zkUrl = AppInfo.getServerZKUrl();
			ZkClient client = ZkClientHelper.getZkClient(zkUrl);
			ZkClientHelper.makeSure(client, ZKConst.SOA_ROOT);
		} catch (Exception e) {
			Log.get("sumk.rpc").error(e.toString(), e);
			System.exit(-1);
		}

	}
}
