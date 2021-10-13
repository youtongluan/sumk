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
package org.yx.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.yx.annotation.spec.Specs;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;

public final class ProxyClassFactory {

	public static Class<?> proxyIfNeed(Class<?> clz) throws Exception {

		Method[] methods = clz.getDeclaredMethods();
		List<Method> aopMethods = new ArrayList<>(methods.length);
		for (Method m : methods) {

			if (Specs.extractBox(m) == null) {
				continue;
			}
			int modifier = m.getModifiers();
			if (!AsmUtils.canProxy(modifier)) {
				Logs.asm().warn("{}.{}被忽略掉。@Box不支持static、final、abstract、private方法", clz.getName(), m.getName());
				continue;
			}
			if (!Modifier.isPublic(modifier) && !Modifier.isProtected(modifier)) {
				Logs.asm().warn("{}.{}被忽略掉。@Box只支持public或protected方法", clz.getName(), m.getName());
				continue;
			}
			if (!aopMethods.contains(m)) {
				aopMethods.add(m);
			}
		}

		if (aopMethods.isEmpty()) {
			return clz;
		}
		if (Logs.asm().isTraceEnabled()) {
			for (Method m : aopMethods) {
				Logs.asm().trace("{}.{}的@Box代理生效了", clz.getName(), m.getName());
			}
		}

		ClassReader cr = new ClassReader(AsmUtils.openStreamForClass(clz.getName()));

		ClassWriter cw = new ClassWriter(cr, AppInfo.getInt("sumk.asm.writer.box.compute", ClassWriter.COMPUTE_MAXS));

		String newClzName = AsmUtils.proxyCalssName(clz);
		ProxyClassVistor cv = new ProxyClassVistor(cw, newClzName, clz, aopMethods);
		cr.accept(cv, AsmUtils.asmVersion());
		return AsmUtils.loadClass(newClzName, cw.toByteArray());
	}
}
