package org.yx.rpc.registry.zookeeper;

import java.util.Optional;

import org.yx.annotation.Bean;
import org.yx.base.Ordered;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.rpc.registry.RegistryFactory;
import org.yx.rpc.registry.client.RegistryClient;
import org.yx.rpc.registry.server.RegistryServer;
import org.yx.util.StringUtil;

@Bean
public class ZkRegistryFactory implements RegistryFactory {

	private static final String ZK_URL = "sumk.zkurl";

	@Override
	public int order() {
		return Ordered.DEFAULT_ORDER + 1000;
	}

	@Override
	public Optional<RegistryServer> registryServer() {
		String zkUrl = zkServerUrl();
		if (StringUtil.isEmpty(zkUrl)) {
			Logs.rpc().warn("##因为没有配置{},所以无法注册接口到注册中心##", ZK_URL);
			return Optional.empty();
		}
		return Optional.of(new ZkRegistryServer(zkUrl));
	}

	@Override
	public Optional<RegistryClient> registryClient() {
		String zkUrl = zkClinetUrl();
		if (StringUtil.isEmpty(zkUrl)) {
			Logs.rpc().warn("##因为没有配置{},所以只能调用本机的微服务接口##", ZK_URL);
			return Optional.empty();
		}
		return Optional.of(new ZkRegistryClient(zkUrl));
	}

	public static String zkServerUrl() {
		String url = AppInfo.get("sumk.rpc.zk.server");
		if (url != null && url.length() > 0) {
			return url;
		}
		return AppInfo.get(ZK_URL);
	}

	public static String zkClinetUrl() {
		String url = AppInfo.get("sumk.rpc.zk.client");
		if (url != null && url.length() > 0) {
			return url;
		}
		return AppInfo.get(ZK_URL);
	}

}
