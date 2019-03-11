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

import java.util.Collection;

import org.yx.annotation.Bean;
import org.yx.annotation.db.Cached;
import org.yx.common.StartConstants;
import org.yx.common.SumkLogs;
import org.yx.common.matcher.MatcherFactory;
import org.yx.common.matcher.TextMatcher;
import org.yx.conf.AppInfo;
import org.yx.db.Cachable;
import org.yx.exception.SumkException;

public class BeanFactory extends AbstractBeanListener {

	private boolean cachedScan;
	private boolean useRedisAsCache;

	private final TextMatcher excludeMatcher;

	public BeanFactory() {
		super(AppInfo.get(StartConstants.IOC_PACKAGES));
		this.cachedScan = AppInfo.getBoolean("sumk.ioc.cached.enable", true);
		this.useRedisAsCache = AppInfo.getBoolean("sumk.dao.cache", true);
		excludeMatcher = MatcherFactory.createWildcardMatcher(AppInfo.get("sumk.ioc.exclude", null), 2);
		SumkLogs.IOC_LOG.info("bean exclude matcher:{}", excludeMatcher);
	}

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			String clzName = clz.getName();
			if (excludeMatcher.match(clzName)) {
				SumkLogs.IOC_LOG.info("{} excluded", clzName);
				return;
			}

			Bean b = clz.getAnnotation(Bean.class);
			if (b != null) {
				if (FactoryBean.class.isAssignableFrom(clz)) {
					FactoryBean factory = (FactoryBean) clz.newInstance();
					Collection<?> beans = factory.beans();
					putFactoryBean(beans);
				} else {
					InnerIOC.putClass(b.value(), clz);
				}
			}

			if (!this.cachedScan) {
				return;
			}
			Cached c = clz.getAnnotation(Cached.class);
			if (c != null) {
				Object bean = InnerIOC.putClass(Cachable.PRE + BeanPool.resloveBeanName(clz), clz);
				if (!Cachable.class.isInstance(bean)) {
					SumkException.throwException(35423543, clz.getName() + " is not instance of Cachable");
				}
				if (this.useRedisAsCache) {
					Cachable cache = (Cachable) bean;
					cache.setCacheEnable(true);
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			SumkException.throwException(-345365, "IOC error", e);
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

		InnerIOC.putBean(name, obj);
	}

}
