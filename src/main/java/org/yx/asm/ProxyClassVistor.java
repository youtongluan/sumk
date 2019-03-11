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

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.reflect.Method;
import java.util.Collection;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.yx.annotation.Box;

public class ProxyClassVistor extends ClassVisitor {

	private Class<?> orginClz;
	private Method[] aopMethods;
	private String clzName;

	public ProxyClassVistor(final ClassVisitor cv, String newClzName, Class<?> clz, Collection<Method> aopMethods) {
		super(Vars.ASM_VER, cv);
		this.orginClz = clz;
		this.aopMethods = aopMethods.toArray(new Method[0]);
		clzName = newClzName;

	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

		super.visit(version, access, clzName.replace('.', '/'), signature, name, new String[] { "org/yx/bean/Boxed" });

		MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	private Method queryMethod(String name, String desc) {
		for (Method m : aopMethods) {

			if (m.getName().equals(name) && desc.equals(Type.getMethodDescriptor(m))) {
				return m;
			}
		}
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

		if ("<init>".equals(name)) {
			return null;
		}

		int badModifiers = ACC_STATIC | ACC_FINAL | ACC_ABSTRACT | ACC_PRIVATE;
		if ((access & badModifiers) != 0) {
			return null;
		}
		Method method = queryMethod(name, desc);
		if (method != null) {
			Box dbBiz = method.getAnnotation(Box.class);
			if (dbBiz == null) {
				return null;
			}

			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, null);
			AsmMethod asmMethod = new AsmMethod(access, name, desc, signature, exceptions, method, clzName,
					this.orginClz);
			ProxyMethodWritor.write(mv, asmMethod);

			return null;
		}

		return null;
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		return null;
	}

	@Override
	public void visitAttribute(Attribute attr) {
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		return null;
	}

	@Override
	public void visitEnd() {
		MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "targetRawClass", "()Ljava/lang/Class;",
				"()Ljava/lang/Class<*>;", null);
		mv.visitCode();
		mv.visitLdcInsn(Type.getType(this.orginClz));
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
		super.visitEnd();
	}

}
