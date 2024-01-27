package org.yx.bean;

import java.util.List;

public interface BeanProvider {
	<T> List<T> getBeans(String name, Class<T> clz);

	List<String> beanNames();

	<T> T getBean(String name, Class<T> clz);
}
