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
package org.yx.common.scaner;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.yx.bean.Loader;

public final class ClassScaner {

	private static Function<Collection<String>, Collection<String>> scaner = new FileNameScaner(".class");

	public static Function<Collection<String>, Collection<String>> getScaner() {
		return scaner;
	}

	public static void setScaner(Function<Collection<String>, Collection<String>> scaner) {
		ClassScaner.scaner = Objects.requireNonNull(scaner);
	}

	public static <T> Set<Class<? extends T>> subClassesInSameOrSubPackage(Class<T> baseClz)
			throws ClassNotFoundException {
		return list(baseClz.getPackage().getName(), baseClz);
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<Class<? extends T>> list(String packageName, Class<T> baseClz) throws ClassNotFoundException {
		Collection<String> clzNames = listClasses(Collections.singletonList(packageName));
		if (clzNames == null || clzNames.isEmpty()) {
			return Collections.emptySet();
		}
		Set<String> names = new HashSet<>(clzNames);
		Set<Class<? extends T>> set = new HashSet<>();
		for (String className : names) {
			Class<?> clz = Loader.loadClass(className);
			if (baseClz.isAssignableFrom(clz) && (!clz.isInterface())
					&& ((clz.getModifiers() & Modifier.ABSTRACT) == 0)) {
				set.add((Class<? extends T>) clz);
			}
		}
		return set;
	}

	public static Collection<String> listClasses(Collection<String> packageNames) {
		return scaner.apply(packageNames);
	}
}
