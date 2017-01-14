/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.util.List;

import org.yx.db.Cachable;
import org.yx.exception.TooManyBeanException;

public final class IOC {

	public static <T> T get(String name) {
		return get(name, null);
	}

	public static <T> T get(Class<T> clz) {
		return get(null, clz);
	}

	/**
	 * 获取对应的bean
	 * 
	 * @param name
	 * @param clz
	 * @return bean不存在，就返回null。如果bean不止一个，会抛出异常
	 * @exception TooManyBeanException
	 */
	public static <T> T get(String name, Class<T> clz) {
		return InnerIOC.pool.getBean(name, clz);
	}

	/**
	 * 根据接口或类，查询它相关的bean。如果接口不存在，就返回空list
	 * 
	 * @param clz
	 *            一般是接口
	 * @return
	 */
	public static <T> List<T> getBeans(Class<T> clz) {
		return InnerIOC.pool.getBeans(null, clz);
	}

	public static String info() {
		return InnerIOC.pool.toString();
	}

	public static <T> T cache(String name, Class<T> clz) {
		if (name == null || name.isEmpty()) {
			name = BeanPool.getBeanName(clz);
		}
		name = Cachable.PRE + name;
		return InnerIOC.pool.getBean(name, clz);
	}

}
