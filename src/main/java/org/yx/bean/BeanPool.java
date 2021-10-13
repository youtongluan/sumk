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
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.yx.asm.ProxyClassFactory;
import org.yx.conf.Const;
import org.yx.util.StringUtil;

public final class BeanPool {

	private final ConcurrentMap<String, NameSlot> slotMap = new ConcurrentHashMap<>(128);

	public List<String> beanNames() {
		Set<String> names = slotMap.keySet();
		return new ArrayList<>(names);
	}

	public Collection<Object> beans() {
		Map<Object, Boolean> beans = new IdentityHashMap<>(slotMap.size());
		for (NameSlot v : slotMap.values()) {
			for (Object bean : v.beans()) {
				beans.putIfAbsent(bean, Boolean.TRUE);
			}
		}
		return beans.keySet();
	}

	public <T> T putClass(String beanName, Class<T> clz) throws Exception {
		Objects.requireNonNull(clz);
		Collection<String> names = (beanName == null || (beanName = beanName.trim()).isEmpty())
				? BeanKit.resloveBeanNames(clz)
				: StringUtil.splitAndTrim(beanName, Const.COMMA, Const.SEMICOLON);
		if (names == null || names.isEmpty()) {
			names = Collections.singleton(BeanKit.resloveBeanName(clz));
		}
		Class<?> proxyClz = ProxyClassFactory.proxyIfNeed(clz);
		Object bean = Loader.newInstance(proxyClz);
		for (String name : names) {
			put(name, bean);
		}
		return this.getBean(beanName, clz);
	}

	public <T> T putBean(String beanName, T bean) {
		Class<?> clz = Objects.requireNonNull(bean).getClass();
		Collection<String> names = (beanName == null || (beanName = beanName.trim()).isEmpty())
				? BeanKit.resloveBeanNames(clz)
				: StringUtil.splitAndTrim(beanName, Const.COMMA, Const.SEMICOLON);
		if (names == null || names.isEmpty()) {
			names = Collections.singleton(BeanKit.resloveBeanName(clz));
		}
		for (String name : names) {
			put(name, bean);
		}
		return bean;
	}

	private synchronized boolean put(String name, Object bean) {
		NameSlot oldSlot = slotMap.putIfAbsent(name, new NameSlot(name, new Object[] { bean }));
		if (oldSlot == null) {
			return true;
		}
		return oldSlot.appendBean(bean);
	}

	public <T> T getBean(String name, Class<T> clz) {
		if (name == null || name.length() == 0) {
			name = BeanKit.resloveBeanName(clz);
		}
		NameSlot bw = slotMap.get(name);
		return bw == null ? null : bw.getBean(clz);
	}

	public <T> List<T> getBeans(String name, Class<T> clz) {
		if (name == null || name.length() == 0) {
			name = BeanKit.resloveBeanName(clz);
		}
		NameSlot slot = slotMap.get(name);
		return slot == null ? Collections.emptyList() : slot.getBeans(clz);

	}

	public NameSlot getSlot(String name) {
		return slotMap.get(name);
	}

	public void clear() {
		slotMap.clear();
	}

	@Override
	public String toString() {
		return slotMap.toString();
	}

}
