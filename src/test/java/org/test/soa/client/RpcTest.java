package org.test.soa.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.test.soa.demo.EchoAction;
import org.yx.rpc.client.Client;
import org.yx.util.GsonUtil;


public class RpcTest {

	@Test
	public void test() {
		Client.init();
		List<String> names=new ArrayList<String>();
		names.add("游侠");
		names.add("BOSS");
		String echo=",how are you";
		//ret是json格式
		String ret=Client.call("demo.EchoAction.echo", echo,names);
		System.out.println("result:"+ret);
		Assert.assertEquals(new EchoAction().echo(echo, names), GsonUtil.fromJson(ret, List.class));
		
		Map<String,Object> map=new HashMap<>();
		map.put("echo", echo);
		map.put("names", names);
		ret=Client.callInJson("demo.EchoAction.echo", GsonUtil.toJson(map));
		Assert.assertEquals(new EchoAction().echo(echo, names), GsonUtil.fromJson(ret, List.class));
	}

}
