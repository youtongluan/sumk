package org.yx.bean;

import org.springframework.util.Assert;

public class InnerIOC {
	final static BeanPool pool=new BeanPool();
	
	/**
	 * 添加实例，如果实例已经存在，就不做任何事情.
	 * 这个方法不会处理内部的注入
	 * @param bean
	 */
	public static void put(Object bean){
		Assert.notNull(bean);
		pool.put(null,bean);
	}
	/**
	 * 添加bean，一个类的实例，不能多次出现在一个name中
	 * 这个方法不会处理内部的注入
	 * @param bean
	 * @param name
	 */
	public static void put(String name,Object bean){
		pool.put(name, bean);
	}
	/**
	 * 获取IOC中的实例，如果实例不存在，就创建新的
	 * 这个方法不会处理内部的注入
	 * @param clz
	 * @return
	 * @throws Exception
	 */
	public static <T> T getOrCreate(Class<?> clz) throws Exception{
		Object obj = IOC.get(clz);
		if(obj==null){
			obj=clz.newInstance();
			put(obj);
		}
		return IOC.get(clz);
	}
	
}
