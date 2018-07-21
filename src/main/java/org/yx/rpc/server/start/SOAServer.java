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
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.conf.Profile;
import org.yx.db.sql.ItemJoiner;
import org.yx.log.Log;
import org.yx.rpc.RpcActionHolder;
import org.yx.rpc.ZKConst;
import org.yx.rpc.client.route.IntfInfo;
import org.yx.rpc.server.MinaServer;
import org.yx.rpc.server.ReqHandlerFactorysBean;
import org.yx.util.GsonUtil;
import org.yx.util.ZkClientHolder;

public class SOAServer implements Plugin {

	private volatile boolean started = false;
	private final int port;
	private MinaServer server;
	private String zkUrl;
	private String path;
	private boolean enable = soaServerEnable();

	private static boolean soaServerEnable() {
		return AppInfo.getBoolean("soa.server.enable", true);
	}

	private final IZkStateListener stateListener = new IZkStateListener() {
		@Override
		public void handleStateChanged(KeeperState state) throws Exception {
			Log.get("sumk.rpc.zk").debug("zk state changed:{}", state);
		}

		@Override
		public void handleNewSession() throws Exception {
			ZkClientHolder.getZkClient(zkUrl).createEphemeral(path, createZkRouteData());
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
		super();
		this.port = port;
	}

	@Override
	public synchronized void start() {
		if (started) {
			return;
		}
		try {
			String ip = AppInfo.get("soa.host", AppInfo.getIp());

			path = ZKConst.SOA_ROOT + "/" + AppInfo.get("soa.zk.host", ip) + ":"
					+ AppInfo.get("soa.zk.port", String.valueOf(port));
			zkUrl = AppInfo.getServerZKUrl();
			ZkClient client = ZkClientHolder.getZkClient(zkUrl);
			ZkClientHolder.makeSure(client, ZKConst.SOA_ROOT);

			startServer(ip, port);
			if (this.enable) {
				this.zkRegister.run();
			} else {
				this.zkUnRegister.run();
			}
			AppInfo.addObserver(new Observer() {

				@Override
				public void update(Observable o, Object arg) {
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
						Log.printStack(e);
					}
				}

			});
			started = true;
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

	private void startServer(String ip, int port) throws Exception {
		server = new MinaServer(ip, port, IOC.get(ReqHandlerFactorysBean.class).create());
		server.run();
	}

	private String createZkRouteData() {
		Set<String> methodSet = RpcActionHolder.soaSet();
		String[] methods = methodSet.toArray(new String[methodSet.size()]);
		final ItemJoiner sj = new ItemJoiner("\n", null, null);
		if (methods.length > 0) {
			ItemJoiner methodJoiner = new ItemJoiner(ZKConst.METHOD_SPLIT, null, null);
			for (String method : methods) {
				String methodJson = AppInfo.get("soa.methods." + method);
				if (methodJson != null && methodJson.startsWith("{")) {
					IntfInfo info = GsonUtil.fromJson(methodJson, IntfInfo.class);
					info.setName(method);
					methodJoiner.item().append(GsonUtil.toJson(info));
					continue;
				}

				methodJoiner.item().append(method);
			}
			sj.item().append(ZKConst.METHODS).append('=').append(methodJoiner.toCharSequence());
		}
		sj.item().append(ZKConst.FEATURE).append('=').append(Profile.featureInHex());
		sj.item().append(ZKConst.START).append('=').append(System.currentTimeMillis());
		sj.item().append(ZKConst.WEIGHT).append('=').append(AppInfo.getInt("soa.weight", 100));

		String zkData = sj.toString();
		return zkData;
	}

	@Override
	public synchronized void stop() {
		try {
			ZkClient client = ZkClientHolder.getZkClient(zkUrl);
			client.unsubscribeAll();
			client.delete(path);
			client.close();
		} catch (Exception e) {
		}

		if (this.server != null) {
			try {
				this.server.stop();
			} catch (IOException e) {
				Log.printStack("sumk.rpc", e);
			}
		}
	}

	public class ZKResiter implements Runnable {

		@Override
		public void run() {
			zkUnRegister.run();
			ZkClient client = ZkClientHolder.getZkClient(zkUrl);
			String zkData = createZkRouteData();
			client.createEphemeral(path, zkData);
			client.subscribeStateChanges(stateListener);
			Log.get("sumk.rpc.zk").trace("server zk data:\n{}", zkData);
		}

	}

	public class ZKUnResiter implements Runnable {

		@Override
		public void run() {
			ZkClient client = ZkClientHolder.getZkClient(zkUrl);
			client.unsubscribeStateChanges(stateListener);
			client.delete(path);
		}

	}
}
