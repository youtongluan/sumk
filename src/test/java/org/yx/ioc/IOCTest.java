package org.yx.ioc;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.yx.bean.Bean;
import org.yx.bean.BeanFactoryListener;
import org.yx.bean.BeanPublisher;
import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.exception.TooManyBeanException;

@Bean
public class IOCTest {
	
	@Test
	public void testInject() {
		BeanPublisher.addListener(new BeanFactoryListener("org.yx"));
		BeanPublisher.publishBeans("org.yx");
		HelloService s=IOC.get(HelloService.class);
		Assert.assertEquals(Hello.class, s.getHello().getClass());
		Assert.assertEquals(Hi.class, s.getSuiyi().getClass());
		Assert.assertEquals(Fine.class, s.f.getClass());
		try {
			IOC.get("hello",IHello.class);
			Assert.fail("要抛出TooManyBeanException才对");
		} catch (TooManyBeanException e) {
		}
	}

	@Test
	public void testDirect() {
		List<String> list=new ArrayList<String>();
		list.add("wertwert");
		InnerIOC.put(list);
		InnerIOC.put(new ArrayList<String>());//这个应该会被过滤掉
		InnerIOC.put("arrayList", new LinkedList<String>());
		Map<Object, Object> map=new HashMap<Object, Object>();
		InnerIOC.put("map", map);
		System.out.println(IOC.info());
		List<String> list2=IOC.get(ArrayList.class);
		Assert.assertEquals(list, list2);
		try {
			IOC.get("arrayList",List.class);
			Assert.fail("要抛出TooManyBeanException才对,因为List接口有2个");
		} catch (Exception e) {
			Assert.assertTrue(e.getClass()==TooManyBeanException.class);
		}
		try {
			IOC.get("arrayList");
			Assert.fail("要抛出TooManyBeanException才对，因为ArrayList有2个bean");
		} catch (Exception e) {
			Assert.assertTrue(e.getClass()==TooManyBeanException.class);
		}
		Assert.assertNull(IOC.get("HashMap"));
		Assert.assertEquals(map, IOC.get("map"));
		//这个会返回LinkedList对应的那个对象,因为ArrayList这个name对应的bean中，实现LinkedList的就只有一个
		Assert.assertNotNull(IOC.get("arrayList",Deque.class).getClass()==LinkedList.class);
	}

}
