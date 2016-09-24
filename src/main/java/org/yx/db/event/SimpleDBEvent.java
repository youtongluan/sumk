package org.yx.db.event;

import org.yx.listener.DBEvent;

/**
 * 简单的任务类型，用于MQ发送等
 * 
 * @author youxia
 *
 */
public class SimpleDBEvent extends DBEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param type
	 *            类型
	 * @param id
	 *            涉及的id
	 */
	public SimpleDBEvent(String type, String id) {
		super(new Object(), type, "S", id);
	}

	/**
	 * 
	 * @param type
	 *            类型
	 * @param id
	 *            涉及的id
	 */
	public SimpleDBEvent(Object obj, String type, String id) {
		super(obj, type, "S", id);
	}

}
