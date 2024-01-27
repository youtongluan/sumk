package org.yx.common.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.yx.annotation.Bean;
import org.yx.annotation.Priority;
import org.yx.bean.FactoryBean;
import org.yx.bean.IOC;
import org.yx.bean.NamedBean;
import org.yx.log.Log;
import org.yx.main.SumkServer;

@Bean
@Priority(20000)
public class EventBusFactory implements FactoryBean {

	@Override
	public Collection<?> beans() {
		Map<String, List<SumkListener>> map = new HashMap<>();
		for (SumkListener listener : IOC.getBeans(SumkListener.class)) {
			for (String type : listener.acceptType()) {
				if (type == null || (type = type.trim()).isEmpty() || IOC.get(type, EventBus.class) != null) {
					Log.get("sumk.listen").error("{}的EventBus已经存在", type);
					continue;
				}
				List<SumkListener> list = map.get(type);
				if (list == null) {
					list = new ArrayList<>();
					map.put(type, list);
				}
				if (!list.contains(listener)) {
					list.add(listener);
				}
			}
		}
		List<NamedBean> beans = new ArrayList<>();
		for (String type : map.keySet()) {
			Executor ex = SumkServer.getExecutor("sumk.event.executor." + type);
			EventBus bus = new EventBus(map.get(type), ex);
			beans.add(new NamedBean(type, bus));
		}
		return beans;
	}
}
