package org.yx.listener;

import java.util.EventObject;

/** 
 * 用于监听所有事件。
 * @see <code>HearAllListener</code>
 */
public class ForAllListenerGroup<T extends EventObject> extends ListenerGroupImpl<T> {

	@Override
	public boolean addListener(Listener<T> listener) {
		if(listener.getTags()!=null){
			return false;
		}
		return super.addListener(listener);
	}

	

}