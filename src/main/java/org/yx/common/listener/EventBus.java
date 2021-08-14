package org.yx.common.listener;

import java.util.List;

import org.yx.util.CollectionUtil;

/**
 * 它的bean统一有框架创建
 */
public class EventBus {

	private final SumkListener[] listeners;

	public EventBus(List<SumkListener> list) {
		list.sort(null);
		this.listeners = list.toArray(new SumkListener[list.size()]);
	}

	public void publishBatch(List<?> events) {
		for (SumkListener l : listeners) {
			l.listenBatch(events);
		}
	}

	public void publish(Object event) {
		for (SumkListener l : listeners) {
			l.listen(event);
		}
	}

	public List<SumkListener> listeners() {
		return CollectionUtil.unmodifyList(listeners);
	}

}
