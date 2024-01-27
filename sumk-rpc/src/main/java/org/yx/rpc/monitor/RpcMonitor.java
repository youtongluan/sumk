package org.yx.rpc.monitor;

import static org.yx.common.monitor.Monitors.BLANK;
import static org.yx.conf.AppInfo.LN;

import org.yx.common.monitor.MessageProvider;
import org.yx.rpc.client.route.RpcRoutes;
import org.yx.rpc.context.RpcActions;
import org.yx.rpc.data.RouteInfo;
import org.yx.util.SumkDate;

public class RpcMonitor implements MessageProvider {

	private String rpcData() {
		RpcRoutes route = RpcRoutes.current();
		StringBuilder sb = new StringBuilder(64).append("##rpcData").append(BLANK)
				.append(SumkDate.of(route.createTime()));
		for (RouteInfo info : route.zkDatas()) {
			sb.append(LN).append(info.path()).append(BLANK).append(info.host()).append(BLANK).append(info.apis());
		}
		return sb.toString();
	}

	@Override
	public Object get(String type, String key, Object param) {
		if ("document".equals(type)) {
			if ("rpc-full".equals(key)) {
				return RpcActions.infos(true);
			}
			if ("rpc-simple".equals(key)) {
				return RpcActions.infos(false);
			}
			return null;
		}
		if ("rpcData".equals(key)) {
			return rpcData();
		}
		return null;
	}
}
