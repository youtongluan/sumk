package org.yx.common.listener;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import org.yx.log.Log;
import org.yx.util.CollectionUtil;
import org.yx.util.Task;

/**
 * 它的bean统一有框架创建
 */
public class EventBus {

	private final SumkListener[] listeners;
	private final Executor executor;

	public EventBus(List<SumkListener> list, Executor executor) {
		list.sort(null);
		this.listeners = list.toArray(new SumkListener[list.size()]);
		this.executor = Objects.requireNonNull(executor);
	}

	public void publishBatch(List<?> events) {
		for (SumkListener l : listeners) {
			try {
				l.listenBatch(events);
			} catch (Throwable e) {
				Log.get("sumk.event").error(l + "批量执行出错," + e.getLocalizedMessage(), e);
			}
		}
	}

	public void publish(Object event) {
		for (SumkListener l : listeners) {
			try {
				l.listen(event);
			} catch (Exception e) {
				Log.get("sumk.event").error(l + "执行出错," + e.getLocalizedMessage(), e);
			}
		}
	}

	public void asyncPublishBatch(List<?> events) {
		Task.executeWithCurrentContext(this.executor, () -> this.publishBatch(events));
	}

	public void asyncPublish(Object event) {
		Task.executeWithCurrentContext(this.executor, () -> this.publish(event));
	}

	public List<SumkListener> listeners() {
		return CollectionUtil.unmodifyList(listeners);
	}

	public Executor executor() {
		return this.executor;
	}

}
