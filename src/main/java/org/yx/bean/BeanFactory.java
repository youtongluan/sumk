/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.yx.annotation.Bean;
import org.yx.common.StartConstants;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public class BeanFactory extends AbstractBeanListener {

	private final Predicate<String> excludeMatcher;

	public BeanFactory() {
		super(AppInfo.get(StartConstants.IOC_PACKAGES));
		this.valid = true;
		this.excludeMatcher = createExcludeMatcher();
		Logs.ioc().debug("bean exclude matcher:{}", excludeMatcher);
	}

	public Predicate<String> excludeMatcher() {
		return this.excludeMatcher;
	}

	protected Predicate<String> createExcludeMatcher() {
		final String name = "sumk.ioc.exclude";

		List<String> list = new ArrayList<>(AppInfo.subMap(name + ".").values());
		String exclude = AppInfo.get(name, null);
		if (exclude != null) {
			list.add(exclude);
		}
		if (list.isEmpty()) {
			return BooleanMatcher.FALSE;
		}
		StringBuilder sb = new StringBuilder();
		for (String v : list) {
			sb.append(v).append(Matchers.SPLIT);
		}
		return Matchers.createWildcardMatcher(sb.toString(), 2);
	}

	@Override
	public void onListen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			String clzName = clz.getName();
			if (excludeMatcher.test(clzName)) {
				Logs.ioc().info("{} excluded", clzName);
				return;
			}

			Bean b = clz.getAnnotation(Bean.class);
			if (b != null) {
				if (FactoryBean.class.isAssignableFrom(clz)) {
					FactoryBean factory = (FactoryBean) Loader.newInstance(clz);
					putFactoryBean(factory.beans());
				} else {
					InnerIOC.putClass(b.value(), clz);
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new SumkException(-345365, "IOC error", e);
		}

	}

	private void putFactoryBean(Collection<?> beans) throws Exception {
		if (beans != null && beans.size() > 0) {
			for (Object obj : beans) {
				putBean(null, obj);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void putBean(String name, Object obj) throws Exception {
		if (obj == null) {
			return;
		}
		Class<?> clz = obj.getClass();
		if (clz == Class.class) {
			InnerIOC.putClass(name, Class.class.cast(obj));
			return;
		}

		if (clz == NamedBean.class) {
			NamedBean named = NamedBean.class.cast(obj);
			this.putBean(named.getBeanName(), named.getBean());
			return;
		}

		if (clz == InterfaceBean.class) {
			InterfaceBean complex = InterfaceBean.class.cast(obj);
			this.putBean(BeanPool.resloveBeanName(complex.getIntf()), complex.getBean());
			return;
		}

		InnerIOC.putBean(name, obj);
	}

}
