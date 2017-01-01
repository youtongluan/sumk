package org.test.soa.server;

import org.I0Itec.zkclient.ZkServer;

public class SOAServer {

	public static void startZKServer() {
		String temp = System.getProperty("java.io.tmpdir");
		ZkServer zk = new ZkServer(temp, temp, zkClient -> {
		});
		zk.start();
	}

}
