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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.rpc.Host;

public class RouteHolder {
	private static Map<String, WeightedRoute> routes = new ConcurrentHashMap<>();
	private static Map<Host, ZkData> zkDatas = new ConcurrentHashMap<>();

	public static WeightedRoute getRoute(String method) {

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

	public static void putRoute(String method, WeightedRoute r) {
		routes.put(method, r);
	}

	public static void putRoutes(Map<String, WeightedRoute> map) {
		routes.putAll(map);
	}

	/**
	 * 初始化数据。会用新数据代替旧的
	 * 
	 * @param route
	 * @param data
	 */
	public static void init(Map<String, WeightedRoute> route, Map<Host, ZkData> data) {
		routes.clear();
		zkDatas.clear();
		zkDatas.putAll(data);
		routes.putAll(route);
	}

	public static void addServer(String method, ServerMachine server) {
		WeightedRoute r = routes.get(method);
		if (r == null) {
			r = new WeightedRoute(server);
			routes.put(method, r);
			return;
		}
		r.addServer(server);
	}

	public static void removeServer(String method, Host url) {
		WeightedRoute r = routes.get(method);
		if (r == null) {
			return;
		}
		r.removeServer(url);
	}

	public static void addZkData(Host url, ZkData data) {
		zkDatas.put(url, data);
	}

	public static ZkData removeZkData(Host url) {
		return zkDatas.remove(url);
	}
}
