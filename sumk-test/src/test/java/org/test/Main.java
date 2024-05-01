package org.test;

import org.yx.log.Log;
import org.yx.main.SumkServer;

public class Main {
	public static void main(String[] args) {
		try {
			// 新版zk不好启动服务端，需要使用外置zk服务器
//			Log.get(Main.class).info("为了测试方便，测试环境内置了zookeeper服务器。");
//			Log.get(Main.class).info("现在开始启动内置zookeeper。。。");
//			SOAServer.startZKServer();
//			Log.get(Main.class).info("zookeeper启动完成，现在开始启动真正的sumk服务器。。。");
			long begin=System.currentTimeMillis();
			SumkServer.start();
			System.out.println("启动耗时："+(System.currentTimeMillis()-begin)+"毫秒");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack("main",e);
		}
	}

}
