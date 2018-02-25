package org.test.soa.demo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.yx.http.HttpSessionHolder;
import org.yx.rpc.Soa;

public class EchoAction {

	@Soa
	public List<String> echo(String echo, List<String> names) {

		List<String> list = new ArrayList<String>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Soa
	public String hi() {
		return "hello";
	}

	@Soa
	public void print() {
		System.out.println("print");
	}

}
