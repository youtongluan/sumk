package org.yx.bean;

import org.yx.listener.SumkEvent;

/**
 * 用于bean扫描
 * 
 * @author 游夏
 *
 */
public class BeanEvent extends SumkEvent {
	public BeanEvent(Class<?> clz) {
		super(clz);
	}

	public Class<?> clz() {
		Object src = this.source;
		return (Class<?>) src;
	}

}
