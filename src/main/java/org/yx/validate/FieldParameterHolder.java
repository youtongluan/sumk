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
package org.yx.validate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.yx.annotation.spec.ParamSpec;
import org.yx.annotation.spec.Specs;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;

public final class FieldParameterHolder {
	private static final ConcurrentMap<Class<?>, List<FieldParameterInfo>> map = new ConcurrentHashMap<>();

	public static void put(Class<?> clz, List<FieldParameterInfo> infos) {
		if (clz == null || infos == null || infos.isEmpty()) {
			return;
		}
		map.put(clz, infos);
	}

	public static Set<Class<?>> keys() {
		return new HashSet<>(map.keySet());
	}

	public static List<FieldParameterInfo> get(Class<?> clz) {
		return map.get(clz);
	}

	public static Map<Field, FieldParameterInfo> getFieldParameterMap(Class<?> clz) {
		List<FieldParameterInfo> infos = get(clz);
		if (infos == null) {
			return Collections.emptyMap();
		}
		Map<Field, FieldParameterInfo> infoMap = new HashMap<>();
		for (FieldParameterInfo info : infos) {
			infoMap.put(info.getField(), info);
		}
		return infoMap;
	}

	public static void registerFieldInfo(final Class<?> clazz) {
		if (clazz.isArray()) {
			registerFieldInfo(clazz.getComponentType());
			return;
		}
		if (clazz.isAssignableFrom(Collection.class)) {

			Logs.system().warn("@Param不支持泛型，包括集合Collection");
			return;
		}
		if (get(clazz) != null) {
			return;
		}
		List<FieldParameterInfo> list = new ArrayList<>();
		Class<?> tempClz = clazz;
		while (tempClz != null && !tempClz.getName().startsWith("java.")) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				if (Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				ParamSpec p = Specs.extractParamField(f);
				if (p == null) {
					continue;
				}

				if (!p.required() && p.max() < 0 && p.min() < 0 && !p.complex() && (p.custom() == null)) {
					continue;
				}
				FieldParameterInfo info = new FieldParameterInfo(p, f);
				list.add(info);
				if (info.isComplex()) {
					registerFieldInfo(info.getParamType());
				}
			}
			tempClz = tempClz.getSuperclass();
		}

		if (list.size() > 0) {
			FieldParameterHolder.put(clazz,
					CollectionUtil.unmodifyList(list.toArray(new FieldParameterInfo[list.size()])));
		}
	}
}
