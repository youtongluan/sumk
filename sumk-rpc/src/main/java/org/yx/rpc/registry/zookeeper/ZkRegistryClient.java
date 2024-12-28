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
package org.yx.rpc.registry.zookeeper;

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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.yx.base.matcher.BooleanMatcher;
import org.yx.base.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.rpc.data.RouteDataOperators;
import org.yx.rpc.data.RouteInfo;
import org.yx.rpc.data.RoutePathData;
import org.yx.rpc.registry.RegistryConst;
import org.yx.rpc.registry.client.RegistryClient;
import org.yx.rpc.registry.client.RouteEvent;
import org.yx.rpc.registry.client.RpcRoutes;
import org.yx.util.StringUtil;
import org.yx.util.SumkThreadPool;

public class ZkRegistryClient implements RegistryClient {
	private final String zkUrl;
	private Set<String> childs = Collections.emptySet();
	private final Predicate<String> matcher;
	private final String SOA_ROOT = AppInfo.get("sumk.rpc.zk.root.client", "sumk.rpc.zk.root",
			RegistryConst.SUMK_SOA_ROOT);
	private Logger logger = Log.get("sumk.rpc.client");
	private final ThreadPoolExecutor executor;
	private final BlockingQueue<RouteEvent> queue = new LinkedBlockingQueue<>();

	public ZkRegistryClient(String zkUrl) {
		this.zkUrl = zkUrl;

		this.matcher = Matchers.includeAndExclude(AppInfo.getLatin("sumk.rpc.server.include", "*"),
				AppInfo.getLatin("sumk.rpc.server.exclude", null));

		executor = new ThreadPoolExecutor(1, 1, 5000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000),
				SumkThreadPool.createThreadFactory("rpc-client-"), new ThreadPoolExecutor.DiscardPolicy());
		executor.allowCoreThreadTimeOut(true);
	}

	@Override
	public void watch() throws IOException {
		if (StringUtil.isEmpty(this.zkUrl)) {
			return;
		}
		List<RouteInfo> datas = new ArrayList<>();
		ZkClient zk = ZkClientHelper.getZkClient(zkUrl);
		ZkClientHelper.makeSure(zk, SOA_ROOT);

		final IZkDataListener nodeListener = new IZkDataListener() {
			ZkRegistryClient parser = ZkRegistryClient.this;

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				logger.trace("{} node changed", dataPath);
				int index = dataPath.lastIndexOf("/");
				if (index > 0) {
					dataPath = dataPath.substring(index + 1);
				}
				RouteInfo d = RouteDataOperators.inst().deserialize(new RoutePathData(dataPath, (byte[]) data));
				if (d == null || d.apis().isEmpty()) {
					logger.debug("{} has no interface or is invalid node", dataPath);
					parser.handle(RouteEvent.deleteEvent(dataPath));
					return;
				}
				parser.handle(RouteEvent.modifyEvent(dataPath, d));
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {

			}

		};

		List<String> paths = zk.subscribeChildChanges(SOA_ROOT, new IZkChildListener() {
			ZkRegistryClient parser = ZkRegistryClient.this;

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
					logger.trace("{} node created", create);
					RouteInfo d = parser.getZkNodeData(zkClient, create);
					if (d == null) {
						continue;
					}
					parser.handle(RouteEvent.createEvent(create, d));
					zk.subscribeDataChanges(parentPath + "/" + create, nodeListener);
				}

				for (String delete : deleteChilds) {
					logger.trace("{} node deleted", delete);
					parser.handle(RouteEvent.deleteEvent(delete));
					zk.unsubscribeDataChanges(parentPath + "/" + delete, nodeListener);
				}
			}
		});
		if (paths == null) {
			paths = Collections.emptyList();
		}
		paths = filter(paths);
		if (logger.isDebugEnabled()) {
			logger.debug("valid rpc servers: {}", paths);
		}
		this.childs = new HashSet<>(paths);
		for (String path : paths) {
			RouteInfo d = getZkNodeData(zk, path);
			if (d == null) {
				continue;
			}
			zk.subscribeDataChanges(SOA_ROOT + "/" + path, nodeListener);
			datas.add(d);
		}
		RpcRoutes.refresh(datas);
	}

	private List<String> filter(List<String> currentChilds) {
		if (this.matcher == BooleanMatcher.TRUE) {
			return currentChilds;
		}
		List<String> ips = new ArrayList<>(currentChilds.size());
		for (String ip : currentChilds) {
			if (this.matcher.test(ip)) {
				ips.add(ip);
			}
		}
		return ips;
	}

	private RouteInfo getZkNodeData(ZkClient zk, String path) {
		byte[] data = zk.readData(SOA_ROOT + "/" + path);
		try {
			return RouteDataOperators.inst().deserialize(new RoutePathData(path, data));
		} catch (Exception e) {
			logger.error("解析" + path + "的zk数据失败", e);
			return null;
		}
	}

	public void handle(RouteEvent event) {
		if (event == null) {
			return;
		}
		queue.offer(event);
		this.executor.execute(() -> {
			if (queue.isEmpty()) {
				return;
			}
			synchronized (ZkRegistryClient.this) {
				List<RouteEvent> list = new ArrayList<>();
				queue.drainTo(list);
				if (list.isEmpty()) {
					return;
				}
				Map<String, RouteInfo> map = new HashMap<>();
				for (RouteInfo info : RpcRoutes.current().zkDatas()) {
					map.put(info.path(), info);
				}
				if (handleData(map, list) > 0) {
					RpcRoutes.refresh(map.values());
				}
			}
		});
	}

	private int handleData(Map<String, RouteInfo> data, List<RouteEvent> list) {
		int count = 0;
		for (RouteEvent event : list) {
			if (event == null) {
				continue;
			}
			switch (event.getType()) {
			case CREATE:
			case MODIFY:
				if (logger.isDebugEnabled()) {
					logger.debug("{}: {} {}", count, event.getType(), event.getNodeName());
					if (logger.isTraceEnabled()) {
						logger.trace("event的接口列表：{}", event.getRoute().apis());
					}
				}
				data.put(event.getNodeName(), event.getRoute());
				count++;
				break;
			case DELETE:

				if (data.remove(event.getNodeName()) != null) {
					logger.debug("{}: {} {}", count, event.getType(), event.getNodeName());
					count++;
					break;
				}
				logger.info("{}: {}已经被移除了", count, event.getNodeName());
				break;
			default:
				break;
			}
		}
		return count;
	}

	@Override
	public void stop() {

	}

}
