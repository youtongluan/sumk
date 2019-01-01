package org.test;

import org.test.soa.server.SOAServer;
import org.yx.log.Log;
import org.yx.main.SumkServer;

public class Main {
	public static void main(String[] args) {
		try {
			Log.get(Main.class).info("为了测试方便，测试环境内置了zookeeper服务器。");
			Log.get(Main.class).info("现在开始启动内置zookeeper。。。");
			SOAServer.startZKServer();
			Log.get(Main.class).info("zookeeper启动完成，现在开始启动真正的sumk服务器。。。");
			long begin=System.currentTimeMillis();
			SumkServer.start();
			System.out.println("启动完成,除zookeeper服务器外耗时："+(System.currentTimeMillis()-begin)+"毫秒");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
