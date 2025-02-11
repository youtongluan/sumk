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
package org.yx.bean.aop.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.Loader;

public final class AsmUtils {
	private static final Map<ClassLoader, ProxyClassLoader> classLoaders = new ConcurrentHashMap<>();

	public static String proxyCalssName(Class<?> clz) {
		String name = clz.getName();
		int index = name.lastIndexOf('.');
		return name.substring(0, index) + ".sumkbox" + name.substring(index);
	}

	public static int asmVersion() {
		return AppInfo.getInt("sumk.asm.version", Opcodes.ASM7);
	}

	public static int jvmVersion() {
		return AppInfo.getInt("sumk.asm.jvm.version", Opcodes.V1_8);
	}

	public static InputStream openStreamForClass(Class<?> clz) {
		String internalName = clz.getName().replace('.', '/') + ".class";
		return Loader.getResourceAsStream(internalName);
	}

	public static boolean sameType(Type[] types, Class<?>[] clazzes) {

		if (types.length != clazzes.length) {
			return false;
		}

		for (int i = 0; i < types.length; i++) {
			if (!Type.getType(clazzes[i]).equals(types[i])) {
				return false;
			}
		}
		return true;
	}

	public static List<MethodParamInfo> buildMethodInfos(List<Method> methods) throws IOException {
		Map<Class<?>, List<Method>> map = new HashMap<>();
		for (Method m : methods) {
			List<Method> list = map.get(m.getDeclaringClass());
			if (list == null) {
				list = new ArrayList<>();
				map.put(m.getDeclaringClass(), list);
			}
			list.add(m);
		}
		List<MethodParamInfo> ret = new ArrayList<>();
		for (List<Method> ms : map.values()) {
			ret.addAll(buildMethodInfos(ms.get(0).getDeclaringClass(), ms));
		}
		return ret;
	}

	private static List<MethodParamInfo> buildMethodInfos(Class<?> declaringClass, List<Method> methods)
			throws IOException {
		ClassReader cr = new ClassReader(openStreamForClass(declaringClass));
		MethodInfoClassVisitor cv = new MethodInfoClassVisitor(methods);
		cr.accept(cv, 0);
		return cv.getMethodInfos();
	}

	public static Method getMethod(Class<?> clz, String methodName, Class<?>[] paramTypes) {
		while (clz != Object.class) {
			Method[] ms = clz.getDeclaredMethods();
			for (Method m : ms) {
				if (!m.getName().equals(methodName)) {
					continue;
				}
				Class<?>[] paramTypes2 = m.getParameterTypes();
				if (!Arrays.equals(paramTypes2, paramTypes)) {
					continue;
				}
				return m;
			}
			clz = clz.getSuperclass();
		}
		return null;
	}

	private static ProxyClassLoader getProxyClassLoader(ClassLoader originClassLoader) {
		if (originClassLoader == null) {
			originClassLoader = Loader.loader();
		}
		if (originClassLoader instanceof ProxyClassLoader) {
			return (ProxyClassLoader) originClassLoader;
		}
		return classLoaders.computeIfAbsent(originClassLoader, k -> new ProxyClassLoader(k));
	}

	public static Class<?> defineClass(String fullName, byte[] b, ClassLoader originClassLoader) throws Exception {

		String clzOutPath = AppInfo.get("sumk.asm.debug.output");
		if (clzOutPath != null && clzOutPath.length() > 0) {
			try {
				File f = new File(clzOutPath, fullName + ".class");
				try (FileOutputStream fos = new FileOutputStream(f)) {
					fos.write(b);
					fos.flush();
				}
			} catch (Exception e) {
				if (Logs.asm().isTraceEnabled()) {
					Logs.asm().error(e.getLocalizedMessage(), e);
				}
			}
		}
		ProxyClassLoader classLoader = getProxyClassLoader(originClassLoader);
		synchronized (AsmUtils.class) {
			try {
				Class<?> clz = classLoader.loadClass(fullName);
				Logs.asm().debug("{} was defined", fullName);
				return clz;
			} catch (Throwable e) {
				if (!(e instanceof ClassNotFoundException)) {
					Logs.asm().warn(fullName + " 加载失败", e);
				}
			}
			Class<?> clz = classLoader.defineClass(fullName, b);
			if (clz == null) {
				throw new SumkException(235345436, "cannot load class " + fullName);
			}
			return clz;
		}
	}

	public static final int BADMODIFIERS = Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE;

	public static boolean notPublicOnly(int modifiers) {

		return (modifiers & (Modifier.PUBLIC | BADMODIFIERS)) != Modifier.PUBLIC;
	}

	public static boolean canProxy(int modifiers) {
		return (modifiers & BADMODIFIERS) == 0;
	}

	public static List<Object> getImplicitFrame(String desc) {
		List<Object> locals = new ArrayList<>(5);
		if (desc.isEmpty()) {
			return locals;
		}
		int i = 0;
		while (desc.length() > i) {
			int j = i;
			switch (desc.charAt(i++)) {
			case 'Z':
			case 'C':
			case 'B':
			case 'S':
			case 'I':
				locals.add(Opcodes.INTEGER);
				break;
			case 'F':
				locals.add(Opcodes.FLOAT);
				break;
			case 'J':
				locals.add(Opcodes.LONG);
				break;
			case 'D':
				locals.add(Opcodes.DOUBLE);
				break;
			case '[':
				while (desc.charAt(i) == '[') {
					++i;
				}
				if (desc.charAt(i) == 'L') {
					++i;
					while (desc.charAt(i) != ';') {
						++i;
					}
				}
				locals.add(desc.substring(j, ++i));
				break;
			case 'L':
				while (desc.charAt(i) != ';') {
					++i;
				}
				locals.add(desc.substring(j + 1, i++));
				break;
			default:
				break;
			}
		}
		return locals;
	}

	public static Method getSameMethod(Method method, Class<?> otherClass) {
		Class<?> clz = method.getDeclaringClass();
		if (clz == otherClass) {
			return method;
		}
		String methodName = method.getName();
		Class<?>[] argTypes = method.getParameterTypes();

		Method[] proxyedMethods = otherClass.getMethods();
		for (Method proxyedMethod : proxyedMethods) {
			if (proxyedMethod.getName().equals(methodName) && Arrays.equals(argTypes, proxyedMethod.getParameterTypes())
					&& !proxyedMethod.getDeclaringClass().isInterface()) {
				return proxyedMethod;
			}
		}
		return method;
	}

	public static void clearProxyClassLoaders() {
		classLoaders.clear();
	}

	public static HashMap<ClassLoader, ProxyClassLoader> aopClassLoaders() {
		return new HashMap<>(classLoaders);
	}
}
