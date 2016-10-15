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
