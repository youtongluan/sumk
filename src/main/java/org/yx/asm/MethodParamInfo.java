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

import org.objectweb.asm.Type;

public final class MethodParamInfo {

	private Method method;
	private String[] argNames;
	private String[] descs;
	private String[] signatures;
	private String methodDesc;

	public String[] getArgNames() {
		return argNames;
	}

	public String[] getDescs() {
		return descs;
	}

	public String[] getSignatures() {
		return signatures;
	}

	public MethodParamInfo(Method method, String[] argNames, String[] descs, String[] signatures) {
		this.method = method;
		this.argNames = argNames;
		this.descs = descs;
		this.signatures = signatures;
		this.methodDesc = Type.getMethodDescriptor(method);
	}

	public Method getMethod() {
		return method;
	}

	public String getMethodDesc() {
		return methodDesc;
	}

	public boolean isSameMethod(String methodName, String desc) {

		return methodName.equals(method.getName()) && this.methodDesc.equals(desc)
				&& AsmUtils.sameType(Type.getArgumentTypes(desc), method.getParameterTypes());
	}

	public Type[] getArgumentTypes() {
		return Type.getArgumentTypes(this.methodDesc);
	}

	public Class<?> getDeclaringClass() {
		return this.method.getDeclaringClass();
	}
}
