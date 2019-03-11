package org.yx.rpc.server.start;

import java.lang.reflect.Method;

import org.yx.annotation.rpc.Soa;

public interface SoaNameResolver {

	String solve(Class<?> clz, Method m, Soa soa);

}