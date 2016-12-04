package org.yx.db.event;

/**
 * 表示是修改
 * 
 * @author 游夏
 *
 */
public class ModifyEvent extends DBEvent {
	public ModifyEvent(String table) {
		super(table);
	}
}
