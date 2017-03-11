/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

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
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.yx.common.MethodInfo;
import org.yx.rpc.server.intf.ActionContext;

public final class AsmUtils {

	private static String[] blanks = new String[] { "getClass", "wait", "equals", "notify", "notifyAll", "toString",
			"hashCode" };

	private static Map<String, Class<?>> clzMap;
	private static Map<Class<?>, Method> loaderDefinds = new HashMap<>();

	public static String proxyCalssName(Class<?> clz) {
		String name = clz.getName();
		int index = name.lastIndexOf(".");
		return name.substring(0, index) + ".sumkbox" + name.substring(index);
	}

	private static ClassLoader loader() {
		ClassLoader load = Thread.currentThread().getContextClassLoader();
		if (load != null) {
			return load;
		}
		return AsmUtils.class.getClassLoader();
	}

	public static InputStream openStreamForClass(String name) {
		String internalName = name.replace('.', '/') + ".class";
		return loader().getResourceAsStream(internalName);
	}

	/**
	 * 
	 * @param method
	 * @return true表示该方法被过滤掉
	 */
	public static boolean isFilted(String method) {
		for (String m : blanks) {
			if (m.equals(method)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断types和clazzes列表是否相等
	 * 
	 * @param types
	 * @param clazzes
	 * @return
	 */
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

	/**
	 * 生成参数的对象，如果参数个数为空，就返回null
	 * 
	 * @param clzName
	 * @param methodName
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public static Class<?> CreateArgPojo(String clzName, MethodInfo p) throws Exception {
		String fullName = clzName + "_" + p.getMethod().getName();
		if (p.getArgNames() == null || p.getArgNames().length == 0) {
			return null;
		}

		ClassWriter cw = new ClassWriter(0);
		cw.visit(Vars.JVM_VERSION, ACC_PUBLIC, fullName.replace('.', '/'), null, "java/lang/Object", null);
		Class<?>[] argTypes = p.getMethod().getParameterTypes();
		int argCount = 0;
		for (int i = 0; i < p.getArgNames().length; i++) {
			if (ActionContext.class.isInstance(argTypes[i])) {
				continue;
			}
			argCount++;
			String arg = p.getArgNames()[i];
			String desc = p.getDescs()[i];
			cw.visitField(ACC_PUBLIC, arg, desc, p.getSignatures()[i], null).visitEnd();
		}
		cw.visitEnd();
		if (argCount == 0) {
			return null;
		}
		byte[] b = cw.toByteArray();

		return loadClass(fullName, b);

	}

	public static MethodInfo createMethodInfo(String classFullName, Method m) throws IOException {
		ClassReader cr = new ClassReader(openStreamForClass(classFullName));
		MethodInfoClassVisitor cv = new MethodInfoClassVisitor(m);
		cr.accept(cv, 0);
		return new MethodInfo(m, cv.argNames.toArray(new String[0]), cv.descriptor,
				cv.signatures.toArray(new String[0]));
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

	public static Class<?> loadClass(String fullName, byte[] b) throws Exception {
		if (clzMap == null) {
			clzMap = new ConcurrentHashMap<>();
		}
		Class<?> clz = clzMap.get(fullName);
		if (clz != null) {
			return clz;
		}
		synchronized (AsmUtils.class) {
			clz = clzMap.get(fullName);
			if (clz != null) {
				return clz;
			}
			Class<?> loaderClz = loader().getClass();
			Method defineClass = loaderDefinds.get(loaderClz);
			if (defineClass == null) {
				defineClass = getMethod(loaderClz, "defineClass",
						new Class<?>[] { String.class, byte[].class, int.class, int.class });
				defineClass.setAccessible(true);
				loaderDefinds.put(loaderClz, defineClass);
			}
			clz = (Class<?>) defineClass.invoke(loader(), fullName, b, 0, b.length);
			if (clz == null) {
				throw new Exception("cannot load class " + fullName);
			}
			clzMap.put(fullName, clz);
			return clz;
		}
	}

	public static final int BADMODIFIERS = Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE;

	/**
	 * true表示不是正常的public方法。<br>
	 * 抽象，静态，final都不算是正常的public
	 * 
	 * @param modifiers
	 * @return
	 */
	public static boolean notPublicOnly(int modifiers) {
		return (modifiers & Modifier.PUBLIC) == 0 || (modifiers & BADMODIFIERS) != 0;
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

	/**
	 * 获取方法所对应的代理类方法
	 * 
	 * @param method
	 *            真实类的方法
	 * @param proxyedClass
	 *            代理它的类，如果该类没有被代理，那么就是自己本身
	 * @return
	 */
	public static Method proxyMethod(Method method, Class<?> proxyedClass) {
		Class<?> clz = method.getDeclaringClass();
		if (clz == proxyedClass) {
			return method;
		}
		String methodName = method.getName();
		Class<?>[] argTypes = method.getParameterTypes();
		Method[] proxyedMethods = proxyedClass.getDeclaredMethods();
		for (Method proxyedMethod : proxyedMethods) {
			if (proxyedMethod.getName().equals(methodName)
					&& Arrays.equals(argTypes, proxyedMethod.getParameterTypes())) {
				return proxyedMethod;
			}
		}
		return method;
	}

}
