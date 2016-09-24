package org.yx.rpc.server.start;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.yx.asm.AsmUtils;
import org.yx.asm.Vars;
import org.yx.bean.InnerIOC;
import org.yx.log.Log;
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ActionInfo;
import org.yx.rpc.MethodInfo;
import org.yx.rpc.SOA;

class SoaFactory {
	private SoaNameResolver nameResolver = new SoaNameResolver();

	public void resolve(Class<?> clz) throws IOException, ClassNotFoundException {
		Object obj = null;
		for (final Method m : clz.getMethods()) {
			if (AsmUtils.isFilted(m.getName())) {
				continue;
			}
			SOA act = m.getAnnotation(SOA.class);
			if (act == null) {
				continue;
			}
			String soaName = nameResolver.solve(clz, m, act.value());
			if (ActionHolder.getActionInfo(soaName) != null) {
				Log.get("SYS.40").error(soaName + "已经存在");
				continue;
			}
			if (obj == null) {
				try {
					obj = InnerIOC.getOrCreate(clz);
				} catch (Exception e) {
					Log.get("SYS.41").error(e.getMessage(), e);
				}
			}
			int argSize = m.getParameterTypes().length;
			if (argSize == 0) {
				Log.get("SYS.42").trace("{}解析出来的空方法：{}()", clz.getName(), m.getName());
				ActionHolder.putActInfo(soaName, new ActionInfo(obj, m, null, null, null, act));
				continue;
			}
			ClassReader cr = new ClassReader(clz.getName());
			MethodInfoClassVisitor cv = new MethodInfoClassVisitor(m);
			cr.accept(cv, 0);
			MethodInfo mInfo = new MethodInfo(m, cv.argNames.toArray(new String[0]), cv.descriptor,
					cv.signatures.toArray(new String[0]));
			Class<?> argClz = AsmUtils.CreateArgPojo(clz.getName(), mInfo);
			ActionHolder.putActInfo(soaName,
					new ActionInfo(obj, m, argClz, mInfo.getArgNames(), m.getParameterTypes(), act));
		}

	}

}

class MethodInfoClassVisitor extends ClassVisitor {

	final List<String> argNames;
	final List<String> signatures;
	String[] descriptor;
	private final Method m;

	/**
	 * 
	 * @arg m
	 * @arg argNames 用于将参数名称传回去
	 */
	public MethodInfoClassVisitor(Method m) {
		super(Vars.ASM_VER);
		this.m = m;
		Type[] tys = Type.getArgumentTypes(m);
		this.argNames = new ArrayList<String>(tys.length);
		this.signatures = new ArrayList<String>(tys.length);
		descriptor = new String[tys.length];
		for (int i = 0; i < tys.length; i++) {
			descriptor[i] = tys[i].getDescriptor();
		}
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		final Type[] args = Type.getArgumentTypes(desc);

		if (!name.equals(m.getName()) || !AsmUtils.sameType(args, m.getParameterTypes())) {
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
		return new MethodVisitor(Vars.ASM_VER, v) {

			@Override
			public void visitLocalVariable(String name, String desc, String signature, Label start, Label end,
					int index) {

				super.visitLocalVariable(name, desc, signature, start, end, index);
				int argSize = m.getParameterTypes().length;
				if ("this".equals(name) || argNames.size() >= argSize || argNames.contains(name)) {
					return;
				}
				int k = argNames.size();
				if (!args[k].getDescriptor().equals(desc)) {

					Log.get("SYS.43").error("###当前参数的类型应该是" + args[k].getDescriptor() + ",但实际却是" + desc);
					return;
				}
				argNames.add(name);
				signatures.add(signature);
			}

		};

	}
}
