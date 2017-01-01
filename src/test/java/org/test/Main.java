package org.test;

import org.test.soa.server.SOAServer;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;
import org.yx.log.MybatisLog;
import org.yx.main.SumkServer;

public class Main {
	public static void main(String[] args) {
		try {
			ConsoleLog.setDefaultLevel(ConsoleLog.TRACE);
			MybatisLog.enableMybatisLog();
			SOAServer.startZKServer();
			SumkServer.start();
			System.out.println("启动完成");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
