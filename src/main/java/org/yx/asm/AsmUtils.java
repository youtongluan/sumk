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

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

public final class AsmUtils {

	private static String[] blanks = new String[] { "getClass", "wait", "equals", "notify", "notifyAll", "toString",
			"hashCode" };

	private static ConcurrentHashMap<String, Class<?>> clzMap = new ConcurrentHashMap<>();
	private static Method defineClass;

	static {
		try {
			defineClass = getMethod(ClassLoader.class, "defineClass",
					new Class<?>[] { String.class, byte[].class, int.class, int.class });
			defineClass.setAccessible(true);
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

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

	public static boolean isFilted(String method) {
		for (String m : blanks) {
			if (m.equals(method)) {
				return true;
			}
		}
		return false;
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

	public static Class<?> createArgPojo(String clzName, MethodDesc p) throws Exception {
		String fullName = clzName + "_" + p.getMethod().getName();
		if (p.getArgNames() == null || p.getArgNames().length == 0) {
			return null;
		}

		ClassWriter cw = new ClassWriter(0);
		cw.visit(Vars.JVM_VERSION, ACC_PUBLIC, fullName.replace('.', '/'), null, "java/lang/Object", null);

		int argCount = 0;
		for (int i = 0; i < p.getArgNames().length; i++) {

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

	public static MethodDesc buildMethodDesc(String classFullName, Method m) throws IOException {
		ClassReader cr = new ClassReader(openStreamForClass(classFullName));
		MethodInfoClassVisitor cv = new MethodInfoClassVisitor(m);
		cr.accept(cv, 0);
		return new MethodDesc(m, cv.argNames.toArray(new String[cv.argNames.size()]), cv.descriptor,
				cv.signatures.toArray(new String[cv.signatures.size()]));
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

		String clzOutPath = AppInfo.get("sumk.aop.debug.output");
		if (clzOutPath != null && clzOutPath.length() > 0) {
			try {
				File f = new File(clzOutPath, fullName + ".class");
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(b);
				fos.close();
			} catch (Exception e) {
				if (Log.isTraceEnable("proxy")) {
					Log.printStack(e);
				}
			}
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
			clz = (Class<?>) defineClass.invoke(loader(), fullName, b, 0, b.length);
			if (clz == null) {
				throw new Exception("cannot load class " + fullName);
			}
			clzMap.put(fullName, clz);
			return clz;
		}
	}

	public static final int BADMODIFIERS = Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE;

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

}
