/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.io.InputStream;

public class Loader {
	public static Class<?> loadClass(String clz) throws ClassNotFoundException {
		if (!clz.startsWith("org") && !clz.startsWith("com")) {
			clz = "org.yx." + clz;
		}
		return Loader.class.getClassLoader().loadClass(clz);
	}

	public static InputStream getResourceAsStream(String name) {
		return Loader.class.getClassLoader().getResourceAsStream(name.replace('.', '/'));
	}

	public static final String JAVA_PRE = "java";
}
