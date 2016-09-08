package org.test.web.demo;

import org.yx.http.start.HttpStarter;
import org.yx.log.Log;
import org.yx.main.Bootstrap;

public class TestServer {
	public static void main(String[] args) {
		try {
			Log.setDefaultLevel(Log.TRACE);
			System.setProperty("nosoa","true");
			HttpStarter.setLoginServlet("/login", new MyLoginServlet());
			Bootstrap.main(new String[]{});
			System.out.println("启动完成");
			Thread.currentThread().join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
