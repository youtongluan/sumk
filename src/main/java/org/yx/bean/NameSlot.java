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
import java.util.List;

import org.yx.exception.SumkException;
import org.yx.exception.SumkExceptionCode;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;

public class NameSlot {

	private final String name;
	private Object[] beans;

	public NameSlot(String name, Object[] beans) {
		this.name = name;
		this.beans = beans == null ? new Object[0] : beans;
	}

	public List<Object> beans() {
		return CollectionUtil.unmodifyList(beans);
	}

	public synchronized boolean appendBean(Object bean) {
		Object[] beans = this.beans;
		if (beans.length == 0) {
			this.beans = new Object[] { bean };
			return true;
		}
		Class<?> clz = BeanKit.getTargetClass(bean);
		for (Object o : beans) {
			if (clz == BeanKit.getTargetClass(o)) {
				Logs.ioc().warn("{}={} duplicate,will be ignored.", name, clz.getName());
				return false;
			}
		}
		Object[] newBeans = new Object[beans.length + 1];
		System.arraycopy(beans, 0, newBeans, 0, beans.length);
		newBeans[newBeans.length - 1] = bean;
		this.beans = newBeans;
		return true;
	}

	private Class<?> resolveType(Class<?> clz) {
		if (clz == null) {
			return Object.class;
		}
		if (Boxed.class.isAssignableFrom(clz)) {
			return clz.getSuperclass();
		}
		return clz;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> clz) {
		clz = (Class<T>) this.resolveType(clz);

		List<Object> ret = new ArrayList<>(2);
		for (Object b : this.beans) {
			Class<?> targetClz = BeanKit.getTargetClass(b);
			if (targetClz == clz) {
				return (T) b;
			}
			if (clz.isAssignableFrom(targetClz)) {
				ret.add(b);
			}
		}
		if (ret.isEmpty()) {
			return null;
		}
		if (ret.size() > 1) {
			throw new SumkException(SumkExceptionCode.TOO_MANY_BEAN, name + "存在多个" + clz.getName() + "实例");
		}
		return (T) ret.get(0);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getBeans(Class<T> clz) {
		clz = (Class<T>) this.resolveType(clz);
		List<T> list = new ArrayList<>(this.beans.length);

		for (Object o : this.beans) {
			if (clz.isInstance(o)) {
				list.add((T) o);
			}
		}
		if (list.size() > 1 && Comparable.class.isAssignableFrom(clz)) {
			list.sort(null);
		}
		return list;
	}

	public String name() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(name).append(" :");
		for (Object o : this.beans) {
			sb.append("  ").append(BeanKit.getTargetClass(o).getName());
		}
		return sb.toString();
	}

}
