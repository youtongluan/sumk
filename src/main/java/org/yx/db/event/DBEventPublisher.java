package org.yx.db.event;

import org.yx.db.listener.DBListener;
import org.yx.listener.ListenerGroup;
import org.yx.listener.ListenerGroupImpl;

/**
 * 监听器对外提供的接口
 *
 */
public class DBEventPublisher {

	private static ListenerGroup<DBEvent> group = new ListenerGroupImpl<>();

	/**
	 * 发布事件
	 * 
	 * @param event
	 */
	public static void publish(DBEvent event) {
		group.listen(event);
	}

	/**
	 * 添加监听器
	 * 
	 * @param listener
	 * @return
	 */
	public static synchronized boolean addListener(DBListener<DBEvent> listener) {
		group.addListener(listener);
		return true;
	}

	/**
	 * 移除监听器.如果监听器不存在，就返回null
	 * 
	 * @return
	 */
	public static synchronized void removeListener(DBListener<DBEvent> listener) {
		group.removeListener(listener);
	}

}