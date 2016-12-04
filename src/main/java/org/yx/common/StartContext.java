package org.yx.common;

import java.util.HashMap;
import java.util.Map;

public class StartContext {

	public static StartContext inst = new StartContext();

	private Map<String, Object> map = new HashMap<>();

	public void put(String key, Object obj) {
		map.put(key, obj);
	}

	public void put(Class<?> clz, Object obj) {
		map.put(clz.getName(), obj);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public Object get(Class<?> clz) {
		return map.get(clz.getName());
	}

	/**
	 * 获取key的值，如果不存在，就设入t。并将t返回
	 * 
	 * @param key
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getOrCreate(String key, T t) {
		if (map.containsKey(key)) {
			return (T) map.get(key);
		}
		map.put(key, t);
		return t;
	}

	public <T> T getOrCreate(Class<?> clz, T t) {
		return this.getOrCreate(clz.getName(), t);
	}

	public static void clear() {
		inst.map = null;
		inst = null;
	}

}
