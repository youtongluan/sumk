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
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.yx.common.Host;
import org.yx.common.matcher.MatcherFactory;
import org.yx.conf.AppInfo;
import org.yx.conf.NamePairs;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.rpc.ZKConst;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;
import org.yx.util.ZkClientHelper;

public class ZkRouteParser {
	private final String zkUrl;
	private Set<String> childs = Collections.emptySet();
	private final Predicate<String> includes;
	private final Predicate<String> excludes;
	private final String SOA_ROOT = AppInfo.get("sumk.rpc.zk.route", "sumk.rpc.client.zk.route", ZKConst.SUMK_SOA_ROOT);
	private Logger logger = Log.get("sumk.rpc.zk");

	private ZkRouteParser(String zkUrl) {
		this.zkUrl = zkUrl;
		String temp = AppInfo.getLatin("sumk.rpc.server.includes");
		includes = StringUtil.isEmpty(temp) ? null : MatcherFactory.createWildcardMatcher(temp, 1);

		temp = AppInfo.getLatin("sumk.rpc.server.excludes");
		excludes = StringUtil.isEmpty(temp) ? null : MatcherFactory.createWildcardMatcher(temp, 1);
	}

	public static ZkRouteParser get(String zkUrl) {
		return new ZkRouteParser(zkUrl);
	}

	private BlockingQueue<RouteEvent> queue = new LinkedBlockingQueue<>();

	public void readRouteAndListen() throws IOException {
		Map<Host, ZkData> datas = new HashMap<>();
		ZkClient zk = ZkClientHelper.getZkClient(zkUrl);
		ZkClientHelper.makeSure(zk, SOA_ROOT);

		final IZkDataListener nodeListener = new IZkDataListener() {
			ZkRouteParser parser = ZkRouteParser.this;

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				ServerData d = parser.buildZkNodeData(dataPath, ZkClientHelper.data2String((byte[]) data));
				if (d == null) {
					parser.handle(RouteEvent.deleteEvent(Host.create(dataPath)));
					return;
				}
				parser.handle(RouteEvent.modifyEvent(d.url, d.data));
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {

			}

		};

		List<String> paths = zk.subscribeChildChanges(SOA_ROOT, new IZkChildListener() {
			ZkRouteParser parser = ZkRouteParser.this;

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				if (currentChilds == null) {
					currentChilds = Collections.emptyList();
				}
				List<String> ips = filter(currentChilds);

				List<String> createChilds = new ArrayList<>();
				Set<String> deleteChilds = new HashSet<>(parser.childs);
				for (String zkChild : ips) {
					boolean exist = deleteChilds.remove(zkChild);

					if (!exist) {
						createChilds.add(zkChild);
					}
				}
				ZkClient zkClient = ZkClientHelper.getZkClient(zkUrl);
				parser.childs = new HashSet<>(ips);
				for (String create : createChilds) {
					ServerData d = parser.getZkNodeData(zkClient, create);
					if (d == null) {
						continue;
					}
					parser.handle(RouteEvent.createEvent(d.url, d.data));
					zk.subscribeDataChanges(parentPath + "/" + create, nodeListener);
				}

				for (String delete : deleteChilds) {
					parser.handle(RouteEvent.deleteEvent(Host.create(delete)));
					zk.unsubscribeDataChanges(parentPath + "/" + delete, nodeListener);
				}
			}

		});
		if (paths == null) {
			paths = Collections.emptyList();
		}
		paths = filter(paths);
		this.childs = new HashSet<>(paths);
		for (String path : paths) {
			ServerData d = getZkNodeData(zk, path);
			if (d == null) {
				continue;
			}
			zk.subscribeDataChanges(SOA_ROOT + "/" + path, nodeListener);
			datas.put(d.url, d.data);
		}
		Routes.refresh(datas);
		SumkThreadPool.loop(() -> {
			RouteEvent event = queue.take();
			if (event == null) {
				return true;
			}
			List<RouteEvent> list = new ArrayList<>(10);
			list.add(event);
			for (int i = 0; i < 8; i++) {
				event = queue.poll(20, TimeUnit.MILLISECONDS);
				if (event == null) {
					break;
				}
				list.add(event);
			}
			Map<Host, ZkData> data = new HashMap<>(Routes.currentDatas());
			if (handleData(data, list) > 0) {
				Routes.refresh(data);
			}
			return true;
		}, "rpc-client-route");
	}

	private List<String> filter(List<String> currentChilds) {

		if (includes != null) {
			List<String> ips = new ArrayList<>();
			for (String ip : currentChilds) {
				if (includes.test(ip)) {
					ips.add(ip);
				}
			}
			return ips;
		}

		if (excludes != null) {
			List<String> ips = new ArrayList<>();
			for (String ip : currentChilds) {
				if (!excludes.test(ip)) {
					ips.add(ip);
				}
			}
			return ips;
		}

		return currentChilds;
	}

	private ServerData getZkNodeData(ZkClient zk, String path) throws IOException {
		String json = ZkClientHelper.data2String(zk.readData(SOA_ROOT + "/" + path));
		return buildZkNodeData(path, json);
	}

	private ServerData buildZkNodeData(String path, String json) throws IOException {
		if (path.contains("/")) {
			path = path.substring(path.lastIndexOf("/") + 1);
		}
		Host host = Host.create(path);
		if (host == null) {
			Log.get("sumk.rpc.zk").warn("{} 不是有效的host", path);
			return null;
		}
		Map<String, String> map = NamePairs.createByString(json).values();
		Map<String, String> methodMap = CollectionUtil.subMap(map, ZKConst.METHODS + ".");
		if (methodMap.isEmpty()) {
			return null;
		}
		ZkData data = new ZkData();
		data.setWeight(map.get(ZKConst.WEIGHT));
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
			}
		});
		return new ServerData(host, data);
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
			this.url = url;
			this.data = data;
		}
	}

	private int handleData(Map<Host, ZkData> data, List<RouteEvent> list) {
		int count = 0;
		for (RouteEvent event : list) {
			if (logger.isDebugEnabled()) {
				logger.debug("{}: {} {}", count, event.getType(), event.getUrl());
				if (logger.isTraceEnabled() && event.getZkData() != null) {
					logger.trace("接口列表：{}", event.getZkData().getIntfs());
				}
			}
			switch (event.getType()) {
			case CREATE:
			case MODIFY:
				data.put(event.getUrl(), event.getZkData());
				count++;
				break;
			case DELETE:

				if (data.remove(event.getUrl()) != null) {
					count++;
				}
				break;
			default:
				break;
			}
		}
		return count;
	}

}
