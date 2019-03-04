package org.yx.http.start;

import java.lang.reflect.Method;

public interface HttpNameResolver {

	String solve(Class<?> clz, Method m, String name);

}