package org.yx.http.start;

import java.lang.reflect.Method;
import java.util.List;

import org.yx.annotation.http.Web;

public interface WebNameResolver {

	List<String> solve(Class<?> clz, Method m, Web web);

}