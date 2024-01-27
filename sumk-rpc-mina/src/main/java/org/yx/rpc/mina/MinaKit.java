package org.yx.rpc.mina;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.yx.conf.AppInfo;
import org.yx.conf.SimpleBeanUtil;
import org.yx.log.Logs;
import org.yx.rpc.RpcSettings;

public final class MinaKit {

	public static void config(SocketSessionConfig conf, boolean server) {

		long maxIdle = server ? RpcSettings.maxServerIdleTime() : RpcSettings.maxClientIdleTime();
		int maxIdleSecond = (int) (maxIdle / 1000);
		Logs.rpc().debug("max idel time for server:{} is {} second", server, maxIdleSecond);
		conf.setIdleTime(IdleStatus.BOTH_IDLE, maxIdleSecond);
		Map<String, String> map = new HashMap<>(AppInfo.subMap("sumk.rpc.conf."));
		String selfKey = server ? "sumk.rpc.server.conf." : "sumk.rpc.client.conf.";
		map.putAll(AppInfo.subMap(selfKey));
		if (map.isEmpty()) {
			return;
		}
		String flag = server ? "server" : "client";
		Logs.rpc().info(flag + " session config: {}", map);
		try {
			SimpleBeanUtil.copyProperties(conf, map);
		} catch (Exception e) {
			Logs.rpc().warn(flag + " rpc config error. " + e.getMessage(), e);
		}
	}
}
