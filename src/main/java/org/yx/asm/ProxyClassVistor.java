package org.yx.asm;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.reflect.Method;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.yx.bean.Box;

public class ProxyClassVistor extends ClassVisitor {

	private Class<?> orginClz;
	private Map<String, Method> aopMethods;
	private String clzName;

	/**
	 * 
	 * @param cv
	 * @param clz
	 * @param aopMethods
	 *            要进行aop解析的方法，不能重名
	 */
	public ProxyClassVistor(final ClassVisitor cv, String newClzName, Class<?> clz, Map<String, Method> aopMethods) {
		super(Vars.ASM_VER, cv);
		this.orginClz = clz;
		this.aopMethods = aopMethods;
		clzName = newClzName;

	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

		super.visit(version, access, clzName.replace('.', '/'), signature, name, interfaces);

		MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
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
		if (aopMethods.containsKey(name)) {
			Method method = aopMethods.get(name);
			Box dbBiz = method.getAnnotation(Box.class);
			if (dbBiz == null) {
				return null;
			}
			String methodDesc = Type.getMethodDescriptor(method);

			if (!desc.equals(methodDesc)) {
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

}
