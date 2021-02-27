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
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.yx.annotation.http.Web;
import org.yx.asm.ArgPojos;
import org.yx.asm.AsmUtils;
import org.yx.asm.MethodParamInfo;
import org.yx.bean.BeanKit;
import org.yx.common.KeyValuePair;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.http.act.HttpActionNode;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

public final class WebAnnotationResolver {

	private Predicate<String> exclude = BooleanMatcher.FALSE;

	private List<String> rawNames(Method m, Web web) {
		String name = web.value();
		if (name == null || name.isEmpty()) {
			return Collections.singletonList(m.getName());
		}
		List<String> list = new ArrayList<String>(1);
		String[] names = StringUtil.toLatin(name).split(",");
		for (String raw : names) {
			if (raw != null && (raw = raw.trim()).length() > 0) {
				list.add(raw);
			}
		}
		return list.size() > 0 ? list : Collections.singletonList(m.getName());
	}

	public WebAnnotationResolver() {
		String patterns = AppInfo.get("sumk.http.exclude", null);
		if (patterns != null) {
			this.exclude = Matchers.createWildcardMatcher(patterns, 1);
			Logs.http().debug("web exclude:{}", this.exclude);
		}
	}

	public List<KeyValuePair<HttpActionNode>> resolve(Object bean) throws Exception {

		Class<?> clz = BeanKit.getTargetClass(bean);
		if (exclude.test(clz.getName())) {
			return null;
		}
		Method[] methods = clz.getMethods();
		List<Method> httpMethods = new ArrayList<>();
		for (final Method m : methods) {
			if (!m.isAnnotationPresent(Web.class)) {
				continue;
			}
			if (AsmUtils.notPublicOnly(m.getModifiers())) {
				Logs.http().warn("$$$ {}.{} has bad modifiers, maybe static or private", clz.getName(), m.getName());
				continue;
			}
			httpMethods.add(m);
		}
		if (httpMethods.isEmpty()) {
			return null;
		}

		List<MethodParamInfo> mpInfos = AsmUtils.buildMethodInfos(httpMethods);
		List<KeyValuePair<HttpActionNode>> infos = new ArrayList<>(mpInfos.size() * 2);
		for (MethodParamInfo info : mpInfos) {
			Method m = info.getMethod();
			Web act = m.getAnnotation(Web.class);
			HttpActionNode node = new HttpActionNode(bean, m, ArgPojos.create(info), info.getArgNames(), act);

			List<String> names = rawNames(m, act);
			if (names == null || names.isEmpty()) {
				continue;
			}
			for (String name : names) {
				infos.add(new KeyValuePair<>(name, node));
			}
		}
		return infos;
	}
}
