package org.yx.asm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.yx.log.Log;

public class MethodInfoClassVisitor extends ClassVisitor {

	public final List<String> argNames;
	public final List<String> signatures;
	public String[] descriptor;
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

					Log.get("SYS.20")
							.error("current desc should be " + args[k].getDescriptor() + ",but really is " + desc);
					return;
				}
				argNames.add(name);
				signatures.add(signature);
			}

		};

	}
}