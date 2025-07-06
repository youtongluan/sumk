package org.test;

import org.junit.Test;
import org.slf4j.spi.LocationAwareLogger;
import org.yx.log.Log;
import org.yx.util.SumkThreadPool;

public class Starter {

	@Test
	public void test() {
		Log.get(this.getClass()).info("{} 只是个测试类",this.getClass().getSimpleName());
		org.apache.log4j.Logger.getLogger(this.getClass()).info("这是log4j的测试类");
		org.apache.logging.log4j.LogManager.getLogger(this.getClass()).warn("这是log4j 2.x的测试类");
		org.apache.logging.log4j.LogManager.getLogger(this.getClass()).error("如果使用log4j 2，请引入log4j-to-slf4j");
		ProxyLog.log("{}是{}的测试类","这个",LocationAwareLogger.class);
		ProxyLog.error("异常消息",new Exception("用于测试的异常"));
		
		Log.get(this.getClass()).atWarn().addArgument("李四").addKeyValue("key1", "marker1")
			.setMessage("name:{}").log();
		SumkThreadPool.shutdown();
	}
	
}
