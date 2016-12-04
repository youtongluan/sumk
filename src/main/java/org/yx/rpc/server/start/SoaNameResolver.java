package org.yx.rpc.server.start;

import java.lang.reflect.Method;

import org.yx.conf.AppInfo;

class SoaNameResolver {

	public String solve(Class<?> clz, Method m, String soaName) {
		if (soaName != null) {
			soaName = soaName.trim();
			if (soaName.length() > 0) {
				return soaName;
			}
		}
		return AppInfo.getAppId() + "." + clz.getSimpleName().toLowerCase() + "." + m.getName().toLowerCase();
	}
}
