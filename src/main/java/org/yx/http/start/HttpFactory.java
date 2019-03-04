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

import org.yx.asm.ArgPojos;
import org.yx.asm.AsmUtils;
import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.common.MethodDesc;
import org.yx.http.HttpActionHolder;
import org.yx.http.Web;
import org.yx.http.handler.HttpActionNode;
import org.yx.log.Log;
import org.yx.validate.ParamFactory;

class HttpFactory {
	private HttpNameResolver nameResolver;

	public HttpFactory() {
		nameResolver = Loader.newInstanceFromAppKey("http.name.resolver");
		if (nameResolver == null) {
			nameResolver = new HttpNameResolverImpl();
		}
	}

	public void resolve(Class<?> clz) throws Exception {
		Method[] methods = clz.getMethods();
		List<Method> httpMethods = new ArrayList<>();
		for (final Method m : methods) {
			if (AsmUtils.isFilted(m.getName())) {
				continue;
			}
			if (AsmUtils.notPublicOnly(m.getModifiers())) {
				continue;
			}
			if (m.isAnnotationPresent(Web.class)) {
				httpMethods.add(m);
			}
		}
		if (httpMethods.isEmpty()) {
			return;
		}
		final Object obj = InnerIOC.putClass(null, clz);
		Class<?> proxyClz = obj.getClass();
		String classFullName = clz.getName();
		for (final Method m : httpMethods) {
			Web act = m.getAnnotation(Web.class);
			String soaName = nameResolver.solve(clz, m, act.value());
			Log.get("sumk.http").debug("http action-{}:{}", soaName, classFullName);
			if (HttpActionHolder.getHttpInfo(soaName) != null) {
				Log.get("sumk.http").error(soaName + " already existed");
				continue;
			}
			Method proxyedMethod = AsmUtils.proxyMethod(m, proxyClz);
			if (m.getParameterTypes().length == 0) {
				HttpActionHolder.putActInfo(soaName,
						new HttpActionNode(obj, proxyedMethod, null, null, null, null, m, act));
				continue;
			}
			MethodDesc mInfo = AsmUtils.buildMethodDesc(classFullName, m);
			HttpActionHolder.putActInfo(soaName,
					new HttpActionNode(obj, proxyedMethod, ArgPojos.create(classFullName, mInfo), mInfo.getArgNames(),
							m.getParameterTypes(), ParamFactory.create(m), m, act));
		}

	}

}
