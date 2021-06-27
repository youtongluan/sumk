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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;

public final class Loader {

	public static final String JAVA_PRE = "java.";
	private static ClassLoader classLoader;

	public static void setClassLoader(ClassLoader classLoader) {
		Loader.classLoader = classLoader;
	}

	public static ClassLoader loader() {
		ClassLoader l = classLoader;
		return l != null ? l : Loader.class.getClassLoader();
	}

	public static <T> T newInstance(Class<T> clz) throws Exception {
		Constructor<T> c = clz.getDeclaredConstructor();
		if (!c.isAccessible()) {
			c.setAccessible(true);
		}
		return c.newInstance();
	}

	public static Object newInstance(String clz) throws Exception {
		Class<?> clazz = loadClass(clz);
		return newInstance(clazz);
	}

	public static Object newInstance(String clzName, Object[] params, Class<?>[] paramsType) throws Exception {
		Class<?> clz = Class.forName(clzName, true, loader());
		Constructor<?> c = paramsType == null ? clz.getDeclaredConstructor() : clz.getDeclaredConstructor(paramsType);
		if (!c.isAccessible()) {
			c.setAccessible(true);
		}
		return c.newInstance(params);
	}

	public static Class<?> loadClass(String clz) throws ClassNotFoundException {
		if (!clz.startsWith("org.") && !clz.startsWith("com.") && !clz.startsWith("net.") && !clz.startsWith("io.")) {
			try {
				return loader().loadClass("org.yx.".concat(clz));
			} catch (Throwable e) {
			}
		}
		return loadClassExactly(clz);
	}

	public static Class<?> loadClassExactly(String clz) throws ClassNotFoundException {
		return loader().loadClass(clz);
	}

	public static InputStream getResourceAsStream(String name) {
		return loader().getResourceAsStream(name);
	}

	public static Enumeration<URL> getResources(String name) throws IOException {
		return loader().getResources(name.trim());
	}

	public static URL getResource(String name) {
		return loader().getResource(name);
	}

}
