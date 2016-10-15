package org.yx.asm;

import static org.springframework.asm.Opcodes.ALOAD;
import static org.springframework.asm.Opcodes.ARETURN;
import static org.springframework.asm.Opcodes.ASTORE;
import static org.springframework.asm.Opcodes.ATHROW;
import static org.springframework.asm.Opcodes.DLOAD;
import static org.springframework.asm.Opcodes.DRETURN;
import static org.springframework.asm.Opcodes.DSTORE;
import static org.springframework.asm.Opcodes.DUP;
import static org.springframework.asm.Opcodes.FLOAD;
import static org.springframework.asm.Opcodes.FRETURN;
import static org.springframework.asm.Opcodes.FSTORE;
import static org.springframework.asm.Opcodes.GETSTATIC;
import static org.springframework.asm.Opcodes.GOTO;
import static org.springframework.asm.Opcodes.ICONST_0;
import static org.springframework.asm.Opcodes.ICONST_1;
import static org.springframework.asm.Opcodes.ILOAD;
import static org.springframework.asm.Opcodes.INVOKESPECIAL;
import static org.springframework.asm.Opcodes.INVOKEVIRTUAL;
import static org.springframework.asm.Opcodes.IRETURN;
import static org.springframework.asm.Opcodes.ISTORE;
import static org.springframework.asm.Opcodes.LLOAD;
import static org.springframework.asm.Opcodes.LRETURN;
import static org.springframework.asm.Opcodes.LSTORE;
import static org.springframework.asm.Opcodes.NEW;
import static org.springframework.asm.Opcodes.RETURN;

import java.util.ArrayList;
import java.util.List;

import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;
import org.yx.bean.Box;
import org.yx.common.AopExcutor;
import org.yx.conf.AppInfo;

public class ProxyMethodWritor {
	private static int SINGLE = AppInfo.getInt("sumk.aop.single", 1);
	private static int WIDTH = AppInfo.getInt("sumk.aop.width", 2);

	private static int load(MethodVisitor mv, Object type, int frameIndex) {
		if (Opcodes.INTEGER.equals(type)) {
			mv.visitVarInsn(ILOAD, frameIndex);
			return frameIndex + SINGLE;
		}
		if (Opcodes.LONG.equals(type)) {
			mv.visitVarInsn(LLOAD, frameIndex);
			return frameIndex + WIDTH;
		}
		if (Opcodes.FLOAT.equals(type)) {
			mv.visitVarInsn(FLOAD, frameIndex);
			return frameIndex + SINGLE;
		}
		if (Opcodes.DOUBLE.equals(type)) {
			mv.visitVarInsn(DLOAD, frameIndex);
			return frameIndex + WIDTH;
		}
		mv.visitVarInsn(ALOAD, frameIndex);
		return frameIndex + SINGLE;
	}

	private static void store(MethodVisitor mv, Object type, int frameIndex) {
		if (Opcodes.INTEGER.equals(type)) {
			mv.visitVarInsn(ISTORE, frameIndex);
		} else if (Opcodes.LONG.equals(type)) {
			mv.visitVarInsn(LSTORE, frameIndex);
		} else if (Opcodes.FLOAT.equals(type)) {
			mv.visitVarInsn(FSTORE, frameIndex);
		} else if (Opcodes.DOUBLE.equals(type)) {
			mv.visitVarInsn(DSTORE, frameIndex);
		} else {
			mv.visitVarInsn(ASTORE, frameIndex);
		}
	}

	private static void jReturn(MethodVisitor mv, Object type) {
		if (Opcodes.INTEGER.equals(type)) {
			mv.visitInsn(IRETURN);
		} else if (Opcodes.LONG.equals(type)) {
			mv.visitInsn(LRETURN);
		} else if (Opcodes.FLOAT.equals(type)) {
			mv.visitInsn(FRETURN);
		} else if (Opcodes.DOUBLE.equals(type)) {
			mv.visitInsn(DRETURN);
		} else {
			mv.visitInsn(ARETURN);
		}
	}

