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
import org.yx.rpc.data.ApiInfo;
import org.yx.rpc.data.RouteInfo;
import org.yx.util.CollectionUtil;

public final class RpcRoutes {

	private final Map<String, Router<Host>> rpcRoutes;

	private final List<RouteInfo> zkDatas;
	private final Map<Host, Integer> protocols;
	private final long createTime;

	private RpcRoutes(Collection<RouteInfo> zkDatas, Map<String, Router<Host>> routes) {
		this.zkDatas = CollectionUtil.unmodifyList(Objects.requireNonNull(zkDatas));
		this.rpcRoutes = Objects.requireNonNull(routes);
		Map<Host, Integer> p = new HashMap<>();
		for (RouteInfo info : zkDatas) {
			p.put(info.host(), info.feature());
		}
		this.protocols = p;
		this.createTime = System.currentTimeMillis();
	}

	private static volatile RpcRoutes ROUTE = new RpcRoutes(Collections.emptyList(), Collections.emptyMap());

	public static Set<Host> servers() {
		return new HashSet<>(ROUTE.protocols.keySet());
	}

	public static int getServerProtocol(Host url) {
		Integer p = ROUTE.protocols.get(url);
		if (p == null) {
			return 0;
		}
		return p.intValue();
	}

	public static Router<Host> getRoute(String api) {
		return ROUTE.rpcRoutes.get(api);
	}

	public static int routeSize() {
		return ROUTE.rpcRoutes.size();
	}

	private static void _refresh(Collection<RouteInfo> rawData, Map<String, Router<Host>> route) {
		RpcRoutes r = new RpcRoutes(rawData, route);
		RpcRoutes.ROUTE = r;
		if (Log.get("sumk.rpc.client").isTraceEnabled()) {
			StringBuilder sb = new StringBuilder("微服务源:");
			for (RouteInfo d : rawData) {
				sb.append("  ").append(d.host());
			}
			Log.get("sumk.rpc.client").trace(sb.toString());
		}
	}

	private static void fillWeightedServer(Map<String, WeightedServer<Host>> source,
			Map<String, Set<WeightedServer<Host>>> dest) {
		for (Map.Entry<String, WeightedServer<Host>> entry : source.entrySet()) {
			String m = entry.getKey();
			WeightedServer<Host> serverMachine = entry.getValue();
			Set<WeightedServer<Host>> server = dest.get(m);
			if (server == null) {
				server = new HashSet<>();
				dest.put(m, server);
			}
			server.add(serverMachine);
		}
	}

	public static synchronized void refresh(Collection<RouteInfo> datas) {
		Map<String, Set<WeightedServer<Host>>> map = new HashMap<>();
		for (RouteInfo r : datas) {
			fillWeightedServer(createServerMachine(r), map);
		}
		Map<String, Router<Host>> routes = new HashMap<>();
		for (Map.Entry<String, Set<WeightedServer<Host>>> entry : map.entrySet()) {
			String method = entry.getKey();
			Set<WeightedServer<Host>> servers = entry.getValue();
			if (servers == null || servers.isEmpty()) {
				continue;
			}
			Router<Host> route = RouterHolder.createRouter(method, servers);
			if (route != null) {
				routes.put(method, route);
			}
		}
		_refresh(datas, routes);
	}

	private static Map<String, WeightedServer<Host>> createServerMachine(RouteInfo data) {
		Map<String, WeightedServer<Host>> servers = new HashMap<>();
		int weight = data.weight() > 0 ? data.weight() : 100;
		for (ApiInfo intf : data.apis()) {
			WeightedServer<Host> server = new WeightedHost(data.host(), weight);
			servers.put(intf.getName(), server);
		}
		return servers;
	}

	public static RpcRoutes current() {
		return ROUTE;
	}

	public Map<String, Router<Host>> routes() {
		return CollectionUtil.unmodifyMap(this.rpcRoutes);
	}

	public List<RouteInfo> zkDatas() {
		return this.zkDatas;
	}

	public long createTime() {
		return this.createTime;
	}

}
