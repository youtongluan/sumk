package org.yx.db.listener;

import org.yx.bean.Bean;
import org.yx.bean.BeanWrapper;
import org.yx.bean.watcher.BeanWatcher;
import org.yx.db.event.DBEventPublisher;

@SuppressWarnings("rawtypes")
@Bean
public class ListenerBuilder implements BeanWatcher<DBListener> {

	@SuppressWarnings("unchecked")
	@Override
	public void beanPost(BeanWrapper w) {
		Object listener = w.getBean();
		DBEventPublisher.addListener((DBListener) listener);
	}

	@Override
	public Class<DBListener> acceptClass() {
		return DBListener.class;
	}

}
