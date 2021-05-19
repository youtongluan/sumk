/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.rpc.server.start;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.yx.annotation.spec.SoaSpec;
import org.yx.annotation.spec.Specs;
import org.yx.asm.ArgPojos;
import org.yx.asm.AsmUtils;
import org.yx.asm.MethodParamInfo;
import org.yx.bean.BeanKit;
import org.yx.bean.Loader;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.SimpleSumkException;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.log.RawLog;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.RpcActions;
import org.yx.util.StringUtil;

public class SoaAnnotationResolver {
	private SoaClassResolver soaClassResolver;
	private SoaNameResolver nameResolver;
	private Predicate<String> exclude = BooleanMatcher.FALSE;

	public SoaAnnotationResolver() {
		nameResolver = newInstanceFromAppKey("sumk.rpc.name.resolver");
		if (nameResolver == null) {
			nameResolver = new SoaNameResolverImpl();
		}

		soaClassResolver = newInstanceFromAppKey("sumk.rpc.name.soaclass.resolver");
		if (soaClassResolver == null) {
			soaClassResolver = new SoaClassResolverImpl();
		}

		String patterns = AppInfo.get("sumk.rpc.server.exclude", null);
		if (patterns != null) {
			this.exclude = Matchers.createWildcardMatcher(patterns, 1);
			Logs.rpc().debug("soa server exclude:{}", this.exclude);
		}
	}

	private void parseSoaClass(Map<Method, String> map, Class<?> targetClass, Class<?> refer)
			throws NoSuchMethodException, SecurityException {
		String pre = this.soaClassResolver.solvePrefix(targetClass, refer);
		if (pre == null) {
			return;
		}
		Method[] methods = refer.getMethods();
		for (Method m : methods) {
			if (m.getDeclaringClass() == Object.class) {
				continue;
			}
			Method methodInTarget = targetClass.getMethod(m.getName(), m.getParameterTypes());
			if (!m.getReturnType().isAssignableFrom(methodInTarget.getReturnType())) {
				throw new SumkException(234324, targetClass.getName() + "." + methodInTarget.getName() + "的返回值类型是"
						+ methodInTarget.getReturnType().getName() + ",期待的类型是" + m.getReturnType().getName());
			}
			map.put(methodInTarget, pre);
		}
	}

	private void parseSoa(Map<Method, String> map, Class<?> clz) {
		Method[] methods = clz.getMethods();
		for (final Method m : methods) {
			if (Specs.extractSoa(m) == null || map.containsKey(m)) {
				continue;
			}
			map.putIfAbsent(m, "");
		}
	}

	public void resolve(Object bean) throws Exception {

		Class<?> clz = BeanKit.getTargetClass(bean);
		if (this.exclude.test(clz.getName())) {
			return;
		}
		Map<Method, String> map = new HashMap<>();

		Class<?> refer = this.soaClassResolver.getRefer(clz, Specs.extractSoaClass(clz));
		if (refer != null) {
			this.parseSoaClass(map, clz, refer);
		}

		if (refer != clz) {
			this.parseSoa(map, clz);
		}
		List<Method> soaMethods = new ArrayList<>(map.size());
		for (Method m : map.keySet()) {
			if (AsmUtils.notPublicOnly(m.getModifiers())) {
				Logs.asm().error("$$$ {}.{} has bad modifiers, maybe static or private", clz.getName(), m.getName());
				continue;
			}
			soaMethods.add(m);
		}
		if (soaMethods.isEmpty()) {
			return;
		}
		List<MethodParamInfo> mpInfos = AsmUtils.buildMethodInfos(soaMethods);
		for (MethodParamInfo info : mpInfos) {
			Method m = info.getMethod();
			String prefix = map.get(m);
			SoaSpec act = Specs.extractSoa(m);
			List<String> soaNames = StringUtil.isEmpty(prefix) ? nameResolver.solve(clz, m, act)
					: Collections.singletonList(prefix + m.getName());
			if (soaNames == null || soaNames.isEmpty()) {
				continue;
			}

			int toplimit = act != null && act.toplimit() > 0 ? act.toplimit()
					: AppInfo.getInt("sumk.rpc.toplimit.default", Const.DEFAULT_TOPLIMIT);
			boolean publish = act != null ? act.publish() : true;
			RpcActionNode node = new RpcActionNode(bean, m, ArgPojos.create(info), info.getArgNames(), toplimit,
					publish);

			for (String soaName : soaNames) {
				if (soaName == null || soaName.isEmpty()) {
					continue;
				}
				if (RpcActions.getActionNode(soaName) != null) {
					RpcActionNode node0 = RpcActions.getActionNode(soaName);
					Logs.rpc().error(soaName + " already existed -- {}.{},{}.{}", node0.getDeclaringClass().getName(),
							node0.getMethodName(), m.getDeclaringClass().getName(), m.getName());
					throw new SimpleSumkException(1242436, soaName + " already existed");
				}
				RpcActions.putActNode(soaName, node);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstanceFromAppKey(String key) {
		String daoClz = AppInfo.get(key);
		if (daoClz != null && daoClz.length() > 2) {
			try {
				return (T) Loader.newInstance(daoClz);
			} catch (Throwable e) {
				RawLog.error("sumk.bean", e.getMessage(), e);
				return null;
			}
		}
		return null;
	}
}
