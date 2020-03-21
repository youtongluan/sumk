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
package org.yx.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yx.annotation.Param;
import org.yx.util.StringUtil;
import org.yx.util.SumkDate;
import org.yx.validate.ParamInfo;

public final class ActInfoUtil {

	public static Object describe(Class<?> clazz) {
		if (clazz.isArray()) {
			return new Object[] { describe(clazz.getComponentType()) };
		}
		if (clazz.isPrimitive() || clazz.getName().startsWith("java.") || clazz == SumkDate.class) {
			return clazz.getSimpleName();
		}
		if (Map.class.isAssignableFrom(clazz)) {
			return Collections.emptyMap();
		}
		if (Collection.class.isAssignableFrom(clazz)) {
			return Collections.emptyList();
		}
		Map<String, Object> map = new LinkedHashMap<>();
		Class<?> tempClz = clazz;
		while (tempClz != null && !tempClz.getName().startsWith("java.")) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				if (Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				map.putIfAbsent(f.getName(), describe(f.getType()));
			}
			tempClz = tempClz.getSuperclass();
		}
		return map;
	}

	public static Map<String, Object> infoMap(String name, CalleeNode node) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("name", name);
		map.put("class", node.getDeclaringClass().getName());
		map.put("method", node.getMethodName());
		List<Map<String, Object>> list = new ArrayList<>();
		int paramSize = node.argNames == null ? 0 : node.argNames.length;
		Class<?>[] paramTypes = node.getParameterTypes();
		for (int i = 0; i < paramSize; i++) {
			Map<String, Object> param = new LinkedHashMap<>();
			list.add(param);
			param.put("name", node.argNames[i]);
			param.put("type", describe(paramTypes[i]));
			ParamInfo pi = node.paramInfos == null ? null : node.paramInfos[i];
			if (pi != null) {
				Param p = pi.getParam();
				if (StringUtil.isNotEmpty(p.value())) {
					param.put("cnName", p.value());
				}
				param.put("required", p.required());
				if (p.max() != Integer.MIN_VALUE) {
					param.put("max", p.max());
				}
				if (p.min() != Integer.MIN_VALUE) {
					param.put("min", p.min());
				}
				if (StringUtil.isNotEmpty(p.example())) {
					param.put("example", p.example());
				}
				if (StringUtil.isNotEmpty(p.comment())) {
					param.put("comment", p.comment());
				}
				if (StringUtil.isNotEmpty(p.custom())) {
					param.put("custom", p.custom());
				}
			}
		}
		map.put("params", list);
		map.put("result", describe(node.method.getReturnType()));
		return map;
	}
}
