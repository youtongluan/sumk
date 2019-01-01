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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.common.LogType;
import org.yx.common.StartConstants;
import org.yx.conf.AppInfo;
import org.yx.db.Cachable;
import org.yx.db.Cached;
import org.yx.exception.SumkException;
import org.yx.util.StringUtil;

public class BeanFactory extends AbstractBeanListener {

	private boolean cachedScan;
	private boolean useRedis;

	private Set<String> excludes;

	private List<String> excludeStarts;

	private List<String> excludeEnds;

	public BeanFactory() {
		super(AppInfo.get(StartConstants.IOC_PACKAGES));
		this.cachedScan = AppInfo.getBoolean("sumk.ioc.cached.enable", true);
		this.useRedis = AppInfo.getBoolean("sumk.dao.cache", true);
		String noProxys = AppInfo.get("sumk.ioc.exclude", null);
		if (noProxys == null) {
			return;
		}
		Set<String> exact = new HashSet<>();
		Set<String> start = new HashSet<>();
		Set<String> end = new HashSet<>();
		String[] noProxyArray = StringUtil.splitByComma(noProxys);
		final String WILDCARD = "*";
		for (String s : noProxyArray) {
			if ((s = s.trim()).isEmpty()) {
				continue;
			}

			if (!s.contains(WILDCARD)) {
				exact.add(s);
				continue;
			}

			if (s.indexOf(WILDCARD) != s.lastIndexOf(WILDCARD)) {
				LogType.IOC_LOG.error("{}出现了2次*,本配置将被忽略", s);
				continue;
			}
			if (s.length() == 1) {
				LogType.IOC_LOG.error("{}的长度太短，将被忽略", s);
				continue;
			}
			if (s.startsWith(WILDCARD)) {
				end.add(s.substring(1));
			} else if (s.endsWith(WILDCARD)) {
				start.add(s.substring(0, s.length() - 1));
			} else {
				LogType.IOC_LOG.error("{}的*不是出现在头尾，将被忽略", s);
			}
		}
		if (exact.size() > 0) {
			excludes = exact;
			LogType.IOC_LOG.debug("exacts:{}", excludes);
		}
		if (start.size() > 0) {
			excludeStarts = new ArrayList<>(start);
			LogType.IOC_LOG.debug("excludeStarts:{}", excludeStarts);
		}
		if (end.size() > 0) {
			excludeEnds = new ArrayList<>(end);
			LogType.IOC_LOG.debug("excludeEnds:{}", excludeEnds);
		}
	}

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			String clzName = clz.getName();

			if (this.excludes != null && this.excludes.contains(clzName)) {
				LogType.IOC_LOG.info("{} excluded by user", clzName);
				return;
			}

			if (this.excludeStarts != null) {
				for (String start : this.excludeStarts) {
					if (clzName.startsWith(start)) {
						LogType.IOC_LOG.info("{} excluded for pattern {}*", clzName, start);
						return;
					}
				}
			}

			if (this.excludeEnds != null) {
				for (String end : this.excludeEnds) {
					if (clzName.endsWith(end)) {
						LogType.IOC_LOG.info("{} excluded for pattern *{}", clzName, end);
						return;
					}
				}
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
				Object bean = InnerIOC.putClass(Cachable.PRE + BeanPool.getBeanName(clz), clz);
				if (!Cachable.class.isInstance(bean)) {
					SumkException.throwException(35423543, clz.getName() + " is not instance of Cachable");
				}
				if (this.useRedis) {
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
