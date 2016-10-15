package org.yx.bean;

import org.yx.db.Cachable;

public final class IOC {

	public static <T> T get(String name) {
		return get(name, null);
	}

	public static <T> T get(Class<T> clz) {
		return get(null, clz);
	}

	public static <T> T get(String name, Class<T> clz) {
		return InnerIOC.pool.getBean(name, clz);
	}

	public static String info() {
		return InnerIOC.pool.toString();
	}

	public static <T> T cache(String name, Class<T> clz) {
		if (name == null || name.isEmpty()) {
			name = BeanPool.getBeanName(clz);
		}
		name = Cachable.PRE + name;
		return InnerIOC.pool.getBean(name, clz);
	}

}
