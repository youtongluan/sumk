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
import java.util.List;
import java.util.function.Predicate;

import org.yx.annotation.rpc.Soa;
import org.yx.asm.ArgPojos;
import org.yx.asm.AsmUtils;
import org.yx.asm.MethodParamInfo;
import org.yx.bean.IOC;
import org.yx.bean.Loader;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.exception.SimpleSumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.RpcActions;
import org.yx.validate.ParamFactory;

public class SoaAnnotationResolver {
	private SoaNameResolver nameResolver;
	private Predicate<String> matcher = BooleanMatcher.TRUE;

	public SoaAnnotationResolver() {
		nameResolver = Loader.newInstanceFromAppKey("sumk.rpc.name.resolver");
		if (nameResolver == null) {
			nameResolver = new SoaNameResolverImpl();
		}

		String patterns = AppInfo.get("sumk.rpc.pattern", null);
		if (patterns != null) {
			this.matcher = Matchers.createWildcardMatcher(patterns, 1);
		}
		Logs.rpc().debug("soa matcher:{}", this.matcher);
	}

	public void resolve(Object bean) throws Exception {

		Class<?> clz = IOC.getTargetClassOfBean(bean);
		if (!matcher.test(clz.getName())) {
			return;
		}
		Method[] methods = clz.getDeclaredMethods();
		List<Method> actMethods = new ArrayList<>();
		for (final Method m : methods) {
			if (AsmUtils.isFilted(m.getName())) {
				continue;
			}
			if (m.getAnnotation(Soa.class) == null) {
				continue;
			}
			if (AsmUtils.notPublicOnly(m.getModifiers())) {
				Log.get("sumk.asm").error("$$$ {}.{} has bad modifiers, maybe static or private", clz.getName(),
						m.getName());
				continue;
			}
			actMethods.add(m);
		}
		if (actMethods.isEmpty()) {
			return;
		}

		List<MethodParamInfo> mpInfos = AsmUtils.buildMethodInfos(actMethods);
		for (MethodParamInfo info : mpInfos) {
			Method m = info.getMethod();
			Soa act = m.getAnnotation(Soa.class);
			List<String> soaNames = nameResolver.solve(clz, m, act);
			if (soaNames == null || soaNames.isEmpty()) {
				continue;
			}

			RpcActionNode node = new RpcActionNode(bean, m, ArgPojos.create(info), info.getArgNames(),
					ParamFactory.create(m), act);

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

}
