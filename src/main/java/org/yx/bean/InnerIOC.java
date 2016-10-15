package org.yx.bean;

public final class InnerIOC {
	final static BeanPool pool = new BeanPool();

	public static <T> T putClass(String name, Class<T> clz) throws Exception {
		return pool.putClass(name, clz);
	}

	/**
	 * 获取IOC中的实例，如果实例不存在，就创建新的 这个方法不会处理内部的注入
	 * 
	 * @param clz
	 * @return
	 * @throws Exception
	 */
	public static <T> T getOrCreate(Class<T> clz) throws Exception {
		T obj = IOC.get(clz);
		if (obj != null) {
			return obj;
		}
		return putClass(null, clz);
	}

}
