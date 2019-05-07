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
package org.yx.util.kit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 支持子类，但不支持属性的嵌套解析
 */

public class BeanConverter {
	private static final String JAVA_PRE = "java";

	private static final int NOT_PARSE = Modifier.STATIC | Modifier.TRANSIENT;
	private final ConcurrentMap<Class<?>, Field[]> cache = new ConcurrentHashMap<>();

	private Field[] parseFields(Class<?> clz) {
		List<Field> list = new ArrayList<>();
		Class<?> tempClz = clz;
		while (tempClz != null && !tempClz.getName().startsWith(JAVA_PRE)) {

			Field[] fs = cache.get(tempClz);
			if (fs != null) {
				list.addAll(Arrays.asList(fs));
				break;
			}
			fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				if ((f.getModifiers() & NOT_PARSE) != 0) {
					continue;
				}
				f.setAccessible(true);
				list.add(f);
			}
			tempClz = tempClz.getSuperclass();
		}
		return list.toArray(new Field[list.size()]);
	}

	public Field[] getFields(Class<?> clz) {
		Field[] fields = cache.get(clz);
		if (fields != null) {
			return fields;
		}
		fields = this.parseFields(clz);
		String clzName = clz.getName();
		if (clzName.startsWith(JAVA_PRE) || clzName.contains("$")) {
			return fields;
		}
		Field[] f2 = cache.putIfAbsent(clz, fields);
		return f2 != null ? f2 : fields;
	}

	/**
	 * 根据field转为map。不会对属性内部的字段再做解析
	 * 
	 * @param bean
	 *            用于转化的pojo对象。
	 * @return 返回map对象，不为null。
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> beanToMap(Object bean) throws Exception {
		if (bean == null) {
			return Collections.emptyMap();
		}
		if (Map.class.isInstance(bean)) {
			return (Map<String, Object>) bean;
		}
		Field[] fields = getFields(bean.getClass());
		Map<String, Object> map = new HashMap<>();
		for (Field f : fields) {
			map.putIfAbsent(f.getName(), f.get(bean));
		}
		return map;
	}

	/**
	 * 根据map的内容填充bean的属性。只转义第一层的key。
	 * 
	 * @param map
	 *            原始map
	 * @param bean
	 *            目标对象，它的属性会被填充进来
	 * @throws Exception
	 *             异常
	 */
	public void fillBean(Map<String, Object> map, Object bean) throws Exception {
		if (map == null || bean == null) {
			return;
		}
		if (Map.class.isInstance(bean)) {
			return;
		}
		Field[] fields = getFields(bean.getClass());
		for (Field f : fields) {
			if (Modifier.isFinal(f.getModifiers())) {
				continue;
			}
			if (map.containsKey(f.getName())) {
				f.set(bean, map.get(f.getName()));
			}
		}
	}

	ConcurrentMap<Class<?>, Field[]> getCache() {
		return this.cache;
	}

}
