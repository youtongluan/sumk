package org.yx.rpc.registry.zookeeper;

import java.util.Objects;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.registry.server.RegistryHelper;
import org.yx.rpc.registry.server.RegistryServer;
import org.yx.util.StringUtil;

public class ZkRegistryServer implements RegistryServer {

	private final Logger logger = Log.get("sumk.rpc.registry.zookeeper");

	private volatile boolean started;

	private Host host;

	private boolean needRegister;

	private final String zkUrl;

	public ZkRegistryServer(String zkUrl) {
		this.zkUrl = zkUrl;
	}

	@Override
	public int order() {
		return Const.DEFAULT_ORDER + 1000;
	}

	private static boolean soaServerEnable() {
		return AppInfo.getBoolean("sumk.rpc.server.register", true);
	}

	@Override
	public synchronized void start(Host serverHost) {
		if (started || host != null || StringUtil.isEmpty(zkUrl)) {
			return;
		}
		this.host = Objects.requireNonNull(serverHost);
		try {
			ZkClient client = zkClient();
			ZkClientHelper.makeSure(client, RegistryHelper.soaRoot());
			this.needRegister = soaServerEnable();
			if (needRegister) {
				this.zkRegister.run();
				logger.info("registe zk at : {}", this.host);
			} else {
				this.zkUnRegister.run();
			}
			AppInfo.addObserver(info -> {
				if (!ZkRegistryServer.this.started) {
					logger.debug("soa server has not started");
					return;
				}
				boolean serverEnable = soaServerEnable();
				if (serverEnable == needRegister) {
					return;
				}
				try {
					if (serverEnable) {
						ZkRegistryServer.this.zkRegister.run();
						logger.info("soa server [{}] enabled", host);
					} else {
						ZkRegistryServer.this.zkUnRegister.run();
						logger.info("soa server [{}] disabled!!!", host);
					}
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			});
			started = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw new SumkException(35334546, "soa服务启动失败");
		}

	}

	private final IZkStateListener stateListener = new IZkStateListener() {
		@Override
		public void handleStateChanged(KeeperState state) throws Exception {
			logger.debug("zk state changed:{}", state);
		}

		@Override
		public void handleNewSession() throws Exception {
			byte[] data = createZkPathData();
			ZkClientHelper.getZkClient(zkUrl).createEphemeral(fullPath(), data);
			logger.debug("handleNewSession");
		}

		@Override
		public void handleSessionEstablishmentError(Throwable error) throws Exception {
			logger.error("SessionEstablishmentError#" + error.getMessage(), error);
		}

	};

	private ZkClient zkClient() {
		return ZkClientHelper.getZkClient(zkUrl);
	}

	private final Runnable zkUnRegister = () -> {
		ZkClient client = zkClient();
		client.unsubscribeStateChanges(stateListener);
		client.delete(fullPath());
	};

	private final Runnable zkRegister = () -> {
		ZkClient client = zkClient();
		byte[] data = null;
		try {
			data = createZkPathData();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return;
		}

		zkUnRegister.run();
		client.createEphemeral(fullPath(), data);
		client.subscribeStateChanges(stateListener);
	};

	@Override
	public synchronized void stop() {
		if (StringUtil.isEmpty(zkUrl)) {
			return;
		}
		try {
			ZkClient client = ZkClientHelper.remove(zkUrl);
			if (client != null) {
				client.unsubscribeAll();
				client.delete(fullPath());
				client.close();
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		started = false;
	}

	private String fullPath() {
		return RegistryHelper.fullPath(host);
	}

	private byte[] createZkPathData() throws Exception {
		return RegistryHelper.routeData(host);
	}

}
