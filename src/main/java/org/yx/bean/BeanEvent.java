package org.yx.bean;

import java.util.EventObject;

/**
 * 用于bean扫描
 * 
 * @author youtl
 *
 */
public class BeanEvent extends EventObject {
	private static final long serialVersionUID = 456354649L;

	public BeanEvent(String className) {
		super(className);
	}

	public String getClassName() {
		return (String) this.source;
	}

}
