package org.yx.db.event;

import java.util.Map;

public class InsertEvent extends ModifyEvent {

	private Map<String, Object> pojo;

	/**
	 * 
	 * @param table
	 * @param pojo
	 * @param idMap
	 *            主键列表，包含所有的主键，即使它是null
	 */
	public InsertEvent(String table, Map<String, Object> pojo) {
		super(table);
		this.pojo = pojo;
	}

	public Map<String, Object> getPojo() {
		return pojo;
	}

}
