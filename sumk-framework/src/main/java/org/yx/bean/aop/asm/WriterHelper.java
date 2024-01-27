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

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DSTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.SIPUSH;

import org.objectweb.asm.MethodVisitor;
import org.yx.conf.AppInfo;

public final class WriterHelper {
	public static final int SINGLE = AppInfo.getInt("sumk.asm.single", 1);
	public static final int WIDTH = AppInfo.getInt("sumk.asm.width", 2);

	public static void visitInt(MethodVisitor mv, int num) {
		if (num >= -1 && num <= 5) {
			mv.visitInsn(ICONST_0 + num);
			return;
		}
		if (num <= Byte.MAX_VALUE && num >= Byte.MIN_VALUE) {
			mv.visitIntInsn(BIPUSH, num);
			return;
		}
		if (num <= Short.MAX_VALUE && num >= Short.MIN_VALUE) {
			mv.visitIntInsn(SIPUSH, num);
			return;
		}
		mv.visitLdcInsn(num);
	}

	public static void buildParamArray(MethodVisitor mv, Class<?>[] params) {
		final int len = params == null ? 0 : params.length;
		visitInt(mv, len);

		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		if (len == 0) {
			return;
		}
		int frameIndex = 1;
		for (int i = 0; i < len; i++) {
			mv.visitInsn(DUP);
			visitInt(mv, i);
			Class<?> argType = params[i];
			frameIndex += loadFromLocalVariable(mv, argType, frameIndex, true);
			mv.visitInsn(AASTORE);
		}
	}

	public static int loadFromLocalVariable(MethodVisitor mv, Class<?> c, int frameIndex, boolean autoBox) {
		if (c == Integer.TYPE) {
			mv.visitVarInsn(ILOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			}
			return SINGLE;
		}
		if (c == Boolean.TYPE) {
			mv.visitVarInsn(ILOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
			}
			return SINGLE;
		}
		if (c == Byte.TYPE) {
			mv.visitVarInsn(ILOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
			}
			return SINGLE;
		}
		if (c == Character.TYPE) {
			mv.visitVarInsn(ILOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
			}
			return SINGLE;
		}
		if (c == Short.TYPE) {
			mv.visitVarInsn(ILOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
			}
			return SINGLE;
		}
		if (c == Float.TYPE) {
			mv.visitVarInsn(FLOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
			}
			return SINGLE;
		}
		if (c == Double.TYPE) {
			mv.visitVarInsn(DLOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
			}
			return WIDTH;
		}
		if (c == Long.TYPE) {
			mv.visitVarInsn(LLOAD, frameIndex);
			if (autoBox) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
			}
			return WIDTH;
		}

		mv.visitVarInsn(ALOAD, frameIndex);
		return SINGLE;

	}

	public static int boxPrimitive(MethodVisitor mv, Class<?> c) {
		if (c == Integer.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			return SINGLE;
		}
		if (c == Boolean.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
			return SINGLE;
		}
		if (c == Byte.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
			return SINGLE;
		}
		if (c == Character.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
			return SINGLE;
		}
		if (c == Short.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
			return SINGLE;
		}
		if (c == Float.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
			return SINGLE;
		}
		if (c == Double.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
			return WIDTH;
		}
		if (c == Long.TYPE) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
			return WIDTH;
		}
		return 0;

	}

	public static int storeToLocalVariable(MethodVisitor mv, Class<?> c, int frameIndex) {
		if (c == Integer.TYPE || c == Boolean.TYPE || c == Byte.TYPE || c == Character.TYPE || c == Short.TYPE) {
			mv.visitVarInsn(ISTORE, frameIndex);
			return SINGLE;
		}
		if (c == Float.TYPE) {
			mv.visitVarInsn(FSTORE, frameIndex);
			return SINGLE;
		}
		if (c == Double.TYPE) {
			mv.visitVarInsn(DSTORE, frameIndex);
			return WIDTH;
		}
		if (c == Long.TYPE) {
			mv.visitVarInsn(LSTORE, frameIndex);
			return WIDTH;
		}

		mv.visitVarInsn(ASTORE, frameIndex);
		return SINGLE;

	}

	public static String boxDesc(String desc) {
		switch (desc.charAt(0)) {
		case 'Z':
			return "Ljava/lang/Boolean;";
		case 'C':
			return "Ljava/lang/Character;";
		case 'B':
			return "Ljava/lang/Byte;";
		case 'S':
			return "Ljava/lang/Short;";
		case 'I':
			return "Ljava/lang/Integer;";
		case 'F':
			return "Ljava/lang/Float;";
		case 'J':
			return "Ljava/lang/Long;";
		case 'D':
			return "Ljava/lang/Double;";
		default:
			return desc;
		}
	}
}
