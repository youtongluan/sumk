package org.test.web.demo;

import org.yx.log.Log;
import org.yx.log.MybatisLog;
import org.yx.main.Bootstrap;

public class TestServer {
	public static void main(String[] args) {
		try {
			MybatisLog.enableMybatisLog();
			System.setProperty("nosoa", "true");
			Bootstrap.main(null);
			System.out.println("启动完成");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
