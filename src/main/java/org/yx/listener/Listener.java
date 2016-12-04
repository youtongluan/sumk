package org.yx.listener;

import org.yx.common.Ordered;

public interface Listener<T extends SumkEvent> extends Ordered {

	/**
	 * 是否接受当前的事件
	 * 
	 * @param event
	 * @return
	 */
	boolean accept(SumkEvent event);

	void listen(T event);

	String[] getTags();
}
