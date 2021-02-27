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

import java.util.List;

public final class IOC {

	/**
	 * 获取对应的bean
	 * 
	 * @param name
	 *            bean的名称
	 * @return 如果不存在，就返回null。如果bean不止一个，会抛出TooManyBeanException异常
	 */
	public static <T> T get(String name) {
		return get(name, null);
	}

	public static <T> T get(Class<T> clz) {
		return get(null, clz);
	}

	public static <T> T get(String name, Class<T> clz) {
		return InnerIOC.pool.getBean(name, clz);
	}

	public static <T> List<T> getBeans(Class<T> clz) {
		return InnerIOC.pool.getBeans(null, clz);
	}

	public static <T> List<T> getBeans(String name, Class<T> clz) {
		return InnerIOC.pool.getBeans(name, clz);
	}

	public static String info() {
		return InnerIOC.pool.toString();
	}

	public static List<String> beanNames() {
		return InnerIOC.beanNames();
	}

}
