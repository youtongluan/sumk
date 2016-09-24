package org.yx.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.yx.log.Log;

/**
 * 简单的bean对象操作，只支持boolean,int,long,double,String,boolean以及他们的包装类
 * 
 * @author youtl
 *
 */
public class SimpleBeanUtil {

	public static void setProperty(Object bean, Method m, String value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (value == null) {
			Log.get(SimpleBeanUtil.class, "setProperty").debug("{} was ignored because value is null", m.getName());
			return;
		}
		value = value.trim();
		if (value.isEmpty()) {
			Log.get(SimpleBeanUtil.class, "setProperty").debug("{} was ignored because value is empty", m.getName());
			return;
		}
		Class<?> ptype = m.getParameterTypes()[0];
		Object v;
		if (ptype == int.class || ptype == Integer.class) {
			v = Integer.valueOf(value);
		} else if (ptype == long.class || ptype == Long.class) {
			v = Long.valueOf(value);
		} else if (ptype == double.class || ptype == Double.class) {
			v = Double.valueOf(value);
		} else if (ptype == boolean.class || ptype == Boolean.class) {
			if ("1".equals(value) || "true".equalsIgnoreCase(value)) {
				v = Boolean.TRUE;
			} else {
				v = Boolean.FALSE;
			}
		} else if (ptype == String.class) {
			v = value;
		} else {
			Log.get(SimpleBeanUtil.class, "setProperty").debug("{}因为类型不支持，被过滤掉", m.getName());
			return;
		}
		m.invoke(bean, v);
	}

	/**
	 * 设置对象属性， setXX方法不能重名，并且参数只有一个。 如果setXX方法不存在，就忽略map中的该值
	 * 
	 * @param bean
	 * @param map
	 */
	public static void copyProperties(Object bean, Map<String, String> map) throws Exception {
		Set<String> set = map.keySet();
		Method[] ms = bean.getClass().getMethods();
		for (String key : set) {
			Method m = getMethod(ms, key);
			if (m == null) {
				Log.get(SimpleBeanUtil.class, "setProperties").debug("{}在{}中不存在", key, bean.getClass().getSimpleName());
				continue;
			}
			setProperty(bean, m, map.get(key));
		}
	}

	private static Method getMethod(Method[] ms, String key) {
		String methodName = "set" + StringUtils.capitalize(key);
		for (Method m : ms) {
			if (m.getName().equals(methodName) && m.getParameterCount() == 1) {
				return m;
			}
		}
		return null;
	}
}
