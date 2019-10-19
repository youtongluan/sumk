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
import org.yx.validate.ParamInfo;

public class ActInfoUtil {

	public static Object describe(Class<?> clazz) {
		if (clazz.isArray()) {
			return new Object[] { describe(clazz.getComponentType()) };
		}
		if (clazz.getName().startsWith("java.") || clazz.isPrimitive()) {
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
				if (StringUtil.isNotEmpty(p.cnName())) {
					param.put("cnName", p.cnName());
				}
				param.put("required", p.required());
				if (p.length() > -1) {
					param.put("length", p.length());
				}
				if (p.maxLength() > -1) {
					param.put("maxLength", p.maxLength());
				}
				if (p.minLength() > -1) {
					param.put("minLength", p.minLength());
				}
			}
		}
		map.put("params", list);
		map.put("result", describe(node.method.getReturnType()));
		return map;
	}
}
