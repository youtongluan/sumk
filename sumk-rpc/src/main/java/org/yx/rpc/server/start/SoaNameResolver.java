package org.yx.rpc.server.start;

import java.lang.reflect.Method;
import java.util.List;

import org.yx.rpc.spec.SoaSpec;

public interface SoaNameResolver {

	List<String> solve(Class<?> clz, Method m, SoaSpec soa);

}