package org.yx.rpc;

import java.lang.reflect.Method;

/**
 * 方法的名称，参数信息等。用于生成参数对象
 * @author youtl
 *
 */
public final class MethodInfo {
	
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
	public MethodInfo(Method method, String[] argNames, String[] descs, String[] signatures) {
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
