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

import org.yx.annotation.spec.BeanSpec;
import org.yx.annotation.spec.Specs;

public class BeanFactory extends AbstractBootWatcher {

	@Override
	public void accept(Class<?> clz) throws Exception {
		BeanSpec b = Specs.extractBean(clz);
		if (b == null) {
			return;
		}
		if (FactoryBean.class.isAssignableFrom(clz)) {
			FactoryBean factory = (FactoryBean) Loader.newInstance(clz);
			registerFactoryBean(factory.beans());
		} else {
			InnerIOC.putClass(b.value(), clz);
		}
	}

	public static void registerFactoryBean(Collection<?> beans) throws Exception {
		if (beans != null && beans.size() > 0) {
			for (Object obj : beans) {
				registerBean(null, obj);
			}
		}
	}

	public static void registerBean(String name, Object obj) throws Exception {
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
			registerBean(named.getBeanName(), named.getBean());
			return;
		}

		if (clz == InterfaceBean.class) {
			InterfaceBean complex = (InterfaceBean) obj;
			registerBean(BeanKit.resloveBeanName(complex.getIntf()), complex.getBean());
			return;
		}

		InnerIOC.putBean(name, obj);
	}

	@Override
	public int order() {
		return 1000;
	}

}