	/**
	 * 
	 * @param mv
	 * @param argTypes
	 * @param frameIndex
	 *            当前已经用到的索引，将从它的下一个用起
	 */
	private static void loadArgs(MethodVisitor mv, List<Object> argTypes, int frameIndex) {
		frameIndex++;
		for (Object argType : argTypes) {
			frameIndex = load(mv, argType, frameIndex);
		}
	}

	private static int argLength(List<Object> argTypes) {
		int size = 0;
		for (Object type : argTypes) {
			if (Opcodes.LONG.equals(type) || Opcodes.DOUBLE.equals(type)) {
				size += WIDTH;
			} else {
				size += SINGLE;
			}
		}
		return size;
	}

	private static final String AOPEXCUTOR = Type.getInternalName(AopExcutor.class);

	public static void write(MethodVisitor mv, AsmMethod asmMethod) {
		if (asmMethod.desc.endsWith(")V")) {
			writeVoid(mv, asmMethod);
		} else {
			writeWithReturn(mv, asmMethod);
		}
	}

	public static void writeVoid(MethodVisitor mv, AsmMethod asmMethod) {
		Box db = asmMethod.method.getAnnotation(Box.class);
		if (db == null) {
			return;
		}
		String superowener = Type.getInternalName(asmMethod.superClz);
		String currentClz = asmMethod.currentClz.replace('.', '/');

		List<Object> argTypes = AsmUtils.getImplicitFrame(
				asmMethod.desc.substring(asmMethod.desc.indexOf("(") + 1, asmMethod.desc.indexOf(")")));
		int localVariableIndex = argLength(argTypes);

		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
		Label l3 = new Label();
		Label l4 = new Label();
		mv.visitTryCatchBlock(l0, l3, l4, null);
		mv.visitTypeInsn(NEW, AOPEXCUTOR);
		mv.visitInsn(DUP);
		mv.visitInsn(db.embed() ? ICONST_1 : ICONST_0);
		mv.visitMethodInsn(INVOKESPECIAL, AOPEXCUTOR, "<init>", "(Z)V", false);
		mv.visitVarInsn(ASTORE, localVariableIndex + 1);
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitLdcInsn(db.dbName());
		mv.visitFieldInsn(GETSTATIC, "org/yx/db/DBType", db.dbType().toString(), "Lorg/yx/db/DBType;");
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "begin", "(Ljava/lang/String;Lorg/yx/db/DBType;)V", false);
		mv.visitVarInsn(ALOAD, 0);

