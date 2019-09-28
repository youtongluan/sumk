package org.yx.rpc.server.start;

import java.lang.reflect.Method;
import java.util.List;

import org.yx.annotation.rpc.Soa;

public interface SoaNameResolver {

	List<String> solve(Class<?> clz, Method m, Soa soa);

}