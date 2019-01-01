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
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.yx.bean.Box;
import org.yx.exception.SumkException;

public class ProxyClassFactory {

	public static Class<?> proxyIfNeed(Class<?> clz) throws Exception {
		Map<String, Method> aopMethods = new HashMap<>();
		Method[] bethods = clz.getDeclaredMethods();
		for (Method m : bethods) {
			if (!AsmUtils.canProxy(m.getModifiers())) {
				continue;
			}
			if (m.getAnnotation(Box.class) == null) {
				continue;
			}
			if (aopMethods.put(m.getName(), m) != null) {
				SumkException.throwException(-2321435, "box method [" + m.getName() + "] duplicate in one class");
			}
		}
		if (aopMethods.isEmpty()) {
			return clz;
		}

		ClassReader cr = new ClassReader(AsmUtils.openStreamForClass(clz.getName()));

		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);

		String newClzName = AsmUtils.proxyCalssName(clz);
		ProxyClassVistor cv = new ProxyClassVistor(cw, newClzName, clz, aopMethods);
		cr.accept(cv, Vars.ASM_VER);
		return AsmUtils.loadClass(newClzName, cw.toByteArray());
	}
}
