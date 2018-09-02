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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.yx.common.StartConstants;
import org.yx.conf.AppInfo;
import org.yx.db.Cachable;
import org.yx.db.Cached;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.StringUtil;

public class BeanFactory extends AbstractBeanListener {

	private boolean cachedScan;
	private boolean useRedis;
	private Set<String> beanExcludes;

	public BeanFactory() {
		super(AppInfo.get(StartConstants.IOC_PACKAGES));
		this.cachedScan = AppInfo.getBoolean("sumk.ioc.cached.enable", true);
		this.useRedis = AppInfo.getBoolean("sumk.dao.cache", true);
		String noProxys = AppInfo.get("sumk.ioc.exclude", null);
		if (noProxys == null) {
			this.beanExcludes = Collections.emptySet();
			return;
		}
		beanExcludes = new HashSet<>();
		String[] noProxyArray = StringUtil.splitByComma(noProxys);
		for (String s : noProxyArray) {
			if ((s = s.trim()).isEmpty()) {
				continue;
			}
			beanExcludes.add(s.trim());
		}
	}

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			if (this.beanExcludes.contains(clz.getName())) {
				Log.get("sumk.ioc").info("{} excluded by user", clz.getName());
				return;
			}
			Bean b = clz.getAnnotation(Bean.class);
			if (b != null) {
				InnerIOC.putClass(b.value(), clz);
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

}
