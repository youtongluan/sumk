package org.test.soa.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.test.soa.demo.EchoAction;
import org.yx.conf.AppInfo;
import org.yx.demo.member.DemoUser;
import org.yx.rpc.client.Client;
import org.yx.util.GsonUtil;

public class RpcTest {

	@Test
	public void test() {
		Client.init();
		List<String> names = new ArrayList<String>();
		names.add("游侠");
		names.add("BOSS");
		String echo = ",how are you";
		// ret是json格式
		String ret = Client.call(AppInfo.getAppId() + ".echoaction.echo", echo, names);
		System.out.println("result:" + ret);
		Assert.assertEquals(new EchoAction().echo(echo, names), GsonUtil.fromJson(ret, List.class));

		Map<String, Object> map = new HashMap<>();
		map.put("echo", echo);
		map.put("names", names);
		ret = Client.callInJson(AppInfo.getAppId() + ".echoaction.echo", GsonUtil.toJson(map));
		Assert.assertEquals(new EchoAction().echo(echo, names), GsonUtil.fromJson(ret, List.class));
	}

	Random r = new Random();

	@Test
	public void db_insert() throws IOException {
		List<DemoUser> list = new ArrayList<DemoUser>();
		for (int i = 0; i < 10; i++) {
			DemoUser obj = new DemoUser();
			obj.setAge(r.nextInt(100));
			obj.setName("名字" + r.nextInt());
			obj.setId(r.nextLong());
			list.add(obj);
		}
		String ret = Client.call(AppInfo.getAppId() + ".dbdemo.add", list);
		Assert.assertEquals(list.size() + "", ret);
	}

}
