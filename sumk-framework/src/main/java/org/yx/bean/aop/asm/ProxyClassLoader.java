package org.yx.bean.aop.asm;

public class ProxyClassLoader extends ClassLoader {

	public ProxyClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Class<?> defineClass(String name, byte[] clzByts) {
		return super.defineClass(name, clzByts, 0, clzByts.length);
	}

	@Override
	public String toString() {
		return "ProxyClassLoader [parent :" + getParent() + "]@" + Integer.toHexString(hashCode());
	}

}
