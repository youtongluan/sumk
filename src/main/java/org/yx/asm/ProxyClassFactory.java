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
import org.yx.annotation.Box;
import org.yx.conf.Const;

public class ProxyClassFactory {

	public static Class<?> proxyIfNeed(Class<?> clz) throws Exception {
		List<Method> aopMethods = new ArrayList<>();

		Method[] bethods = clz.getMethods();
		for (Method m : bethods) {
			if (!AsmUtils.canProxy(m.getModifiers()) || m.getDeclaringClass().isInterface()) {
				continue;
			}

			if (m.getAnnotation(Box.class) == null) {
				continue;
			}
			if (!aopMethods.contains(m)) {
				aopMethods.add(m);
			}
		}

		bethods = clz.getDeclaredMethods();
		for (Method m : bethods) {

			if (!AsmUtils.canProxy(m.getModifiers()) || (m.getModifiers() & Modifier.PROTECTED) == 0) {
				continue;
			}

			if (m.getAnnotation(Box.class) == null) {
				continue;
			}
			if (!aopMethods.contains(m)) {
				aopMethods.add(m);
			}
		}

		if (aopMethods.isEmpty()) {
			return clz;
		}

		ClassReader cr = new ClassReader(AsmUtils.openStreamForClass(clz.getName()));

		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);

		String newClzName = AsmUtils.proxyCalssName(clz);
		ProxyClassVistor cv = new ProxyClassVistor(cw, newClzName, clz, aopMethods);
		cr.accept(cv, Const.ASM_VERSION);
		return AsmUtils.loadClass(newClzName, cw.toByteArray());
	}
}
