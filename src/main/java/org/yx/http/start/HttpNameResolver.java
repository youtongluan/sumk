package org.yx.http.start;

import java.lang.reflect.Method;

class HttpNameResolver {

	public String solve(Class<?> clz, Method m, String name) {
		if (name != null) {
			name = name.trim();
			if (name.length() > 0) {
				return name;
			}
		}
		return m.getName();
	}
}
