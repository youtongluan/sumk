package org.yx.db.event;

import org.yx.listener.DBEvent;

/**
 * 数据变更的event
 *
 */
public class DataModifyEvent extends DBEvent {

	public DataModifyEvent(Object source, String type, String operate, String id) {
		super(source, type, operate, id);
	}

	private static final long serialVersionUID = 1L;

}
