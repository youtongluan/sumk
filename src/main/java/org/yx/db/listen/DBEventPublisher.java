package org.yx.db.listen;

import java.util.HashMap;
import java.util.Map;

import org.yx.listener.DBEvent;
import org.yx.listener.ForAllListenerGroup;
import org.yx.listener.Listener;
import org.yx.listener.ListenerGroup;
import org.yx.listener.ListenerGroupImpl;
import org.yx.log.Log;

/**
 * 监听器对外提供的接口
 *
 */
public class DBEventPublisher {
	
	private static Map<String,ListenerGroup<DBEvent>> listenerGroups = new HashMap<>();
	private static ForAllListenerGroup<DBEvent> forAllGroup = new ForAllListenerGroup<>();

	/**
	 * 发布事件
	 * 
	 * @param event
	 */
	public static void publish(DBEvent event) {
		forAllGroup.listen(event);
		String type=event.getType();
		ListenerGroup<DBEvent> group=listenerGroups.get(type);
		if(group==null){
			Log.get(DBEventPublisher.class,"publish").debug("{}没有监听类",type);
			return;
		}
		group.listen(event);
	}

	/**
	 * 添加监听器
	 * 
	 * @param listener
	 * @return
	 */
	public static synchronized  boolean addListener(Listener<DBEvent> listener) {
		String[] tags=listener.getTags();
		if(tags==null){
			if (forAllGroup.addListener(listener)) {
				Log.get(DBEventPublisher.class,"addListener").info("{}添加到commonGroup中",listener);
				return true;
			}
			return false;
		}
		for(String tag:tags){
			ListenerGroup<DBEvent> group=listenerGroups.get(tag);
			if(group==null){
				group=new ListenerGroupImpl<DBEvent>();
				listenerGroups.put(tag, group);
			}
			group.addListener(listener);
		}
		return true;
	}

	/**
	 * 移除监听器.如果监听器不存在，就返回null
	 * 
	 * @return
	 */
	public static synchronized  void removeListener(Listener<DBEvent> listener) {
		String[] tags=listener.getTags();
		if(tags==null){
			forAllGroup.removeListener(listener);
			return;
		}
		for(String tag:tags){
			ListenerGroup<DBEvent> group=listenerGroups.get(tag);
			if(group==null){
				continue;
			}
			group.removeListener(listener);
		}
	}

}