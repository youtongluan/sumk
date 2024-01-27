package org.yx.rpc.server.start;

import org.yx.rpc.spec.SoaClassSpec;

public interface SoaClassResolver {

	Class<?> AUTO = Void.class;

	String solvePrefix(Class<?> targetClass, Class<?> refer);

	Class<?> getRefer(Class<?> targetClass, SoaClassSpec sc);

}