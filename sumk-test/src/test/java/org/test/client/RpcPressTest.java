package org.test.client;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yx.log.LogLevel;
import org.yx.log.Loggers;
import org.yx.main.StartConstants;
import org.yx.main.SumkServer;
import org.yx.rpc.client.LockHolder;
import org.yx.rpc.client.Rpc;
import org.yx.util.SumkDate;

public class RpcPressTest {

	@Before
	public void before(){
		SumkServer.start(RpcPressTest.class,Arrays.asList(StartConstants.NOHTTP,StartConstants.NOSOA));
		Loggers.setDefaultLevel(LogLevel.ERROR);//这个只能修改默认级别的，如果有具体设置了日志级别，它的优先级比这个高
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
		System.out.println("开始压测，请耐心等待30秒左右。。。");
		final int count=50_0000;
		final int threadCount=50;
		AtomicInteger failCount=new AtomicInteger();
		CountDownLatch down=new CountDownLatch(count);
		Rpc.call("a.b.repeat", "预热");
		char[] cs=new char[1200];
		Arrays.fill(cs, 't');
		Arrays.fill(cs, 100,200,'好');
		cs[5]='啊';
		cs[45]='1';
		cs[59]='o';
		cs[115]='是';
		cs[800]='是';
		String pre=new String(cs);
		long begin=System.currentTimeMillis();
		AtomicLong totalRT=new AtomicLong();
		for(int i=0;i<threadCount;i++){
			new Thread(){
				@Override
				public void run(){
					long t=0;
					for(int i=0;i<count/threadCount;i++){
						//收发1k左右的数据量
						String msg=pre+i;
						long b2=System.currentTimeMillis();
						String msg2=Rpc.create("a.b.repeat").paramInArray(msg).timeout(30000).execute().getOrException();
						t+=System.currentTimeMillis()-b2;
						if(!("\""+msg+"\"").equals(msg2)){
							failCount.incrementAndGet();
							System.out.println(msg);
							System.out.println(msg2);
						}
						down.countDown();
					}
					totalRT.addAndGet(t);
				}
			}.start();
		}
		while(!down.await(5, TimeUnit.SECONDS)){
			System.out.println(SumkDate.now()+" - 剩余请求："+down.getCount());
		}
		long time=System.currentTimeMillis()-begin;
		System.out.println("耗时："+time+",平均每秒请求数："+(count*1000d/time));
		System.out.println("平均每个响应耗时："+totalRT.get()/count+"ms");
		Assert.assertEquals(0, failCount.get());
	}
	

}
