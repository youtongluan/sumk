package org.test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yx.common.StartConstants;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;
import org.yx.log.LogLevel;
import org.yx.log.LogType;
import org.yx.main.SumkServer;
import org.yx.rpc.client.LockHolder;
import org.yx.rpc.client.Rpc;

public class RpcPressTest {

	@Before
	public void before(){
		SumkServer.start(StartConstants.NOHTTP,StartConstants.NOSOA);
		Log.setLogType(LogType.console);
		ConsoleLog.setDefaultLevel(LogLevel.ERROR);//这个只能修改默认级别的，如果有具体设置了日志级别，它的优先级比这个高
		Rpc.init();
	}
	
	@After
	public void after(){
		Assert.assertEquals(0, LockHolder.lockSize());
		System.out.println("锁状态正确！");
	}
	
	Random r=new Random();
	
	@Test
	public void test() throws InterruptedException {
		System.out.println("开始压测，请耐心等待10秒左右。。。");
		int count=10_0000;
		AtomicInteger failCount=new AtomicInteger();
		CountDownLatch down=new CountDownLatch(count);
		Rpc.call("a.b.repeat", "预热");
		long begin=System.currentTimeMillis();
		for(int i=0;i<count;i++){
			String msg="这些文字是发送到服务器端的，服务器端会返回一样的文本，然后判断返回的文本是否跟现在一致-"+i;
			Rpc.create("a.b.repeat").paramInArray(msg).timeout(30000).callback(result->{
				if(!msg.equals(result.optResult(String.class))){
					failCount.incrementAndGet();
				}
				down.countDown();
			}).execute();
		}
		down.await();
		long time=System.currentTimeMillis()-begin;
		System.out.println("耗时："+time+",平均每秒请求数："+(count*1000d/time));
		Assert.assertEquals(0, failCount.get());
	}
	

}
