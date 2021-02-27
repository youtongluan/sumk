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
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.main.StartConstants;

public class BeanFactory extends AbstractBeanListener {

	public BeanFactory() {
		super(AppInfo.get(StartConstants.IOC_PACKAGES));
		this.valid = true;
	}

	@Override
	public void onListen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
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
			throw new SumkException(-345365, "IOC error on " + event.getSource(), e);
		}

	}

	private void putFactoryBean(Collection<?> beans) throws Exception {
		if (beans != null && beans.size() > 0) {
			for (Object obj : beans) {
				putBean(null, obj);
			}
		}
	}

	private void putBean(String name, Object obj) throws Exception {
		if (obj == null) {
			return;
		}
		Class<?> clz = obj.getClass();
		if (clz == Class.class) {
			InnerIOC.putClass(name, (Class<?>) obj);
			return;
		}

		if (clz == NamedBean.class) {
			NamedBean named = (NamedBean) obj;
			this.putBean(named.getBeanName(), named.getBean());
			return;
		}

		if (clz == InterfaceBean.class) {
			InterfaceBean complex = (InterfaceBean) obj;
			this.putBean(BeanKit.resloveBeanName(complex.getIntf()), complex.getBean());
			return;
		}

		InnerIOC.putBean(name, obj);
	}

}
