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
package org.yx.http.start;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.yx.annotation.http.Web;
import org.yx.asm.ArgPojos;
import org.yx.asm.AsmUtils;
import org.yx.asm.MethodParamInfo;
import org.yx.bean.IOC;
import org.yx.bean.Loader;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.MatcherFactory;
import org.yx.common.matcher.TextMatcher;
import org.yx.conf.AppInfo;
import org.yx.exception.SimpleSumkException;
import org.yx.http.HttpActionHolder;
import org.yx.http.handler.HttpActionNode;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;
import org.yx.validate.ParamFactory;

public class WebAnnotationResolver {
	private WebNameResolver nameResolver;
	private TextMatcher matcher = BooleanMatcher.TRUE;

	public WebAnnotationResolver() {
		nameResolver = Loader.newInstanceFromAppKey("sumk.http.name.resolver");
		if (nameResolver == null) {
			nameResolver = new WebNameResolverImpl();
		}
		String patterns = AppInfo.get("sumk.http.pattern", null);
		if (patterns != null) {
			this.matcher = MatcherFactory.createWildcardMatcher(patterns, 1);
		}
		ConsoleLog.get("sumk.http").debug("web matcher:{}", this.matcher);
	}

	public void resolve(Object bean) throws Exception {

		Class<?> clz = IOC.getTargetClassOfBean(bean);
		if (!matcher.match(clz.getName())) {
			return;
		}
		Method[] methods = clz.getDeclaredMethods();
		List<Method> httpMethods = new ArrayList<>();
		for (final Method m : methods) {
			if (AsmUtils.isFilted(m.getName())) {
				continue;
			}
			if (!m.isAnnotationPresent(Web.class)) {
				continue;
			}
			if (AsmUtils.notPublicOnly(m.getModifiers())) {
				Log.get("sumk.asm").error("$$$ {}.{} has bad modifiers, maybe static or private", clz.getName(),
						m.getName());
				continue;
			}
			httpMethods.add(m);
		}
		if (httpMethods.isEmpty()) {
			return;
		}

		List<MethodParamInfo> mpInfos = AsmUtils.buildMethodInfos(httpMethods);
		for (MethodParamInfo info : mpInfos) {
			Method m = info.getMethod();
			Web act = m.getAnnotation(Web.class);
			List<String> names = nameResolver.solve(clz, m, act);
			if (names == null || names.isEmpty()) {
				continue;
			}

			HttpActionNode node = new HttpActionNode(bean, m, ArgPojos.create(info), info.getArgNames(),
					ParamFactory.create(m), m, act);

			for (String name : names) {
				this.addAction(name, node);
			}
		}
	}

	private void addAction(String name, HttpActionNode node) throws Exception {
		if (name == null || name.isEmpty()) {
			return;
		}
		if (HttpActionHolder.getHttpInfo(name) != null) {
			throw new SimpleSumkException(1242435, name + " already existed");
		}
		HttpActionHolder.putActInfo(name, node);
	}

}
