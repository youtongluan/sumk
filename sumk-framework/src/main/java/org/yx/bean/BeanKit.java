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
package org.yx.bean;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.yx.util.Loader;
import org.yx.util.StringUtil;

public final class BeanKit {

	public static Class<?> getTargetClass(Object bean) {
		return bean instanceof Boxed ? ((Boxed) bean).targetRawClass() : bean.getClass();
	}

	public static String resloveBeanName(Class<?> clz) {
		String name = StringUtil.uncapitalize(clz.getSimpleName());
		if (name.endsWith("Impl")) {
			name = name.substring(0, name.length() - 4);
		}
		return name;
	}

	public static Set<String> resloveBeanNames(Class<?> clazz) {
		Set<Class<?>> interfaces = new HashSet<>();
		resloveSuperClassAndInterface(clazz, interfaces);
		Set<String> names = new HashSet<>();
		for (Class<?> clz : interfaces) {
			names.add(resloveBeanName(clz));
		}
		return names;
	}

	private static void resloveSuperClassAndInterface(Class<?> clazz, Set<Class<?>> interfaces) {
		while (clazz != null && !clazz.getName().startsWith(Loader.JAVA_PRE)
				&& (clazz.getModifiers() & Modifier.PUBLIC) != 0) {
			interfaces.add(clazz);
			Class<?>[] ifcs = clazz.getInterfaces();
			if (ifcs != null) {
				for (Class<?> ifc : ifcs) {
					resloveSuperClassAndInterface(ifc, interfaces);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

}
