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

import org.yx.conf.AppInfo;
import org.yx.log.RawLog;

public final class Loader {

	public static <T> T newInstance(Class<T> clz) throws Exception {
		Constructor<T> c = clz.getDeclaredConstructor();
		c.setAccessible(true);
		return c.newInstance();
	}

	public static Object newInstance(String clz) throws Exception {
		Class<?> clazz = Loader.loadClass(clz);
		return newInstance(clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstanceFromAppKey(String key) {
		String daoClz = AppInfo.get(key);
		if (daoClz != null && daoClz.length() > 2) {
			try {
				return (T) Loader.newInstance(daoClz);
			} catch (Throwable e) {
				RawLog.error("sumk.bean", e.getMessage(), e);
				return null;
			}
		}
		return null;
	}

	public static Class<?> loadClass(String clz) throws ClassNotFoundException {
		if (!clz.startsWith("org.") && !clz.startsWith("com.") && !clz.startsWith("net.") && !clz.startsWith("io.")) {
			try {
				return Loader.class.getClassLoader().loadClass("org.yx.".concat(clz));
			} catch (Throwable e) {
			}
		}
		return loadClassExactly(clz);
	}

	public static Class<?> loadClassExactly(String clz) throws ClassNotFoundException {
		return loader().loadClass(clz);
	}

	public static InputStream getResourceAsStream(String name) {
		name = name.replace('.', '/');
		InputStream in = loader().getResourceAsStream(name + "-impl");
		if (in != null) {
			return in;
		}
		return loader().getResourceAsStream(name);
	}

	public static ClassLoader loader() {
		return Loader.class.getClassLoader();
	}

	public static Enumeration<URL> getResources(String name) throws IOException {
		return loader().getResources(name.trim());
	}

	public static URL getResource(String name) {
		return loader().getResource(name);
	}

	public static final String JAVA_PRE = "java.";
}
