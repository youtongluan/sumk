package org.test.soa.demo;

import java.util.ArrayList;
import java.util.List;

import org.yx.rpc.SOA;

public class EchoAction {

	@SOA
	public List<String> echo(String echo, List<String> names) {
		List<String> list = new ArrayList<String>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@SOA
	public String hi() {
		return "hello";
	}

	@SOA
	public void print() {
		System.out.println("print");
	}
}