		loadArgs(mv, argTypes, 0);
		mv.visitMethodInsn(INVOKESPECIAL, superowener, asmMethod.name, asmMethod.desc, false);

		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "commit", "()V", false);
		mv.visitLabel(l1);
		Label l5 = new Label();
		mv.visitJumpInsn(GOTO, l5);
		mv.visitLabel(l2);

		List<Object> list = new ArrayList<Object>();
		list.add(currentClz);
		list.addAll(argTypes);
		list.add(AOPEXCUTOR);
		Object[] frames = list.toArray(new Object[list.size()]);
		mv.visitFrame(Opcodes.F_FULL, frames.length, frames, 1, new Object[] { "java/lang/Exception" });
		mv.visitVarInsn(ASTORE, localVariableIndex + 2);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitVarInsn(ALOAD, localVariableIndex + 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "rollback", "(Ljava/lang/Throwable;)V", false);
		mv.visitLabel(l3);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "close", "()V", false);
		Label l6 = new Label();
		mv.visitJumpInsn(GOTO, l6);
		mv.visitLabel(l4);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "java/lang/Throwable" });
		mv.visitVarInsn(ASTORE, localVariableIndex + 3);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "close", "()V", false);
		mv.visitVarInsn(ALOAD, localVariableIndex + 3);
		mv.visitInsn(ATHROW);
		mv.visitLabel(l5);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "close", "()V", false);
		mv.visitLabel(l6);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitInsn(RETURN);
		mv.visitMaxs(Math.max(3, localVariableIndex + 1), 4 + localVariableIndex);
		mv.visitEnd();
	}

	public static void writeWithReturn(MethodVisitor mv, AsmMethod asmMethod) {
		Box db = asmMethod.method.getAnnotation(Box.class);
		if (db == null) {
			return;
		}
		String superowener = Type.getInternalName(asmMethod.superClz);
		String currentClz = asmMethod.currentClz.replace('.', '/');
		List<Object> argTypes = AsmUtils.getImplicitFrame(
				asmMethod.desc.substring(asmMethod.desc.indexOf("(") + 1, asmMethod.desc.indexOf(")")));
		Object returnType = AsmUtils.getImplicitFrame(asmMethod.desc.substring(asmMethod.desc.indexOf(")") + 1)).get(0);
		int localVariableIndex = argLength(argTypes);

		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
		Label l3 = new Label();
		mv.visitTryCatchBlock(l0, l1, l3, null);
		Label l4 = new Label();
		mv.visitTryCatchBlock(l2, l4, l3, null);
		mv.visitTypeInsn(NEW, AOPEXCUTOR);
		mv.visitInsn(DUP);
		mv.visitInsn(db.embed() ? ICONST_1 : ICONST_0);
		mv.visitMethodInsn(INVOKESPECIAL, AOPEXCUTOR, "<init>", "(Z)V", false);
		mv.visitVarInsn(ASTORE, localVariableIndex + 1);
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitLdcInsn(db.dbName());
		mv.visitFieldInsn(GETSTATIC, "org/yx/db/DBType", db.dbType().toString(), "Lorg/yx/db/DBType;");
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "begin", "(Ljava/lang/String;Lorg/yx/db/DBType;)V", false);
		mv.visitVarInsn(ALOAD, 0);

		loadArgs(mv, argTypes, 0);
		mv.visitMethodInsn(INVOKESPECIAL, superowener, asmMethod.name, asmMethod.desc, false);
		int methodReturnFrameIndex = localVariableIndex + 2;
		store(mv, returnType, methodReturnFrameIndex);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "commit", "()V", false);

		mv.visitLabel(l1);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "close", "()V", false);
		load(mv, returnType, methodReturnFrameIndex);
		jReturn(mv, returnType);
		mv.visitLabel(l2);

		List<Object> list = new ArrayList<Object>();
		list.add(currentClz);
		list.addAll(argTypes);
		list.add(AOPEXCUTOR);
		Object[] frames = list.toArray(new Object[list.size()]);

		mv.visitFrame(Opcodes.F_FULL, frames.length, frames, 1, new Object[] { "java/lang/Exception" });
		mv.visitVarInsn(ASTORE, localVariableIndex + 2);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitVarInsn(ALOAD, localVariableIndex + 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "rollback", "(Ljava/lang/Throwable;)V", false);
		mv.visitLabel(l4);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "close", "()V", false);
		Label l5 = new Label();
		mv.visitJumpInsn(GOTO, l5);
		mv.visitLabel(l3);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "java/lang/Throwable" });
		mv.visitVarInsn(ASTORE, localVariableIndex + 3);
		mv.visitVarInsn(ALOAD, localVariableIndex + 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPEXCUTOR, "close", "()V", false);
		mv.visitVarInsn(ALOAD, localVariableIndex + 3);
		mv.visitInsn(ATHROW);
		mv.visitLabel(l5);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitTypeInsn(NEW, "org/yx/exception/SystemException");
		mv.visitInsn(DUP);
		mv.visitLdcInsn(new Integer(-364533425));
		mv.visitLdcInsn("you are locky to see me^_^");
		mv.visitMethodInsn(INVOKESPECIAL, "org/yx/exception/SystemException", "<init>", "(ILjava/lang/String;)V",
				false);
		mv.visitInsn(ATHROW);
		mv.visitMaxs(Math.max(4, localVariableIndex + 1), 4 + localVariableIndex);
		mv.visitEnd();
	}
}
