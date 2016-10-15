package org.yx.rpc.server.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.yx.common.ServerStarter;
import org.yx.conf.AppInfo;
import org.yx.exception.SystemException;
import org.yx.log.Log;
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ZKRouteData;
import org.yx.rpc.ZkClientHolder;
import org.yx.rpc.server.MinaHandler;
import org.yx.rpc.server.ServerListener;
import org.yx.rpc.server.impl.DefaultMinaHandler;
import org.yx.rpc.server.impl.JsonArgHandler;
import org.yx.util.GsonUtil;

/**
 * 启动服务器端
 * 
 * @author youtl
 *
 */
public class SOAStarter implements ServerStarter{
	private boolean started = false;

	public synchronized void start(int port) throws Exception {
		if (started) {
			SystemException.throwException(20000, "soa server has started");
		}

		String zkUrl = AppInfo.getZKUrl();
		ZkClient client = ZkClientHolder.getZkClient(zkUrl);
		ZkClientHolder.makeSure(client, ZkClientHolder.SOA_ROOT);
		String path = ZkClientHolder.SOA_ROOT + "/" + AppInfo.getIp() + ":" + port;
		client.delete(path);
		client.createEphemeral(path, createZkRouteData());
		client.unsubscribeStateChanges(new IZkStateListener() {

			@Override
			public void handleStateChanged(KeeperState state) throws Exception {
				Log.get("SYS.46").info("zk state changed:{}", state);
			}

			@Override
			public void handleNewSession() throws Exception {
				client.createEphemeral(path, createZkRouteData());
			}

			@Override
			public void handleSessionEstablishmentError(Throwable error) throws Exception {
				Log.get("SYS.47").error("SessionEstablishmentError#" + error.getMessage(), error);
			}

		});
		startServer(AppInfo.getIp(), port);
		started = true;
	}

	private void startServer(String ip, int port) {
		List<MinaHandler> list = new ArrayList<MinaHandler>();
		list.add(new DefaultMinaHandler());
		list.add(new JsonArgHandler());
		ServerListener server = new ServerListener(ip, port, list);
		Thread t = new Thread(server, "server-listener");
		t.setDaemon(true);
		t.start();
	}

	private String createZkRouteData() {
		Set<String> methods = ActionHolder.soaSet();
		StringBuilder sb = new StringBuilder();
		for (String m : methods) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(m);
		}
		ZKRouteData data = new ZKRouteData();
		data.setMethods(sb.toString());
		return GsonUtil.toJson(data);
	}
}
