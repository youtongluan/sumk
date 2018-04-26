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
package org.yx.common;

import java.lang.reflect.Method;

public final class MethodDesc {

	private Method method;
	private String[] argNames;
	private String[] descs;
	private String[] signatures;

	public String[] getArgNames() {
		return argNames;
	}

	public String[] getDescs() {
		return descs;
	}

	public String[] getSignatures() {
		return signatures;
	}

	public MethodDesc(Method method, String[] argNames, String[] descs, String[] signatures) {
		super();
		this.method = method;
		this.argNames = argNames;
		this.descs = descs;
		this.signatures = signatures;
	}

	public Method getMethod() {
		return method;
	}

}
