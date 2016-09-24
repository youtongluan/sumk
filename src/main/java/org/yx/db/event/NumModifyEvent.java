package org.yx.db.event;

import org.springframework.util.StringUtils;
import org.yx.listener.DBEvent;

/**
 * 金额或数量改变的event
 *
 */
public class NumModifyEvent extends DBEvent {

	private static final long serialVersionUID = 1L;

	private String field;

	public NumModifyEvent(Object source, String type, String operate, String id, String field) {
		super(source, type, operate, id);
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public static NumModifyEvent createAdd(String type, String id, String field, Number num) {
		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(field) || num == null || StringUtils.isEmpty(id)) {
			return null;
		}
		return new NumModifyEvent(num, type, EventOperate.ADDNUM, id, field);
	}

}