package org.yx.redis;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class SingleTest {

	@Test
	public void test() throws Exception {
		Redis redis=RedisPool.get("127.0.0.1", 6379,1000);
		System.out.println("ExecTest的key最后的ttl时间："+redis.ttl("minunte_count"));
		String k="asdfds",v="sdfsdfsdg";
		redis.set(k,v);
		Assert.assertEquals(v, redis.get(k));
		redis.del(k);
//		Thread.sleep(1000000);
	}
	
	
	@Test
	public void testApi() throws Exception {
		Redis redis=RedisPool.get("127.0.0.1", 6379,3000);
		redis.move("aaa", 2);
		redis.del("kkk");
		redis.rpush("kkk", "12","13");
		List<String> list=redis.lrange("kkk", 0, -1);
		System.out.println(list);
		Assert.assertEquals(list, Arrays.asList("12","13"));
		redis.del("111","222");
		redis.mset("a","1","b","2");
		list=redis.mget("a","b");
		System.out.println(list);
		Assert.assertEquals(list, Arrays.asList("1","2"));
		redis.msetnx("a","30","c","10");
		list=redis.mget("a","b","c");
		System.out.println(list);
		Assert.assertEquals(list, Arrays.asList("1","2",null));
		
		redis.msetnx("a2","30","b2","10");
		list=redis.mget("a2","b2");
		System.out.println(list);
		Assert.assertEquals(list, Arrays.asList("30","10"));
		
	}

	@Test
	public void multiDB() throws Exception {
		Redis redis0=RedisPool.get(null, "127.0.0.1", null);
		Redis redis2=RedisPool.get(null, "127.0.0.1", RedisParamter.create().setDb(2));
		redis0.del("aaa");
		redis2.del("aaa");
		for(int i=0;i<100;i++){
			System.out.println(redis0.incr("aaa"));
			redis2.incr("aaa");
		}
		Assert.assertEquals("100", redis0.get("aaa"));
		Assert.assertEquals("100", redis2.get("aaa"));
	}
}
