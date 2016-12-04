package org.yx.db.event;

import java.util.Map;

public class DeleteEvent extends ModifyEvent {

	private Map<String, Object> map;

	public DeleteEvent(String table, Map<String, Object> where) {
		super(table);
		this.map = where;
	}

	public Map<String, Object> getWhere() {
		return map;
	}

}
