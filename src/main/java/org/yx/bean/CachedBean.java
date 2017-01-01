package org.yx.bean;

import java.util.concurrent.ConcurrentHashMap;

import org.yx.exception.SumkException;

/**
 * 对IOC中的bean进行缓存，以加快获取速度
 * 
 * @author 游夏
 *
 */
public final class CachedBean {
	private static ConcurrentHashMap<Class<?>, Object> map = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> intf) {
		Object obj = map.get(intf);
		if (obj == null) {
			obj = IOC.get(intf);
			if (obj == null) {
				return null;
			}
			if (!intf.isInstance(obj)) {
				SumkException.throwException(65465435, obj.getClass().getName() + " is not an instance of " + intf);
			}
			map.put(intf, obj);
		}
		return (T) obj;
	}

}
