package org.test.soa.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.test.soa.demo.EchoAction;
import org.yx.conf.AppInfo;
import org.yx.demo.member.DemoUser;
import org.yx.rpc.client.Rpc;
import org.yx.util.GsonUtil;

public class RpcTest {

	@Test
	public void call() {
		Rpc.init();
		List<String> names = Arrays.asList("游夏", "游侠");
		String echo = "how are you";
		String ret = Rpc.call(AppInfo.getAppId() + ".echoaction.echo", echo, names);
		System.out.println("result:" + ret);
		Assert.assertEquals(new EchoAction().echo(echo, names), GsonUtil.fromJson(ret, List.class));
	}

	@Test
	public void callInJson() {
		Rpc.init();
		List<String> names = Arrays.asList("游夏", "游侠");
		String echo = "how are you";
		Map<String, Object> map = new HashMap<>();
		map.put("echo", echo);
		map.put("names", names);
		String ret = Rpc.callInJson(AppInfo.getAppId() + ".echoaction.echo", GsonUtil.toJson(map));
		Assert.assertEquals(new EchoAction().echo(echo, names), GsonUtil.fromJson(ret, List.class));
	}

	Random r = new Random();

	@Test
	public void db_insert() throws IOException {
		Rpc.init();
		for (int j = 0; j < 5; j++) {
			List<DemoUser> list = new ArrayList<DemoUser>();
			for (int i = 0; i < 10; i++) {
				DemoUser obj = new DemoUser();
				obj.setAge(r.nextInt(100));
				obj.setName("RPC" + r.nextInt());
				obj.setId(r.nextLong());
				list.add(obj);
			}
			String ret = Rpc.call(AppInfo.getAppId() + ".dbdemo.add", list);
			Assert.assertEquals(list.size() + "", ret);
		}
	}

}
