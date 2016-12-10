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

	public static final String JAVA_PRE = "java.";
}
