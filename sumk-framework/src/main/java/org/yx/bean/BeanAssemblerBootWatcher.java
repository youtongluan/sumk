package org.yx.bean;

import java.util.List;
import java.util.function.Predicate;

import org.yx.annotation.spec.BeanSpec;
import org.yx.annotation.spec.Specs;
import org.yx.common.util.kit.PriorityKits;
import org.yx.log.Logs;

public class BeanAssemblerBootWatcher extends ParallelBootWatcher {

	@Override
	protected void handle(Class<?> clz) throws Exception {
		BeanSpec b = Specs.extractBean(clz);
		BeanRegistry.registerClass(clz, b);
	}

	protected List<List<Class<?>>> priorityList(List<Class<?>> sortedClasses) {
		return PriorityKits.split(sortedClasses);
	}

	@Override
	public List<Class<?>> publish(List<Class<?>> sortedClasses, Predicate<String> optional) throws Exception {
		List<List<Class<?>>> lists = priorityList(sortedClasses);
		this.serialPublish(lists.get(0), optional);
		Logs.ioc().trace("handled lower bean classes:{}", lists.get(0));
		super.publish(lists.get(1), optional);
		this.serialPublish(lists.get(2), optional);
		Logs.ioc().trace("handled higher bean classes:{}", lists.get(2));
		Logs.ioc().debug("full beans:{}", InnerIOC.beans());
		return null;
	}

	@Override
	public int order() {
		return 1000;
	}

}