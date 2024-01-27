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

import java.util.function.Predicate;

import org.yx.base.matcher.BooleanMatcher;
import org.yx.base.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.rpc.context.InnerRpcUtil;
import org.yx.rpc.spec.SoaClassSpec;
import org.yx.util.Loader;

public class SoaClassResolverImpl implements SoaClassResolver {

	private Predicate<String> autoMatcher = BooleanMatcher.FALSE;

	public SoaClassResolverImpl() {
		String soaClassMatcher = AppInfo.get("sumk.rpc.intfserver.automatch", null);
		if (soaClassMatcher != null) {
			this.autoMatcher = Matchers.createWildcardMatcher(soaClassMatcher, 1);
		}
	}

	@Override
	public String solvePrefix(Class<?> targetClass, Class<?> refer) {
		if (refer == null) {
			Logs.rpc().warn("{}的@SoaClass的值不能为null或Object", targetClass.getName());
			return null;
		}
		if (!refer.isAssignableFrom(targetClass)) {
			Logs.rpc().warn("{}的@SoaClass的value不是它的接口或超类", targetClass.getName());
			return null;
		}
		return InnerRpcUtil.parseRpcIntfPrefix(refer);
	}

	@Override
	public Class<?> getRefer(Class<?> targetClass, SoaClassSpec sc) {
		Class<?> refer = parseRefer(targetClass, sc);
		if (refer != SoaClassResolver.AUTO) {
			return refer;
		}
		Class<?>[] intfs = targetClass.getInterfaces();
		if (intfs != null && intfs.length == 1 && !intfs[0].getName().startsWith(Loader.JAVA_PRE)) {
			return intfs[0];
		} else {
			return targetClass;
		}
	}

	protected Class<?> parseRefer(Class<?> targetClass, SoaClassSpec sc) {
		if (sc != null) {
			return sc.refer();
		}
		if (this.autoMatcher.test(targetClass.getName())) {
			return SoaClassResolver.AUTO;
		}
		return null;
	}

}
