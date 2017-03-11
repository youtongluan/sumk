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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yx.rpc.Host;

public class Routes {
	private final Map<String, WeightedRoute> routes;
	private final Map<Host, ZkData> zkDatas;

	private Routes(Map<Host, ZkData> zkDatas, Map<String, WeightedRoute> routes) {
		super();
		this.zkDatas = zkDatas;
		this.routes = routes;
	}

	private static volatile Routes ROUTE = new Routes(Collections.emptyMap(), Collections.emptyMap());

	public static WeightedRoute getRoute(String method) {
		Map<String, WeightedRoute> routes = ROUTE.routes;

		for (;; method = method.substring(0, method.lastIndexOf("."))) {
			WeightedRoute r = routes.get(method);
			if (r != null) {
				return r;
			}
			if (!method.contains(".")) {
				return null;
			}
		}
	}

	/**
	 * 初始化数据。会用新数据代替旧的
	 * 
	 * @param route
	 * @param data
	 */
	private static void _refresh(Map<Host, ZkData> data, Map<String, WeightedRoute> route) {
		Routes r = new Routes(data, route);
		Routes.ROUTE = r;
	}

	static void handle(RouteEvent event) {
		Map<Host, ZkData> data = new HashMap<>(ROUTE.zkDatas);
		switch (event.getType()) {
		case CREATE:
		case MODIFY:
			data.put(event.getUrl(), event.getZkData());
			break;
		case DELETE:

			if (data.remove(event.getUrl()) == null) {
				return;
			}
			break;
		default:
		}
		refresh(data);
	}

	public static synchronized void refresh(Map<Host, ZkData> datas) {
		Map<String, Set<ServerMachine>> map = new HashMap<>();
		for (Host url : datas.keySet()) {
			Map<String, ServerMachine> ms = createServerMachine(url, datas.get(url));
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
		_refresh(datas, routes);
	}

	/**
	 * 返回方法和路由的键值对，如果方法为空，返回空map。
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	private static Map<String, ServerMachine> createServerMachine(Host url, ZkData data) {
		Map<String, ServerMachine> servers = new HashMap<>();
		int weight = data.weight > 0 ? data.weight : 5;
		data.getIntfs().forEach(intf -> {
			ServerMachine server = new ServerMachine(url, weight);
			servers.put(intf.getIntf(), server);
		});
		return servers;
	}

}
