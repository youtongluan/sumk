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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

class MethodInfoClassVisitor extends ClassVisitor {

	private final List<MethodParamInfo> infos;

	public MethodInfoClassVisitor(List<Method> methods) {
		super(AsmUtils.asmVersion());
		this.infos = new ArrayList<>(Objects.requireNonNull(methods).size());
		for (Method method : methods) {
			infos.add(createMethodInfo(method));
		}

	}

	private MethodParamInfo createMethodInfo(Method m) {
		Type[] tys = Type.getArgumentTypes(m);

		String[] descriptor = new String[tys.length];
		for (int i = 0; i < tys.length; i++) {
			descriptor[i] = tys[i].getDescriptor();
		}
		return new MethodParamInfo(m, new String[tys.length], descriptor, new String[tys.length]);
	}

	private MethodParamInfo getMethodParamInfo(String methodName, String desc) {
		for (MethodParamInfo mp : this.infos) {
			if (mp.isSameMethod(methodName, desc)) {
				return mp;
			}
		}
		return null;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String methodName, final String desc,
			final String signature, final String[] exceptions) {

		MethodParamInfo info = this.getMethodParamInfo(methodName, desc);
		if (info == null || info.getArgNames().length == 0) {
			return null;
		}
		return new ParseParamsMethodVisitor(AsmUtils.asmVersion(), info);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
	}

	@Override
	public void visitSource(String source, String debug) {
	}

	@Override
	public ModuleVisitor visitModule(String name, int access, String version) {
		return null;
	}

	@Override
	public void visitNestHost(String nestHost) {
	}

	@Override
	public void visitOuterClass(String owner, String name, String descriptor) {
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		return null;
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
		return null;
	}

	@Override
	public void visitAttribute(Attribute attribute) {
	}

	@Override
	public void visitNestMember(String nestMember) {
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		return null;
	}

	@Override
	public void visitEnd() {
	}

	public List<MethodParamInfo> getMethodInfos() {
		for (MethodParamInfo info : this.infos) {
			for (String name : info.getArgNames()) {
				if (StringUtil.isEmpty(name)) {
					Logs.asm().error("{}.{}() has empty name。params:{}", info.getMethod().getClass().getSimpleName(),
							info.getMethod().getName(), Arrays.toString(info.getArgNames()));
					throw new SumkException(362655465, "params not full parsed");
				}
			}
		}
		return this.infos;
	}
}