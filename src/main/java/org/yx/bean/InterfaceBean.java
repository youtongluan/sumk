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

import java.util.Objects;

import org.yx.exception.SimpleSumkException;

public final class InterfaceBean implements ComplexBean {
	private final Class<?> intf;
	/**
	 * 可以是class类，也可以包含实力化后的对象。但不能是NamedBean
	 */
	private final Object bean;

	/**
	 * @param beanName
	 *            bean的名称，不能为null或空串
	 * @param bean
	 *            可以是class类，也可以包含实力化后的对象。但不能是NamedBean
	 */
	public InterfaceBean(Class<?> intf, Object bean) {
		this.intf = Objects.requireNonNull(intf);
		if (ComplexBean.class.isInstance(bean)) {
			throw new SimpleSumkException(233654645, "bean can not be a ComplexBean object");
		}
		this.bean = Objects.requireNonNull(bean);
	}

	public Class<?> getIntf() {
		return intf;
	}

	public Object getBean() {
		return bean;
	}

}
