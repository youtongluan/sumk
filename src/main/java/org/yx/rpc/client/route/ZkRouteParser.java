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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.I0Itec.zkclient.ZkClient;
import org.yx.rpc.Host;
import org.yx.rpc.ZKConst;
import org.yx.rpc.ZkClientHolder;
import org.yx.util.CollectionUtils;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;

public class ZkRouteParser {
	/**
	 * 根据zk的数据，初始化RouteHolder
	 * 
	 * @param zkUrl
	 * @throws IOException
	 */
	public void parse(String zkUrl) throws IOException {
		Map<Host, ZkData> datas = new HashMap<>();
		ZkClient zk = ZkClientHolder.getZkClient(zkUrl);

		List<String> paths = zk.getChildren(ZkClientHolder.SOA_ROOT);
		for (String path : paths) {
			Host url = Host.create(path);
			String json = zk.readData(ZkClientHolder.SOA_ROOT + "/" + path);
			Map<String, String> map = CollectionUtils.loadMap(new StringReader(json));
			String methods = map.get(ZKConst.METHODS);
			if (StringUtils.isEmpty(methods)) {
				continue;
			}
			ZkData data = new ZkData();
			data.setWeight(map.get(ZKConst.WEIGHT));
			Arrays.asList(methods.split(",")).forEach(m -> {
				if (m.startsWith("{")) {
					IntfInfo intf = GsonUtil.fromJson(m, IntfInfo.class);
					data.addIntf(intf);
					return;
				}
				IntfInfo intf = new IntfInfo();
				intf.setIntf(m);
				data.addIntf(intf);
			});
			datas.put(url, data);
		}
		parseRoute(datas);
	}

	private void parseRoute(Map<Host, ZkData> datas) {
		Map<String, Set<ServerMachine>> map = new HashMap<>();
		for (Host url : datas.keySet()) {
			Map<String, ServerMachine> ms = this.createServerMachine(url, datas.get(url));
			for (String m : ms.keySet()) {
				Set<ServerMachine> server = map.get(m);
				if (server == null) {
					server = new HashSet<>();
					map.put(m, server);
				}
				server.add(ms.get(m));
			}
		}
		Map<String, WeightedRoute> routes = new HashMap<>();
		for (String method : map.keySet()) {
			routes.put(method, new WeightedRoute(map.get(method)));
		}
		RouteHolder.init(routes, datas);
	}

	/**
	 * 返回方法和路由的键值对，如果方法为空，返回空map。
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public Map<String, ServerMachine> createServerMachine(Host url, ZkData data) {
		Map<String, ServerMachine> servers = new HashMap<>();
		int weight = data.weight > 0 ? data.weight : 5;
		data.getIntfs().forEach(intf -> {
			ServerMachine server = new ServerMachine(url, weight);
			servers.put(intf.getIntf(), server);
		});
		return servers;
	}
}
