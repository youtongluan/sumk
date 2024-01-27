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

import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.yx.bean.aop.asm.WriterHelper.SINGLE;
import static org.yx.bean.aop.asm.WriterHelper.WIDTH;
import static org.yx.bean.aop.asm.WriterHelper.buildParamArray;
import static org.yx.bean.aop.asm.WriterHelper.loadFromLocalVariable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

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
		private static final String AOP_EXCUTOR_CHAIN = "org/yx/bean/aop/AopExecutorChain";

		private final MethodVisitor mv;
		private final AsmMethod asmMethod;
		private final Class<?>[] params;
		private final Class<?> returnType;
		private final String superowener;

		private final int aopExcutorChainIndex;

		private void jReturn() {
			ProxyMethodWritor.jReturn(mv, returnType);
		}

		private void jReturnNULLOrException() {
			if (!returnType.isPrimitive()) {
				mv.visitInsn(ACONST_NULL);
				mv.visitInsn(ARETURN);
			} else {
				mv.visitTypeInsn(NEW, "org/yx/exception/SumkException");
				mv.visitInsn(DUP);
				mv.visitLdcInsn(new Integer(-912753901));
				mv.visitLdcInsn("返回值为原始类型的方法，不支持aop屏蔽异常");
				mv.visitMethodInsn(INVOKESPECIAL, "org/yx/exception/SumkException", "<init>", "(ILjava/lang/String;)V",
						false);
				mv.visitInsn(ATHROW);
			}
		}

		private int storeReuturnToLocalVariable(int frameIndex) {
			return WriterHelper.storeToLocalVariable(mv, returnType, frameIndex);
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
			aopExcutorChainIndex = paramsVariableIndex + 1;
		}

		private void callSuperMethod() {
			mv.visitVarInsn(ALOAD, 0);
			this.loadArgs();
			mv.visitMethodInsn(INVOKESPECIAL, superowener, asmMethod.name, asmMethod.desc, false);
		}

		private void writeVoidMethod(int key) {
			Label l3 = new Label();
			Label label_before_1 = new Label();
			Label label_biz_executed = new Label();
			mv.visitTryCatchBlock(label_before_1, label_biz_executed, l3, "java/lang/Throwable");

			WriterHelper.visitInt(mv, key);
			mv.visitMethodInsn(INVOKESTATIC, "org/yx/bean/InnerIOC", "getAopExecutorChain",
					"(I)Lorg/yx/bean/aop/AopExecutorChain;", false);
			mv.visitVarInsn(ASTORE, this.aopExcutorChainIndex);
			mv.visitLabel(label_before_1);
			mv.visitVarInsn(ALOAD, this.aopExcutorChainIndex);
			buildParamArray(mv, params);
			this.visitBefore();

			this.callSuperMethod();
			mv.visitLabel(label_biz_executed);
			mv.visitVarInsn(ALOAD, this.aopExcutorChainIndex);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ICONST_1);
			this.visitAfter();
			mv.visitInsn(RETURN);

			mv.visitLabel(l3);
			this.visitFullFrame();
			int exceptionIndex = this.aopExcutorChainIndex + 1;
			mv.visitVarInsn(ASTORE, exceptionIndex);
			mv.visitVarInsn(ALOAD, this.aopExcutorChainIndex);
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ALOAD, exceptionIndex);
			mv.visitInsn(ICONST_0);
			visitAfter();
			mv.visitInsn(RETURN);

		}

		public void write() {
			int index = this.asmMethod.excutorSupplierIndex;
			mv.visitCode();

			boolean hasReturn = returnType != null && returnType != Void.TYPE;
			if (hasReturn) {
				this.writeWithReturn(index);
			} else {
				this.writeVoidMethod(index);
			}

			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		private void visitAfter() {
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR_CHAIN, "after", "(Ljava/lang/Object;Ljava/lang/Throwable;Z)V",
					false);
		}

		private void visitBefore() {
			mv.visitMethodInsn(INVOKEVIRTUAL, AOP_EXCUTOR_CHAIN, "before", "([Ljava/lang/Object;)V", false);
		}

		private void writeWithReturn(int key) {
			Label l3 = new Label();
			Label label_before_1 = new Label();
			Label label_biz_executed = new Label();
			mv.visitTryCatchBlock(label_before_1, label_biz_executed, l3, "java/lang/Throwable");
			WriterHelper.visitInt(mv, key);
			mv.visitMethodInsn(INVOKESTATIC, "org/yx/bean/InnerIOC", "getAopExecutorChain",
					"(I)Lorg/yx/bean/aop/AopExecutorChain;", false);
			mv.visitVarInsn(ASTORE, this.aopExcutorChainIndex);
			mv.visitLabel(label_before_1);
			mv.visitVarInsn(ALOAD, this.aopExcutorChainIndex);
			buildParamArray(mv, params);
			this.visitBefore();

			this.callSuperMethod();
			mv.visitLabel(label_biz_executed);
			int returnValueIndex = this.aopExcutorChainIndex + 1;
			storeReuturnToLocalVariable(returnValueIndex);

			mv.visitVarInsn(ALOAD, this.aopExcutorChainIndex);
			loadFromLocalVariable(mv, this.returnType, returnValueIndex, true);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ICONST_1);
			visitAfter();
			loadFromLocalVariable(mv, this.returnType, this.aopExcutorChainIndex + 1, false);
			this.jReturn();

			int exceptionIndex = this.aopExcutorChainIndex + 1;
			mv.visitLabel(l3);
			this.visitFullFrame();
			mv.visitVarInsn(ASTORE, exceptionIndex);
			mv.visitVarInsn(ALOAD, this.aopExcutorChainIndex);
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ALOAD, exceptionIndex);
			mv.visitInsn(ICONST_0);
			visitAfter();

			this.jReturnNULLOrException();

		}

		private void visitFullFrame() {
			String currentClz = asmMethod.currentClz.replace('.', '/');
			List<Object> argTypes = AsmUtils.getImplicitFrame(
					asmMethod.desc.substring(asmMethod.desc.indexOf("(") + 1, asmMethod.desc.indexOf(")")));
			List<Object> list = new ArrayList<>();
			list.add(currentClz);
			list.addAll(argTypes);
			list.add(AOP_EXCUTOR_CHAIN);
			Object[] frames = list.toArray(new Object[list.size()]);
			mv.visitFrame(Opcodes.F_FULL, frames.length, frames, 1, new Object[] { "java/lang/Throwable" });
		}
	}

	public static void write(MethodVisitor mv, AsmMethod asmMethod) {
		new ProxyBuilder(mv, asmMethod).write();
	}

	static class AsmMethod {
		final int access;
		final String name;
		final String desc;
		final String signature;
		final String[] exceptions;
		final Method method;
		final String currentClz;
		final Class<?> superClz;
		final int excutorSupplierIndex;

		public AsmMethod(int access, String name, String desc, String signature, String[] exceptions, Method method,
				int advisors, String currentClz, Class<?> supperClz) {
			this.access = access;
			this.name = name;
			this.desc = desc;
			this.signature = signature;
			this.exceptions = exceptions;
			this.method = method;
			this.currentClz = currentClz;
			this.superClz = supperClz;
			this.excutorSupplierIndex = advisors;
		}

	}

}
