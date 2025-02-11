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

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;

final class ParamPojoClassFactory {
	private static final AtomicInteger SEED = new AtomicInteger(0);

	final MethodParamInfo p;
	final Method method;

	final String fullName;
	private ClassWriter cw;
	private Arg[] args;

	public ParamPojoClassFactory(MethodParamInfo p) {
		this.p = p;
		method = p.getMethod();
		fullName = "org/ytl/pojo/" + String.join("_", p.getDeclaringClass().getSimpleName(), method.getName(),
				"" + SEED.incrementAndGet());
	}

	@SuppressWarnings("unchecked")
	public Class<? extends ParamPojo> create() throws Exception {
		if (Logs.asm().isTraceEnabled()) {
			Logs.asm().trace("begin generate paramters pojo :{}", fullName);
		}

		cw = new ClassWriter(AppInfo.getInt("sumk.asm.writer.parampojo.compute", ClassWriter.COMPUTE_MAXS));
		cw.visit(AsmUtils.jvmVersion(), ACC_PUBLIC | ACC_SUPER, fullName, null, "java/lang/Object",
				new String[] { "org/yx/bean/aop/asm/ParamPojo" });
		this.args = new Arg[p.getArgNames().length];
		for (int i = 0; i < args.length; i++) {
			String arg = p.getArgNames()[i];
			String desc = p.getDescs()[i];
			args[i] = new Arg(arg, desc);
			cw.visitField(ACC_PUBLIC, arg, desc, p.getSignatures()[i], null).visitEnd();
		}

		this.buildInit();
		this.buildParams();
		this.buildInvoke();
		this.buildCreateEmpty();
		this.buildSetParams();

		cw.visitEnd();

		return (Class<? extends ParamPojo>) AsmUtils.defineClass(fullName.replace('/', '.'), cw.toByteArray(),
				p.getDeclaringClass().getClassLoader());
	}

	private void buildInit() {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	private void buildParams() {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "params", "()[Ljava/lang/Object;", null, null);
		mv.visitCode();
		buildArgArray(fullName, mv, args, method.getParameterTypes());
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 0);
		mv.visitEnd();
	}

	private static void buildArgArray(String fullName, MethodVisitor mv, Arg[] args, Class<?>[] params) {
		int frameIndex = 1;
		for (int i = 0; i < params.length; i++) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, fullName, args[i].name, args[i].desc);
			frameIndex += WriterHelper.storeToLocalVariable(mv, params[i], frameIndex);
		}
		WriterHelper.buildParamArray(mv, params);
	}

	private void buildCreateEmpty() {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "createEmpty", "()Lorg/yx/bean/aop/asm/ParamPojo;", null, null);
		mv.visitCode();
		if (this.p.getArgNames().length == 0) {
			mv.visitVarInsn(ALOAD, 0);
		} else {
			mv.visitTypeInsn(NEW, this.fullName);
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, this.fullName, "<init>", "()V", false);
		}
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	private void buildInvoke() {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "invoke", "(Ljava/lang/Object;)Ljava/lang/Object;", null,
				new String[] { "java/lang/Throwable" });
		mv.visitCode();
		Class<?> ownerClz = method.getDeclaringClass();
		final String ownerName = ownerClz.getName().replace('.', '/');

		mv.visitLdcInsn(org.objectweb.asm.Type.getType(ownerClz));
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "cast", "(Ljava/lang/Object;)Ljava/lang/Object;",
				false);
		mv.visitTypeInsn(Opcodes.CHECKCAST, ownerName);
		loadObjectFields(fullName, mv, args, method.getParameterTypes());
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ownerName, method.getName(), p.getMethodDesc(), false);
		Class<?> returnType = method.getReturnType();
		if (void.class == method.getReturnType()) {
			mv.visitInsn(Opcodes.ACONST_NULL);
			Logs.asm().debug("{} has no return", fullName);
		} else if (returnType.isPrimitive()) {
			WriterHelper.boxPrimitive(mv, returnType);
		}
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 0);
		mv.visitEnd();
	}

	private static void loadObjectFields(String fullName, MethodVisitor mv, Arg[] args, Class<?>[] params) {
		for (int i = 0; i < params.length; i++) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, fullName, args[i].name, args[i].desc);
		}
	}

	private void buildSetParams() {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "setParams", "([Ljava/lang/Object;)V", null, null);
		mv.visitCode();

		for (int i = 0; i < args.length; i++) {
			Arg arg = args[i];
			Class<?> argType = method.getParameterTypes()[i];
			Label label1 = new Label();
			if (argType.isPrimitive()) {
				mv.visitVarInsn(ALOAD, 1);
				WriterHelper.visitInt(mv, i);
				mv.visitInsn(AALOAD);
				mv.visitJumpInsn(IFNULL, label1);
			}
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			WriterHelper.visitInt(mv, i);
			mv.visitInsn(AALOAD);
			checkCast(mv, argType, arg.desc);
			mv.visitFieldInsn(PUTFIELD, this.fullName, arg.name, arg.desc);
			if (argType.isPrimitive()) {
				mv.visitLabel(label1);
				mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			}
		}

		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 2);
		mv.visitEnd();
	}

	private void checkCast(MethodVisitor mv, Class<?> argType, String desc) {
		if (!argType.isPrimitive()) {
			mv.visitTypeInsn(CHECKCAST, argType.getName().replace('.', '/'));
			return;
		}
		if (argType == char.class) {
			mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
			return;
		}
		if (argType == boolean.class) {
			mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
			return;
		}
		mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", argType.getName() + "Value", "()" + desc, false);
	}

	static class Arg {
		final String name;
		final String desc;

		private Arg(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}
	}
}
