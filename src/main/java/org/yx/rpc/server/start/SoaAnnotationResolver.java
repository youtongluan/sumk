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

import org.yx.annotation.rpc.Soa;
import org.yx.asm.ArgPojos;
import org.yx.asm.AsmUtils;
import org.yx.asm.MethodDesc;
import org.yx.bean.IOC;
import org.yx.bean.Loader;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.MatcherFactory;
import org.yx.common.matcher.TextMatcher;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.rpc.RpcActionHolder;
import org.yx.rpc.RpcActionNode;
import org.yx.validate.ParamFactory;

public class SoaAnnotationResolver {
	private SoaNameResolver nameResolver;
	private TextMatcher matcher = BooleanMatcher.TRUE;

	public SoaAnnotationResolver() {
		nameResolver = Loader.newInstanceFromAppKey("sumk.rpc.name.resolver");
		if (nameResolver == null) {
			nameResolver = new SoaNameResolverImpl();
		}

		String patterns = AppInfo.get("sumk.rpc.pattern", null);
		if (patterns != null) {
			this.matcher = MatcherFactory.createWildcardMatcher(patterns, 1);
		}
		Log.get("sumk.rpc").debug("soa matcher:{}", this.matcher);
	}

	public void resolve(Object bean) throws Exception {

		Class<?> clz = IOC.getTargetClassOfBean(bean);
		if (!matcher.match(clz.getName())) {
			return;
		}
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

		String classFullName = clz.getName();
		for (final Method m : actMethods) {
			Soa act = m.getAnnotation(Soa.class);
			String soaName = nameResolver.solve(clz, m, act);
			if (soaName == null) {
				continue;
			}
			if (RpcActionHolder.getActionNode(soaName) != null) {
				RpcActionNode node = RpcActionHolder.getActionNode(soaName);
				Log.get("sumk.rpc").error(soaName + " already existed -- {}.{},{}.{}",
						node.method.getDeclaringClass().getName(), node.method.getName(), classFullName, m.getName());
				continue;
			}

			Method proxyedMethod = AsmUtils.getSameMethod(m, bean.getClass());
			int argSize = m.getParameterTypes().length;
			if (argSize == 0) {
				RpcActionHolder.putActNode(soaName,
						new RpcActionNode(bean, proxyedMethod, null, null, null, null, act));
				continue;
			}
			MethodDesc mInfo = AsmUtils.buildMethodDesc(classFullName, m);
			RpcActionHolder.putActNode(soaName,
					new RpcActionNode(bean, proxyedMethod, ArgPojos.create(classFullName, mInfo), mInfo.getArgNames(),
							m.getParameterTypes(), ParamFactory.create(m), act));
		}

	}

}
