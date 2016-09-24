package org.yx.listener;

import java.util.EventObject;

public interface ListenerGroup<T extends EventObject> {

	/**
	 * 如果这个组的listen和support的listen的类型一致，就返回true。否则返回false。
	 * 注意：即使返回true，也不代表就添加到group中，因为如果重复的话，也照样没有添加进来
	 * 
	 * @param listner
	 * @return
	 */
	boolean addListener(Listener<T> listener);

	Listener<T> removeListener(Listener<T> listener);

	/**
	 * 监听器的总数
	 * 
	 * @return
	 */
	int size();

	void listen(T event);

}