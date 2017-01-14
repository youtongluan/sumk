package org.test;

import org.test.soa.server.SOAServer;
import org.yx.log.Log;
import org.yx.main.SumkServer;

public class Main {
	public static void main(String[] args) {
		try {
			SOAServer.startZKServer();
			SumkServer.start();
			System.out.println("启动完成");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
