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
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.concurrent.atomic.AtomicInteger;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class ArgPojos {
	private static AtomicInteger ai = new AtomicInteger(1000);

	@SuppressWarnings("unchecked")
	public static Class<? extends ArgPojo> create(String clzName, MethodDesc p) throws Exception {
		if (p.getArgNames() == null || p.getArgNames().length == 0) {
			return null;
		}
		String fullName = clzName + "_" + p.getMethod().getName();
		fullName = fullName.replace('.', '/') + "_" + ai.incrementAndGet();

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cw.visit(Vars.JVM_VERSION, ACC_PUBLIC | ACC_SUPER, fullName, null, "java/lang/Object",
				new String[] { "org/yx/asm/ArgPojo" });
		Arg[] args = new Arg[p.getArgNames().length];
		for (int i = 0; i < args.length; i++) {
			String arg = p.getArgNames()[i];
			String desc = p.getDescs()[i];
			args[i] = new Arg(arg, desc);
			cw.visitField(ACC_PUBLIC, arg, desc, p.getSignatures()[i], null).visitEnd();
		}

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();

		mv = cw.visitMethod(ACC_PUBLIC, "params", "()[Ljava/lang/Object;", null, null);
		mv.visitCode();
		buildArgArray(fullName, mv, args, p.getMethod().getParameterTypes());
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 0);
		mv.visitEnd();

		cw.visitEnd();

		return (Class<? extends ArgPojo>) AsmUtils.loadClass(fullName.replace('/', '.'), cw.toByteArray());

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

	private static class Arg {
		String name;
		String desc;

		private Arg(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}

	}
}
