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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.yx.common.Host;
import org.yx.common.route.Router;
import org.yx.common.route.WeightedServer;
import org.yx.log.Log;
import org.yx.rpc.data.RouteInfo;

public final class RpcRoutes {
	private final Map<String, Router<Host>> rpcRoutes;
	private final List<RouteInfo> zkDatas;

	private RpcRoutes(List<RouteInfo> zkDatas, Map<String, Router<Host>> routes) {
		this.zkDatas = Objects.requireNonNull(zkDatas);
		this.rpcRoutes = Objects.requireNonNull(routes);
	}

	private static volatile RpcRoutes ROUTE = new RpcRoutes(Collections.emptyList(), Collections.emptyMap());

	public static List<RouteInfo> currentDatas() {
		return Collections.unmodifiableList(ROUTE.zkDatas);
	}

	public static Router<Host> getRoute(String api) {
		return ROUTE.rpcRoutes.get(api);
	}

	public static int routeSize() {
		return ROUTE.rpcRoutes.size();
	}

	private static void _refresh(Collection<RouteInfo> rawData, Map<String, Router<Host>> route) {
		List<RouteInfo> data = new ArrayList<>(rawData);
		RpcRoutes r = new RpcRoutes(data, route);
		RpcRoutes.ROUTE = r;
		if (Log.get("sumk.rpc.client").isTraceEnabled()) {
			StringBuilder sb = new StringBuilder("微服务源:");
			for (RouteInfo d : data) {
				sb.append("  ").append(d.host());
			}
			Log.get("sumk.rpc.client").trace(sb.toString());
		}
	}

	public static synchronized void refresh(Collection<RouteInfo> datas) {
		Map<String, Set<WeightedServer<Host>>> map = new HashMap<>();
		for (RouteInfo r : datas) {
			Map<String, WeightedServer<Host>> ms = createServerMachine(r);
			ms.forEach((m, serverMachine) -> {
				Set<WeightedServer<Host>> server = map.get(m);
				if (server == null) {
					server = new HashSet<>();
					map.put(m, server);
				}
				server.add(serverMachine);
			});
		}
		Map<String, Router<Host>> routes = new HashMap<>();
		map.forEach((method, servers) -> {
			if (servers == null || servers.isEmpty()) {
				return;
			}
			Router<Host> route = RouteHolder.createRouter(method, servers);
			if (route != null) {
				routes.put(method, route);
			}
		});
		_refresh(datas, routes);
	}

	private static Map<String, WeightedServer<Host>> createServerMachine(RouteInfo data) {
		Map<String, WeightedServer<Host>> servers = new HashMap<>();
		int weight = data.weight() > 0 ? data.weight() : 100;
		data.intfs().forEach(intf -> {
			WeightedServer<Host> server = new WeightedHost(data.host(), weight);
			servers.put(intf.getName(), server);
		});
		return servers;
	}

}
