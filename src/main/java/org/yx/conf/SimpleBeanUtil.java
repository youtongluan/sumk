/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.conf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.yx.log.Log;
import org.yx.util.StringUtil;

public class SimpleBeanUtil {

	public static void setProperty(Object bean, Method m, String value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (value == null) {
			Log.get("sumk.bean").debug("{} was ignored because value is null", m.getName());
			return;
		}
		value = value.trim();
		if (value.isEmpty()) {
			Log.get("sumk.bean").debug("{} was ignored because value is empty", m.getName());
			return;
		}
		Class<?> ptype = m.getParameterTypes()[0];
		Object v = parseValue(ptype, value);
		if (v == null) {
			Log.get("sumk.bean").warn("{}因为类型({})不支持，被过滤掉", m.getName(), ptype);
			return;
		}
		m.invoke(bean, v);
	}

	public static Object parseValue(Class<?> ptype, String value) {
		if (ptype == String.class) {
			return value;
		}
		if (ptype == int.class || ptype == Integer.class) {
			return Integer.valueOf(value);
		}
		if (ptype == long.class || ptype == Long.class) {
			return Long.valueOf(value);
		}
		if (ptype == double.class || ptype == Double.class) {
			return Double.valueOf(value);
		}
		if (ptype == boolean.class || ptype == Boolean.class) {
			if ("1".equals(value) || "true".equalsIgnoreCase(value)) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}

		if (ptype == short.class || ptype == Short.class) {
			return Short.valueOf(value);
		}
		if (ptype == byte.class || ptype == Byte.class) {
			return Byte.valueOf(value);
		}
		if (ptype == float.class || ptype == Float.class) {
			return Float.valueOf(value);
		}

		if (ptype == char.class || ptype == Character.class) {
			if (value.length() > 0) {
				return value.charAt(0);
			}
		}
		return null;

	}

	public static void copyProperties(Object bean, Map<String, String> map) throws Exception {
		Set<String> set = map.keySet();
		Method[] ms = bean.getClass().getMethods();
		for (String key : set) {
			Method m = getMethod(ms, key);
			if (m == null) {
				Log.get("sumk.bean").warn("{}在{}中不存在", key, bean.getClass().getSimpleName());
				continue;
			}
			setProperty(bean, m, map.get(key));
		}
	}

	private static Method getMethod(Method[] ms, String key) {
		String methodName = "set" + StringUtil.capitalize(key);
		for (Method m : ms) {
			if (m.getName().equals(methodName) && m.getParameterCount() == 1) {
				return m;
			}
		}
		return null;
	}
}
