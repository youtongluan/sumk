package org.yx.db.event;

import org.yx.listener.SumkEvent;

public class DBEvent extends SumkEvent {
	/**
	 * 表名
	 */
	public String getTable() {
		return (String) this.source;
	}

	public DBEvent(String table) {
		super(table);
	}

}
