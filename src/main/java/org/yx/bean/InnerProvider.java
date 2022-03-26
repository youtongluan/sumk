package org.yx.bean;

import java.util.List;

public class InnerProvider implements BeanProvider {

	@Override
	public <T> List<T> getBeans(String name, Class<T> clz) {
		return InnerIOC.pool.getBeans(name, clz);
	}

	@Override
	public List<String> beanNames() {
		return InnerIOC.beanNames();
	}

	@Override
	public <T> T getBean(String name, Class<T> clz) {
		return InnerIOC.pool.getBean(name, clz);
	}

}
