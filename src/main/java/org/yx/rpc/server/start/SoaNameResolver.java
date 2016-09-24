package org.yx.rpc.server.start;

import java.lang.reflect.Method;

import org.yx.exception.SystemException;

class SoaNameResolver {

	public String solve(Class<?> clz, Method m, String soaName) {
		if (soaName != null) {
			soaName = soaName.trim();
			if (soaName.length() > 0) {
				return soaName;
			}
		}
		String[] cs = clz.getName().split("\\.");
		if (cs.length < 2) {
			SystemException.throwException(122342, clz.getName() + "必须要有包名");
		}
		return String.join(".", cs[cs.length - 2], cs[cs.length - 1], m.getName());
	}
}
