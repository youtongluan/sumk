package org.yx.db.event;

import java.util.Map;

public class UpdateEvent extends ModifyEvent {

	private Map<String, Object> pojo;
	private boolean fullUpdate;

	/**
	 * 
	 * @param table
	 * @param pojo
	 * @param idMap
	 *            主键列表，包含所有的主键，即使它是null
	 */
	public UpdateEvent(String table, Map<String, Object> pojo, boolean fullUpdate) {
		super(table);
		this.pojo = pojo;
		this.fullUpdate = fullUpdate;
	}

	public Map<String, Object> getPojo() {
		return pojo;
	}

	public boolean isFullUpdate() {
		return fullUpdate;
	}

}
