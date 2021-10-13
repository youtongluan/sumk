package org.test.soa.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.test.soa.demo.EchoAction;
import org.yx.conf.AppInfo;
import org.yx.demo.member.DemoUser;
import org.yx.main.StartConstants;
import org.yx.main.SumkServer;
import org.yx.rpc.client.Rpc;
import org.yx.rpc.client.RpcFuture;
import org.yx.util.S;

// RPC是异步启动的，所以有可能打印启动成功，但实际上还没有
public class RpcTest {

	@BeforeClass
	public static void before() {
		SumkServer.start(StartConstants.NOHTTP,StartConstants.NOSOA);
		Rpc.init();
	}

	public static String soaName(String soaName){
		StringBuilder sb=new StringBuilder();
		if(AppInfo.appId(null)!=null){
			sb.append(AppInfo.appId(null)).append('.');
		}
		return sb.append(soaName).toString();
	}

	@Test
	public void test() {
		List<String> names = Arrays.asList("游夏", "游侠");
		String echo = "how are you";
		// ret是json格式
		String ret = Rpc.call(soaName("echo"), echo, names);
		System.out.println("result:" + ret);
		Assert.assertEquals(new EchoAction().echo(echo, names), S.json().fromJson(ret, List.class));
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("echo", echo);
			map.put("names", names);
			ret = Rpc.callInMap(soaName("echo"), map);
			Assert.assertEquals(new EchoAction().echo(echo, names), S.json().fromJson(ret, List.class));
			ret = Rpc.call(soaName("echo"), echo, names);
			Assert.assertEquals(new EchoAction().echo(echo, names), S.json().fromJson(ret, List.class));
			System.out.println("test:" + ret);
		}
	}

	Random r = new Random();

	private List<DemoUser> create() {
		List<DemoUser> list = new ArrayList<DemoUser>();
		for (int i = 0; i < 10; i++) {
			DemoUser obj = new DemoUser();
			obj.setAge(r.nextInt(100));
			obj.setName("名字" + r.nextInt());
			obj.setId(r.nextLong());
			list.add(obj);
		}
		return list;
	}

	@Test
	public void db_insert() throws IOException {
		for (int j = 0; j < 5; j++) {
			List<DemoUser> list = create();
			String ret;
			Map<String, Object> map = new HashMap<>();
			map.put("users", list);
			ret = Rpc.callInMap(soaName("add"), map);
			System.out.println("返回的信息：" + ret);
			Assert.assertEquals(list.size() + "", ret);

			list = create();
			ret = Rpc.call(soaName("add"), list);
			System.out.println("返回的信息：" + ret);
			Assert.assertEquals(list.size() + "", ret);
		}
	}

	@Test
	public void async() {
		System.out.println("now:" + System.currentTimeMillis());
		List<String> names = Arrays.asList("游夏", "游侠");
		String echo = "how are you,";
		// ret是json格式
		List<RpcFuture> retList = new ArrayList<>();
		List<RpcFuture> retList2 = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("echo", echo + i);
			map.put("names", names);
			retList.add(Rpc.callInMapAsync(soaName("echo"), map));
			retList2.add(Rpc.callAsync(soaName("echo"), echo + i, names));
		}

		for (int i = 0; i < 5; i++) {
			System.out.println(i + " 异步");
			Assert.assertEquals(new EchoAction().echo(echo + i, names),
					S.json().fromJson(retList.get(i).opt(), List.class));
			Assert.assertEquals(new EchoAction().echo(echo + i, names),
					S.json().fromJson(retList2.get(i).opt(), List.class));
		}
	}

}
