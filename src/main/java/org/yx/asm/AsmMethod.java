/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

class AsmMethod {
	int access;
	String name;
	String desc;
	String signature;
	String[] exceptions;
	Method method;
	String currentClz;
	Class<?> superClz;

	public AsmMethod(int access, String name, String desc, String signature, String[] exceptions, Method method,
			String currentClz, Class<?> supperClz) {
		super();
		this.access = access;
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.exceptions = exceptions;
		this.method = method;
		this.currentClz = currentClz;
		this.superClz = supperClz;
	}

}
