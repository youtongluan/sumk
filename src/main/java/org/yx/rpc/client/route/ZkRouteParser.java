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
package org.yx.rpc.client.route;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.yx.main.SumkServer;
import org.yx.rpc.Host;
import org.yx.rpc.ZKConst;
import org.yx.util.CollectionUtils;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;
import org.yx.util.ZkClientHolder;

public class ZkRouteParser {
	private String zkUrl;
	private Set<String> childs = Collections.emptySet();

	private ZkRouteParser(String zkUrl) {
		this.zkUrl = zkUrl;
	}

	public static ZkRouteParser get(String zkUrl) {
		return new ZkRouteParser(zkUrl);
	}

	private BlockingQueue<RouteEvent> queue = new LinkedBlockingQueue<>();

	/**
	 * 根据zk的数据，初始化Routes
	 * 
	 * @param zkUrl
	 * @throws IOException
	 */
	public void readRouteAndListen() throws IOException {
		Map<Host, ZkData> datas = new HashMap<>();
		ZkClient zk = ZkClientHolder.getZkClient(zkUrl);

		final IZkDataListener nodeListener = new IZkDataListener() {
			ZkRouteParser parser = ZkRouteParser.this;

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				ServerData d = parser.buildZkNodeData(dataPath, ZkClientHolder.data2String((byte[]) data));
				if (d == null) {
					parser.handle(RouteEvent.delete(Host.create(dataPath)));
					return;
				}
				parser.handle(RouteEvent.create(d.url, d.data));
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {

			}

		};

		List<String> paths = zk.subscribeChildChanges(ZKConst.SOA_ROOT, new IZkChildListener() {
			ZkRouteParser parser = ZkRouteParser.this;

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				List<String> createChilds = new ArrayList<>();
				Set<String> deleteChilds = new HashSet<>(parser.childs);
				for (String zkChild : currentChilds) {
					boolean exist = deleteChilds.remove(zkChild);

					if (!exist) {
						createChilds.add(zkChild);
					}
				}
				parser.childs = new HashSet<>(currentChilds);
				for (String create : createChilds) {
					ServerData d = parser.getZkNodeData(create);
					if (d == null) {
						continue;
					}
					parser.handle(RouteEvent.create(d.url, d.data));
					zk.subscribeDataChanges(parentPath + "/" + create, nodeListener);
				}

				for (String delete : deleteChilds) {
					parser.handle(RouteEvent.delete(Host.create(delete)));
					zk.unsubscribeDataChanges(parentPath + "/" + delete, nodeListener);
				}
			}

		});
		this.childs = new HashSet<>(paths);
		for (String path : paths) {
			ServerData d = getZkNodeData(path);
			zk.subscribeDataChanges(ZKConst.SOA_ROOT + "/" + path, nodeListener);
			if (d == null) {
				continue;
			}
			datas.put(d.url, d.data);
		}
		Routes.refresh(datas);
		SumkServer.runDeamon(() -> {
			RouteEvent event = queue.take();
			if (event == null) {
				return;
			}
			Routes.handle(event);
		}, "rpc-client-route");
	}

	private ServerData getZkNodeData(String path) throws IOException {
		ZkClient zk = ZkClientHolder.getZkClient(zkUrl);
		String json = ZkClientHolder.data2String(zk.readData(ZKConst.SOA_ROOT + "/" + path));
		return buildZkNodeData(path, json);
	}

	private ServerData buildZkNodeData(String path, String json) throws IOException {
		if (path.contains("/")) {
			path = path.substring(path.lastIndexOf("/") + 1);
		}
		Map<String, String> map = CollectionUtils.loadMap(new StringReader(json));
		String methods = map.get(ZKConst.METHODS);
		if (StringUtils.isEmpty(methods)) {
			return null;
		}
		ZkData data = new ZkData();
		data.setWeight(map.get(ZKConst.WEIGHT));
		Arrays.stream(methods.split(ZKConst.METHOD_SPLIT)).forEach(m -> {
			if (m.length() == 0) {
				return;
			}
			if (m.startsWith("{")) {
				IntfInfo intf = GsonUtil.fromJson(m, IntfInfo.class);
				data.addIntf(intf);
				return;
			}
			IntfInfo intf = new IntfInfo();
			intf.setName(m);
			data.addIntf(intf);
		});
		return new ServerData(Host.create(path), data);
	}

	public void handle(RouteEvent event) {
		if (event == null) {
			return;
		}
		queue.offer(event);
	}

	private static class ServerData {
		final Host url;
		final ZkData data;

		private ServerData(Host url, ZkData data) {
			super();
			this.url = url;
			this.data = data;
		}
	}

}
