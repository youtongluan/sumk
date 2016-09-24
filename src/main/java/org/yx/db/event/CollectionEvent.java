package org.yx.db.event;

import org.yx.listener.DBEvent;

public class CollectionEvent extends DBEvent {

	private static final long serialVersionUID = 534756756L;

	private Object para;

	public CollectionEvent(Object source, Object para, String type) {
		super(source, type, EventOperate.LIST, null);
		this.para = para;
	}

	/**
	 * 获取列表时所用到的参数，大多数情况下是*Page类型
	 * 
	 * @return
	 */
	public Object getPara() {
		return para;
	}

}