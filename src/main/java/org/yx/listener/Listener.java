package org.yx.listener;

import java.util.EventObject;

public interface Listener<T extends EventObject> {
	/**
	 * 是否接受当前的事件
	 * 
	 * @param event
	 * @return
	 */
	boolean accept(T event);

	void listen(T event);

	/**
	 * 所属group的标签,null表示监听全部，空数组表示全部不监听
	 * 
	 * @return
	 */
	String[] getTags();
}
