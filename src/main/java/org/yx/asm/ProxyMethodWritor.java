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

import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.yx.asm.WriterHelper.SINGLE;
import static org.yx.asm.WriterHelper.WIDTH;
import static org.yx.asm.WriterHelper.buildParamArray;
import static org.yx.asm.WriterHelper.loadFromLocalVariable;
import static org.yx.asm.WriterHelper.storeToLocalVariable;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.yx.annotation.Box;
import org.yx.bean.AopExcutorFactory;

public final class ProxyMethodWritor {

	private static void jReturn(MethodVisitor mv, Class<?> c) {
		if (c == Integer.TYPE || c == Boolean.TYPE || c == Byte.TYPE || c == Character.TYPE || c == Short.TYPE) {
			mv.visitInsn(IRETURN);
		} else if (c == Float.TYPE) {
			mv.visitInsn(FRETURN);
		} else if (c == Double.TYPE) {
			mv.visitInsn(DRETURN);
		} else if (c == Long.TYPE) {
			mv.visitInsn(LRETURN);
		} else {
			mv.visitInsn(ARETURN);
		}
	}

	private static int argLength(Class<?>[] params) {
		int size = 0;
		for (Class<?> param : params) {
			size += (param == Double.TYPE || param == Long.TYPE) ? WIDTH : SINGLE;
		}
		return size;
	}

	private static class ProxyBuilder {
		private static final String AOP_EXCUTOR = "org/yx/common/AopExcutor";
		private final MethodVisitor mv;
		private final AsmMethod asmMethod;
		private final Class<?>[] params;
		private final Class<?> returnType;
		private final String superowener;

		private final int aopExcutorIndex;

		private void jReturn() {
			ProxyMethodWritor.jReturn(mv, returnType);
		}

		private int storeReuturnToLocalVariable(int frameIndex) {
			return storeToLocalVariable(mv, returnType, frameIndex);
		}

		private void loadArgs() {
			int frameIndex = 1;
			for (Class<?> argType : params) {
				frameIndex += loadFromLocalVariable(mv, argType, frameIndex, false);
			}
		}

		public ProxyBuilder(MethodVisitor mv, AsmMethod asmMethod) {
			this.asmMethod = asmMethod;
			this.mv = mv;
			params = asmMethod.method.getParameterTypes();
			returnType = asmMethod.method.getReturnType();
			superowener = Type.getInternalName(asmMethod.superClz);
			int paramsVariableIndex = argLength(params);
			aopExcutorIndex = paramsVariableIndex + 1;
		}

		private void callSuperMethod() {
			mv.visitVarInsn(ALOAD, 0);
			this.loadArgs();
			mv.visitMethodInsn(INVOKESPECIAL, superowener, asmMethod.name, asmMethod.desc, false);
		}

		private void writeVoidMethod(int key) {
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			Label l3 = new Label();
			Label l4 = new Label();

			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
			mv.visitTryCatchBlock(l0, l1, l3, null);
			mv.visitTryCatchBlock(l2, l4, l3, null);
			mv.visitIntInsn(SIPUSH, key);
			mv.visitMethodInsn(INVOKESTATIC, "org/yx/bean/AopExcutorFactory", "create", "(I)Lorg/yx/common/AopExcutor;",
					false);
			mv.visitVarInsn(ASTORE, this.aopExcutorIndex);
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			buildParamArray(mv, params);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "before", "([Ljava/lang/Object;)V", false);
			this.callSuperMethod();
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "after", "(Ljava/lang/Object;)V", false);
			mv.visitLabel(l1);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "close", "()V", false);
			mv.visitInsn(RETURN);
			mv.visitLabel(l2);
			this.visitFullFrame();
			mv.visitVarInsn(ASTORE, this.aopExcutorIndex + 1);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex + 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "onError", "(Ljava/lang/Throwable;)V", false);
			mv.visitLabel(l4);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "close", "()V", false);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "java/lang/Throwable" });
			mv.visitVarInsn(ASTORE, this.aopExcutorIndex + 2);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "close", "()V", false);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex + 2);
			mv.visitInsn(ATHROW);
			mv.visitLabel(l5);
		}

		public void write() {
			Box box = asmMethod.method.getAnnotation(Box.class);
			int key = AopExcutorFactory.add(box);
			mv.visitCode();

			boolean hasReturn = returnType != null && returnType != Void.TYPE;
			if (hasReturn) {
				this.writeWithReturn(key);
			} else {
				this.writeVoidMethod(key);
			}

			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitTypeInsn(NEW, "org/yx/exception/SumkException");
			mv.visitInsn(DUP);
			mv.visitIntInsn(SIPUSH, 12345);
			mv.visitLdcInsn("execute failed");
			mv.visitMethodInsn(INVOKESPECIAL, "org/yx/exception/SumkException", "<init>", "(ILjava/lang/String;)V",
					false);
			mv.visitInsn(ATHROW);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		private void writeWithReturn(int key) {
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			Label l3 = new Label();
			Label l4 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
			mv.visitTryCatchBlock(l0, l1, l3, null);
			mv.visitTryCatchBlock(l2, l4, l3, null);
			mv.visitIntInsn(SIPUSH, key);
			mv.visitMethodInsn(INVOKESTATIC, "org/yx/bean/AopExcutorFactory", "create", "(I)Lorg/yx/common/AopExcutor;",
					false);
			mv.visitVarInsn(ASTORE, this.aopExcutorIndex);
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			buildParamArray(mv, params);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "before", "([Ljava/lang/Object;)V", false);
			this.callSuperMethod();
			int returnWidth = this.storeReuturnToLocalVariable(this.aopExcutorIndex + 1);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			loadFromLocalVariable(mv, this.returnType, this.aopExcutorIndex + 1, true);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "after", "(Ljava/lang/Object;)V", false);
			mv.visitLabel(l1);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "close", "()V", false);
			loadFromLocalVariable(mv, this.returnType, this.aopExcutorIndex + 1, false);
			this.jReturn();
			mv.visitLabel(l2);
			this.visitFullFrame();
			mv.visitVarInsn(ASTORE, this.aopExcutorIndex + 1);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex + 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "onError", "(Ljava/lang/Throwable;)V", false);
			mv.visitLabel(l4);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "close", "()V", false);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "java/lang/Throwable" });
			mv.visitVarInsn(ASTORE, this.aopExcutorIndex + 1 + returnWidth);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex);
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR, "close", "()V", false);
			mv.visitVarInsn(ALOAD, this.aopExcutorIndex + 1 + returnWidth);
			mv.visitInsn(ATHROW);
			mv.visitLabel(l5);
		}

		private void visitFullFrame() {
			String currentClz = asmMethod.currentClz.replace('.', '/');
			List<Object> argTypes = AsmUtils.getImplicitFrame(
					asmMethod.desc.substring(asmMethod.desc.indexOf("(") + 1, asmMethod.desc.indexOf(")")));
			List<Object> list = new ArrayList<>();
			list.add(currentClz);
			list.addAll(argTypes);
			list.add(AOP_EXCUTOR);
			Object[] frames = list.toArray(new Object[list.size()]);
			mv.visitFrame(Opcodes.F_FULL, frames.length, frames, 1, new Object[] { "java/lang/Throwable" });
		}
	}

	public static void write(MethodVisitor mv, AsmMethod asmMethod) {
		new ProxyBuilder(mv, asmMethod).write();
	}

}
