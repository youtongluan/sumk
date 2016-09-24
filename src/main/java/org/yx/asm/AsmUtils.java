package org.yx.asm;

import static org.springframework.asm.Opcodes.ACC_PUBLIC;

import org.springframework.asm.ClassWriter;
import org.springframework.asm.Type;
import org.yx.rpc.MethodInfo;
import org.yx.rpc.server.intf.ActionContext;

public class AsmUtils {

	private static String[] blanks = new String[] { "getClass", "wait", "equals", "notify", "notifyAll", "toString",
			"hashCode" };

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

	public static MyClassLoader myClassLoader = new MyClassLoader();

	/**
	 * 生成参数的对象，如果参数个数为空，就返回null
	 * 
	 * @param clzName
	 * @param methodName
	 * @param p
	 * @return
	 */
	public static Class<?> CreateArgPojo(String clzName, MethodInfo p) {
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

		return myClassLoader.defineClass(fullName, b);

	}

}
