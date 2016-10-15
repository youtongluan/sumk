package org.test.soa.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.yx.rpc.SOA;

public class DemoAction2 {
	/*
	 * 用来作为测试用例，解析方法中的参数，组装层pojo. 除此之外，还需要测试继承与重载
	 */
	@SOA
	public void d_b_names_ps(Date d, double b, String[] names, Set<Date> ps) {
		System.out.println("开始执行d_b_names_ps");
		System.out.println("ps:" + ps);
		System.out.println("b:" + b);
		int k = 5;
		String info = "你好" + k;
		System.out.println(info);
		System.out.println("d:" + d);
		System.out.println("name:" + Arrays.toString(names));
		System.out.println(ps.iterator().next().getYear());
	}

	@SOA
	public static void staic_this2(String this2) {
		int k = 5;
		String info2 = "你好" + k;
		System.out.println(info2);
	}

	@SOA
	public static void static_null() {
		int k = 5;
		String info2 = "你好" + k;
		System.out.println(info2);
	}

	@SOA
	public void test_null() {
		int k = 5;
		String info2 = "你好" + k;
		System.out.println(info2);
	}

	@SOA
	public void names_abc(List<String> names, Set<Student> abc) {
		int k = 5;
		String info = "你好" + k;
		System.out.println(info);
		System.out.println("name:" + names);
	}
}
