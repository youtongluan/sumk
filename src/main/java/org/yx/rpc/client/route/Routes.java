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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.yx.common.Host;
import org.yx.log.Log;

public final class Routes {
	private final Map<String, RpcRoute> rpcRoutes;
	private final Map<Host, ZkData> zkDatas;

	private Routes(Map<Host, ZkData> zkDatas, Map<String, RpcRoute> routes) {
		this.zkDatas = Objects.requireNonNull(zkDatas);
		this.rpcRoutes = Objects.requireNonNull(routes);
	}

	private static volatile Routes ROUTE = new Routes(Collections.emptyMap(), Collections.emptyMap());

	public static Map<Host, ZkData> currentDatas() {
		return Collections.unmodifiableMap(ROUTE.zkDatas);
	}

	public static RpcRoute getRoute(String api) {
		return ROUTE.rpcRoutes.get(api);
	}

	public static int routeSize() {
		return ROUTE.rpcRoutes.size();
	}

	private static void _refresh(Map<Host, ZkData> data, Map<String, RpcRoute> route) {
		Routes r = new Routes(data, route);
		Routes.ROUTE = r;
		if (Log.get("sumk.rpc.zk").isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("微服务源:");
			for (Host host : data.keySet()) {
				sb.append("  ").append(host.toString());
			}
			Log.get("sumk.rpc.zk").debug(sb.toString());
		}
	}

	public static synchronized void refresh(Map<Host, ZkData> datas) {
		Map<String, Set<ServerMachine>> map = new HashMap<>();
		datas.forEach((url, data) -> {
			Map<String, ServerMachine> ms = createServerMachine(url, data);
			ms.forEach((m, serverMachine) -> {
				Set<ServerMachine> server = map.get(m);
				if (server == null) {
					server = new HashSet<>();
					map.put(m, server);
				}
				server.add(serverMachine);
			});
		});
		Map<String, RpcRoute> routes = new HashMap<>();
		map.forEach((method, servers) -> {
			if (servers == null || servers.isEmpty()) {
				return;
			}
			routes.put(method, new RpcRoute(servers));
		});
		_refresh(datas, routes);
	}

	private static Map<String, ServerMachine> createServerMachine(Host url, ZkData data) {
		Map<String, ServerMachine> servers = new HashMap<>();
		int weight = data.weight > 0 ? data.weight : 100;
		data.getIntfs().forEach(intf -> {
			ServerMachine server = new ServerMachine(url, weight);
			servers.put(intf.getName(), server);
		});
		return servers;
	}

}
