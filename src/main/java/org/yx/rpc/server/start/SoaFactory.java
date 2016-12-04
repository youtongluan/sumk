package org.yx.rpc.server.start;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.yx.asm.AsmUtils;
import org.yx.bean.InnerIOC;
import org.yx.common.MethodInfo;
import org.yx.log.Log;
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ActionInfo;
import org.yx.rpc.Soa;

class SoaFactory {
	private SoaNameResolver nameResolver = new SoaNameResolver();

	public void resolve(Class<?> clz) throws Exception {
		Method[] methods = clz.getMethods();
		List<Method> actMethods = new ArrayList<>();
		for (final Method m : methods) {
			if (AsmUtils.isFilted(m.getName())) {
				continue;
			}
			if (AsmUtils.notPublicOnly(m.getModifiers())) {
				continue;
			}
			if (m.getAnnotation(Soa.class) != null) {
				actMethods.add(m);
			}
		}
		if (actMethods.isEmpty()) {
			return;
		}

		final Object obj = InnerIOC.putClass(null, clz);
		Class<?> proxyClz = obj.getClass();
		String classFullName = clz.getName();
		for (final Method m : actMethods) {
			Soa act = m.getAnnotation(Soa.class);
			String soaName = nameResolver.solve(clz, m, act.value());
			if (ActionHolder.getActionInfo(soaName) != null) {
				Log.get("SYS.soa").error(soaName + " already existed");
				continue;
			}
			Method proxyedMethod = AsmUtils.proxyMethod(m, proxyClz);
			int argSize = m.getParameterTypes().length;
			if (argSize == 0) {
				ActionHolder.putActInfo(soaName, new ActionInfo(obj, proxyedMethod, null, null, null, act));
				continue;
			}
			MethodInfo mInfo = AsmUtils.createMethodInfo(classFullName, m);
			Class<?> argClz = AsmUtils.CreateArgPojo(classFullName, mInfo);
			ActionHolder.putActInfo(soaName,
					new ActionInfo(obj, proxyedMethod, argClz, mInfo.getArgNames(), m.getParameterTypes(), act));
		}

	}

}
