package org.test;

import org.yx.log.Log;
import org.yx.main.SumkServer;

public class Main {
	public static void main(String[] args) {
		try {
			Log.get(Main.class).info("需要在外部启动一个zookeeper服务器");
			long begin=System.currentTimeMillis();
			SumkServer.start(Main.class,null);
			System.out.println("启动耗时："+(System.currentTimeMillis()-begin)+"毫秒");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack("main",e);
		}
	}

}
