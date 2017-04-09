/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ZKConst;
import org.yx.rpc.client.route.IntfInfo;
import org.yx.rpc.server.ReqHandlerFactorysBean;
import org.yx.rpc.server.ServerListener;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;
import org.yx.util.ZkClientHolder;

/**
 * 启动服务器端
 * 
 * @author 游夏
 *
 */
public class SOAServer implements Plugin {

	private boolean started = false;
	private final int port;
	private ServerListener server;
	private String zkUrl;
	private String path;

	public SOAServer(int port) {
		super();
		this.port = port;
	}

	public synchronized void start() {
		if (started) {
			return;
		}
		try {
			String ip = AppInfo.get("soa.host");

			ip = AppInfo.get("soa.zk.host", ip);
			if (StringUtils.isEmpty(ip) || "0.0.0.0".equals(ip)) {
				ip = AppInfo.getIp();
			}
			path = ZKConst.SOA_ROOT + "/" + ip + ":" + port;
			zkUrl = AppInfo.getZKUrl();
			ZkClient client = ZkClientHolder.getZkClient(zkUrl);
			ZkClientHolder.makeSure(client, ZKConst.SOA_ROOT);

			startServer(ip, port);

			client.delete(path);
			IZkStateListener stateListener = new IZkStateListener() {

				@Override
				public void handleStateChanged(KeeperState state) throws Exception {
					Log.get("sumk.rpc").info("zk state changed:{}", state);
				}

				@Override
				public void handleNewSession() throws Exception {
					client.createEphemeral(path, createZkRouteData());
				}

				@Override
				public void handleSessionEstablishmentError(Throwable error) throws Exception {
					Log.get("sumk.rpc").error("SessionEstablishmentError#" + error.getMessage(), error);
				}

			};
			client.createEphemeral(path, createZkRouteData());
			client.subscribeStateChanges(stateListener);
			started = true;
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

	private void startServer(String ip, int port) throws Exception {

		server = new ServerListener(ip, port, IOC.get(ReqHandlerFactorysBean.class).create());
		server.run();
	}

	private String createZkRouteData() {
		Set<String> methodSet = ActionHolder.soaSet();
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
		Log.get("sumk.SOA").debug("server zk data:\n{}", zkData);
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
}
