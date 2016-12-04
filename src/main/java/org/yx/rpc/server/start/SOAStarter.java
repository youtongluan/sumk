package org.yx.rpc.server.start;

import java.util.Set;
import java.util.StringJoiner;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.yx.bean.IOC;
import org.yx.common.ServerStarter;
import org.yx.conf.AppInfo;
import org.yx.conf.Profile;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ZKConst;
import org.yx.rpc.ZkClientHolder;
import org.yx.rpc.server.ReqHandlerFactorysBean;
import org.yx.rpc.server.ServerListener;

/**
 * 启动服务器端
 * 
 * @author 游夏
 *
 */
public class SOAStarter implements ServerStarter {
	private boolean started = false;

	public synchronized void start(int port) throws Exception {
		if (started) {
			SumkException.throwException(20000, "soa server has started");
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
				Log.get("SYS.RPC").info("zk state changed:{}", state);
			}

			@Override
			public void handleNewSession() throws Exception {
				client.createEphemeral(path, createZkRouteData());
			}

			@Override
			public void handleSessionEstablishmentError(Throwable error) throws Exception {
				Log.get("SYS.RPC").error("SessionEstablishmentError#" + error.getMessage(), error);
			}

		});
		startServer(AppInfo.getIp(), port);
		started = true;
	}

	private void startServer(String ip, int port) throws Exception {
		ServerListener server=new ServerListener(ip,port,IOC.get(ReqHandlerFactorysBean.class).create());
		Thread t = new Thread(server, "server-listener");
		t.setDaemon(true);
		t.start();
	}

	private String createZkRouteData() {
		Set<String> methods = ActionHolder.soaSet();
		StringJoiner sj = new StringJoiner("\n");
		if (methods != null && methods.size() > 0) {
			sj.add(ZKConst.METHODS + "=" + String.join(",", methods.toArray(new String[methods.size()])));
		}
		sj.add(ZKConst.FEATURE + "=" + Profile.featureInHex());
		return sj.toString();
	}
}
