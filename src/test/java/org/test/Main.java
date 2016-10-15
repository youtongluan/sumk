package org.test;

import org.test.soa.server.SOAServer;
import org.yx.log.Log;
import org.yx.log.MybatisLog;
import org.yx.main.Bootstrap;

public class Main {
	public static void main(String[] args) {
		try {
			Log.setDefaultLevel(Log.TRACE);
			MybatisLog.enableMybatisLog();
			SOAServer.startZKServer();
			Bootstrap.main(null);
			System.out.println("启动完成");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
