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

import org.yx.common.Ordered;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public final class IOC {

	/**
	 * 获取对应的bean
	 * 
	 * @param      <T> 返回值类型
	 * @param name bean的名称
	 * @return 如果不存在，就返回null。如果bean不止一个，会抛出SumkException异常
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

	/**
	 * 获取该类型bean的第一个，如果不存在就抛出异常。
	 * 
	 * @param            <T> bean的类型
	 * @param clz        bean的类型，返回order最小的那个
	 * @param allowEmpty true表示允许为空，否则会抛出异常
	 * @return 返回第一个符合条件的bean。被自定义名称的bean可能获取不到
	 */
	public static <T extends Ordered> T getFirstBean(Class<T> clz, boolean allowEmpty) {
		List<T> factorys = IOC.getBeans(clz);
		if (factorys.isEmpty()) {
			if (allowEmpty) {
				return null;
			}
			throw new SumkException(-353451, clz.getName() + "没有实现类，或者实现类不是Bean");
		}
		T f = factorys.get(0);
		Logs.ioc().debug("{}第一个bean是{}", clz.getName(), f);
		return f;
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
