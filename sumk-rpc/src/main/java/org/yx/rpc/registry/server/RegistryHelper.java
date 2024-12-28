package org.yx.rpc.registry.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.rpc.Profile;
import org.yx.rpc.context.RpcActions;
import org.yx.rpc.data.RouteDataOperators;
import org.yx.rpc.registry.RegistryConst;

public class RegistryHelper {

	public static String soaRoot() {
		return AppInfo.get("sumk.rpc.zk.root.server", "sumk.rpc.zk.root", RegistryConst.SUMK_SOA_ROOT);
	}

	public static String fullPath(final Host host) {
		StringBuilder sb = new StringBuilder().append(soaRoot()).append('/')
				.append(RouteDataOperators.inst().getName(host));
		return sb.toString();
	}

	public static byte[] routeData(final Host host) throws Exception {
		List<String> apis = RpcActions.publishSoaSet();
		final Map<String, String> map = new HashMap<>();
		for (String api : apis) {

			map.put(RegistryConst.METHODS + api, AppInfo.get("sumk.rpc.api." + api));
		}
		map.put(RegistryConst.FEATURE, Profile.featureInHex());
		map.put(RegistryConst.START, String.valueOf(System.currentTimeMillis()));
		map.put(RegistryConst.WEIGHT, AppInfo.get("sumk.rpc.weight", "100"));

		return RouteDataOperators.inst().serialize(host, map);
	}

	public static boolean needRegisterServer() {
		return AppInfo.getBoolean("sumk.rpc.server.register", true);
	}
}
