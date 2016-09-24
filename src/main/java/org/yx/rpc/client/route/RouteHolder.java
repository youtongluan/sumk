package org.yx.rpc.client.route;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.rpc.Url;
import org.yx.rpc.ZkData;

public class RouteHolder {
	private static Map<String, WeightedRoute> routes = new ConcurrentHashMap<>();
	private static Map<Url, ZkData> zkDatas = new ConcurrentHashMap<>();

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
	public static void init(Map<String, WeightedRoute> route, Map<Url, ZkData> data) {
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

	public static void removeServer(String method, Url url) {
		WeightedRoute r = routes.get(method);
		if (r == null) {
			return;
		}
		r.removeServer(url);
	}

	public static void addZkData(Url url, ZkData data) {
		zkDatas.put(url, data);
	}

	public static ZkData removeZkData(Url url) {
		return zkDatas.remove(url);
	}
}
