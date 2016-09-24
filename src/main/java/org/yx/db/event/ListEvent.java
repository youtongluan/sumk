package org.yx.db.event;

import java.util.Collection;

import org.yx.listener.DBEvent;

/**
 * 用于根据某种id查询的类型.比如productImage，他是根据productID查询的
 * 
 * @author Administrator
 *
 */
public class ListEvent extends DBEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 外键id，非空.多个外键就用PkUtils组合起来使用
	 */
	private String foreignId;

	public String getForeignId() {
		return foreignId;
	}

	public ListEvent(Collection<?> list, String type, String foreignId) {
		super(list, type, EventOperate.LISTByParent, null);
		this.foreignId = foreignId;
	}

	@Override
	public String toString() {
		return "ListEvent [foreignId=" + foreignId + ", getType()=" + getType() + "]";
	}

}
