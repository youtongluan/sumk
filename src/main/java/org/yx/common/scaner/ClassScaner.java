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

import org.yx.bean.Loader;

public final class ClassScaner {

	private static ClassNameScaner scaner = new DefaultClassNameScaner();

	public static ClassNameScaner getScaner() {
		return scaner;
	}

	public static void setScaner(ClassNameScaner scaner) {
		ClassScaner.scaner = Objects.requireNonNull(scaner);
	}

	public static <T> Collection<Class<? extends T>> listSubClassesInSamePackage(Class<T> baseClz)
			throws ClassNotFoundException {
		return list(baseClz.getPackage().getName(), baseClz);
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<Class<? extends T>> list(String packageName, Class<T> baseClz)
			throws ClassNotFoundException {
		Collection<String> clzNames = parse(packageName);
		if (clzNames == null || clzNames.isEmpty()) {
			return Collections.emptyList();
		}
		Set<String> names = new HashSet<>(clzNames);
		Set<Class<? extends T>> set = new HashSet<>();
		for (String className : names) {
			Class<?> clz = Loader.loadClassExactly(className);
			if (baseClz.isAssignableFrom(clz) && (!clz.isInterface())
					&& ((clz.getModifiers() & Modifier.ABSTRACT) == 0)) {
				set.add((Class<? extends T>) clz);
			}
		}
		return set;
	}

	public static Collection<String> parse(String... packageNames) {
		return scaner.parse(packageNames);
	}
}
