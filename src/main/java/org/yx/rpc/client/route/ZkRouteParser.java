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
package org.yx.rpc.client.route;

import java.io.IOException;
import java.util.ArrayList;
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
import org.yx.common.WildcardMatcher;
import org.yx.conf.AppInfo;
import org.yx.conf.NamePairs;
import org.yx.main.SumkThreadPool;
import org.yx.rpc.Host;
import org.yx.rpc.ZKConst;
import org.yx.util.CollectionUtil;
import org.yx.util.S;
import org.yx.util.StringUtil;
import org.yx.util.ZkClientHelper;

public class ZkRouteParser {
	private String zkUrl;
	private Set<String> childs = Collections.emptySet();
	private WildcardMatcher includes;
	private WildcardMatcher excludes;

	private ZkRouteParser(String zkUrl) {
		this.zkUrl = zkUrl;
		String temp = AppInfo.getLatin("soa.server.includes");
		includes = StringUtil.isEmpty(temp) ? null : new WildcardMatcher(temp, 1);

		temp = AppInfo.getLatin("soa.server.excludes");
		excludes = StringUtil.isEmpty(temp) ? null : new WildcardMatcher(temp, 1);
	}

	public static ZkRouteParser get(String zkUrl) {
		return new ZkRouteParser(zkUrl);
	}

	private BlockingQueue<RouteEvent> queue = new LinkedBlockingQueue<>();

	public void readRouteAndListen() throws IOException {
		Map<Host, ZkData> datas = new HashMap<>();
		ZkClient zk = ZkClientHelper.getZkClient(zkUrl);

		final IZkDataListener nodeListener = new IZkDataListener() {
			ZkRouteParser parser = ZkRouteParser.this;

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				ServerData d = parser.buildZkNodeData(dataPath, ZkClientHelper.data2String((byte[]) data));
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
				List<String> ips = filter(currentChilds);

				List<String> createChilds = new ArrayList<>();
				Set<String> deleteChilds = new HashSet<>(parser.childs);
				for (String zkChild : ips) {
					boolean exist = deleteChilds.remove(zkChild);

					if (!exist) {
						createChilds.add(zkChild);
					}
				}
				parser.childs = new HashSet<>(ips);
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
		paths = filter(paths);
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
		SumkThreadPool.runDeamon(() -> {
			RouteEvent event = queue.take();
			if (event == null) {
				return true;
			}
			Routes.handle(event);
			return true;
		}, "rpc-client-route");
	}

	private List<String> filter(List<String> currentChilds) {

		if (includes != null) {
			List<String> ips = S.Collection.list();
			for (String ip : currentChilds) {
				if (includes.match(ip)) {
					ips.add(ip);
				}
			}
			return ips;
		}

		if (excludes != null) {
			List<String> ips = S.Collection.list();
			for (String ip : currentChilds) {
				if (!excludes.match(ip)) {
					ips.add(ip);
				}
			}
			return ips;
		}

		return currentChilds;
	}

	private ServerData getZkNodeData(String path) throws IOException {
		ZkClient zk = ZkClientHelper.getZkClient(zkUrl);
		String json = ZkClientHelper.data2String(zk.readData(ZKConst.SOA_ROOT + "/" + path));
		return buildZkNodeData(path, json);
	}

	private ServerData buildZkNodeData(String path, String json) throws IOException {
		if (path.contains("/")) {
			path = path.substring(path.lastIndexOf("/") + 1);
		}
		Map<String, String> map = NamePairs.createByString(json).values();
		Map<String, String> methodMap = CollectionUtil.subMap(map, ZKConst.METHODS + ".");
		if (methodMap.isEmpty()) {
			return null;
		}
		ZkData data = new ZkData();
		data.setWeight(map.get(ZKConst.WEIGHT));
		data.setClientCount(map.get(ZKConst.CLIENT_COUNT));
		methodMap.forEach((m, value) -> {
			if (m.length() == 0) {
				return;
			}
			IntfInfo intf = new IntfInfo();
			intf.setName(m);
			data.addIntf(intf);
			if (value != null && value.length() > 0) {
				Map<String, String> methodProperties = CollectionUtil.loadMapFromText(value, ",", ":");
				intf.setWeight(methodProperties.get(ZKConst.WEIGHT));
				intf.setClientCount(methodProperties.get(ZKConst.CLIENT_COUNT));
			}
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
