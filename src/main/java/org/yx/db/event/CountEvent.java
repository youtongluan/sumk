package org.yx.db.event;


import org.yx.listener.DBEvent;
public class CountEvent extends DBEvent {

	private static final long serialVersionUID = 3434543445L;


	private Object para;


	public CountEvent(Long count, String type, Object para) {
		super(count, type, EventOperate.COUNT, null);
		this.para=para;
	}


	public Object getPara() {
		return para;
	}
	
	
	
}