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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.asm.ProxyClassFactory;
import org.yx.common.Ordered;
import org.yx.exception.TooManyBeanException;
import org.yx.log.Log;
import org.yx.util.StringUtil;

public final class BeanPool {

	private final Map<String, Object> map = new ConcurrentHashMap<>(128, 0.5f);

	public static String resloveBeanName(Class<?> clz) {
		String name = StringUtil.uncapitalize(clz.getSimpleName());
		if (name.endsWith("Impl")) {
			name = name.substring(0, name.length() - 4);
		}
		return name;
	}

	static Set<String> resloveBeanNames(Class<?> clazz) {
		Objects.requireNonNull(clazz, "Class must not be null");
		Set<Class<?>> interfaces = new HashSet<>();
		resloveSuperClassAndInterface(clazz, interfaces);
		Set<String> names = new HashSet<>();
		interfaces.forEach(clz -> names.add(resloveBeanName(clz)));
		return names;
	}

	private static void resloveSuperClassAndInterface(Class<?> clazz, Set<Class<?>> interfaces) {
		while (clazz != null && !clazz.getName().startsWith(Loader.JAVA_PRE)
				&& (clazz.getModifiers() & Modifier.PUBLIC) != 0) {
			interfaces.add(clazz);
			Class<?>[] ifcs = clazz.getInterfaces();
			if (ifcs != null) {
				for (Class<?> ifc : ifcs) {
					resloveSuperClassAndInterface(ifc, interfaces);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	private final Object getBean(Object v) {
		BeanWrapper w = (BeanWrapper) v;
		return w.getBean();
	}

	private final Class<?> getBeanClass(Object v) {
		BeanWrapper w = (BeanWrapper) v;
		return w.getTargetClass();
	}

	String[] beanNames() {
		Set<String> names = map.keySet();
		return names.toArray(new String[0]);
	}

	Collection<BeanWrapper> allBeanWrapper() {
		Map<BeanWrapper, Boolean> beans = new IdentityHashMap<>();
		Collection<Object> vs = map.values();
		for (Object v : vs) {
			if (!v.getClass().isArray()) {
				beans.putIfAbsent((BeanWrapper) v, Boolean.TRUE);
				continue;
			}
			BeanWrapper[] objs = (BeanWrapper[]) v;
			for (BeanWrapper o : objs) {
				beans.putIfAbsent(o, Boolean.TRUE);
			}
		}
		return beans.keySet();
	}

	Collection<Object> beans() {
		Map<Object, Boolean> beans = new IdentityHashMap<>();
		Collection<Object> vs = map.values();
		for (Object v : vs) {
			if (!v.getClass().isArray()) {
				Object bean = getBean(v);
				beans.putIfAbsent(bean, Boolean.TRUE);
				continue;
			}
			BeanWrapper[] objs = (BeanWrapper[]) v;
			for (BeanWrapper o : objs) {
				Object bean = o.getBean();
				beans.putIfAbsent(bean, Boolean.TRUE);
			}
		}
		return beans.keySet();
	}

	public <T> T putClass(String beanName, Class<T> clz) throws Exception {
		Objects.requireNonNull(clz);
		Set<String> names = (beanName == null || (beanName = beanName.trim()).isEmpty()) ? resloveBeanNames(clz)
				: Collections.singleton(beanName);
		if (names == null || names.isEmpty()) {
			names = Collections.singleton(resloveBeanName(clz));
		}
		Class<?> proxyClz = ProxyClassFactory.proxyIfNeed(clz);
		Object bean = Loader.newInstance(proxyClz);
		BeanWrapper w = new BeanWrapper(bean);
		for (String name : names) {
			put(name, w);
		}
		return this.getBean(beanName, clz);
	}

	public <T> T putBean(String beanName, T bean) {
		Objects.requireNonNull(bean);
		Class<?> clz = bean.getClass();
		Set<String> names = (beanName == null || (beanName = beanName.trim()).isEmpty()) ? resloveBeanNames(clz)
				: Collections.singleton(beanName);
		if (names == null || names.isEmpty()) {
			names = Collections.singleton(resloveBeanName(clz));
		}
		BeanWrapper w = new BeanWrapper(bean);
		for (String name : names) {
			put(name, w);
		}
		return bean;
	}

	private void put(String name, BeanWrapper w) {
		Class<?> clz = w.getTargetClass();
		Object oldWrapper = map.putIfAbsent(name, w);
		if (oldWrapper == null) {
			return;
		}
		synchronized (this) {
			if (!oldWrapper.getClass().isArray()) {
				if (clz == this.getBeanClass(oldWrapper)) {
					Log.get("sumk.bean").debug("{}={} duplicate,will be ignored", name, clz.getName());
					return;
				}
				map.put(name, new BeanWrapper[] { (BeanWrapper) oldWrapper, w });
				return;
			}
			BeanWrapper[] objs = (BeanWrapper[]) oldWrapper;
			for (BeanWrapper o : objs) {
				if (clz == this.getBeanClass(o)) {
					Log.get("sumk.bean").debug("{}={} duplicate,will be ignored.", name, clz.getName());
					return;
				}
			}
			BeanWrapper[] beans = new BeanWrapper[objs.length + 1];
			System.arraycopy(objs, 0, beans, 0, objs.length);
			beans[beans.length - 1] = w;
			map.put(name, beans);
		}

	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> clz) {
		if (name == null || name.length() == 0) {
			name = resloveBeanName(clz);
		}
		if (clz == Object.class) {
			clz = null;
		}
		Object bw = map.get(name);
		if (bw == null) {
			return null;
		}
		if (!bw.getClass().isArray()) {
			Object obj = this.getBean(bw);
			if (clz == null || clz.isInstance(obj)) {
				return (T) obj;
			}
			throw new ClassCastException(name + " type error。real is " + obj.getClass().getName()
					+ ",cannot  compatible with " + clz.getName());
		}
		if (clz == null) {
			throw new TooManyBeanException(name + " exist multi instance");
		}
		BeanWrapper[] objs = (BeanWrapper[]) bw;

		for (BeanWrapper o : objs) {
			if (clz == o.getBean().getClass()) {
				return (T) o.getBean();
			}
		}

		List<Object> beans = new ArrayList<>(2);
		for (BeanWrapper w : objs) {
			Class<?> targetClz = w.getTargetClass();
			if (targetClz == clz) {
				return (T) w.getBean();
			}
			if (clz.isAssignableFrom(targetClz)) {
				beans.add(w.getBean());
			}
		}
		if (beans.isEmpty()) {
			return null;
		}
		if (beans.size() > 1) {
			throw new TooManyBeanException(name + "存在多个" + clz.getName() + "实例");
		}
		return (T) beans.get(0);

	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getBeans(String name, Class<T> clz) {
		List<T> list = new ArrayList<>(4);
		if (name == null || name.length() == 0) {
			name = resloveBeanName(clz);
		}
		Object bw = map.get(name);
		if (bw == null) {
			return list;
		}
		if (!bw.getClass().isArray()) {
			Object obj = this.getBean(bw);
			if (clz == null || clz.isInstance(obj)) {
				list.add((T) obj);
			}
			return list;
		}
		BeanWrapper[] objs = (BeanWrapper[]) bw;

		for (BeanWrapper w : objs) {
			Object o = w.getBean();
			if (clz.isInstance(o)) {
				list.add((T) o);
			}
		}
		if (Ordered.class.isAssignableFrom(clz)) {
			list.sort(null);
		}
		return list;

	}

	@Override
	public String toString() {
		Iterator<Entry<String, Object>> i = map.entrySet().iterator();
		if (!i.hasNext())
			return "empty bean";

		StringBuilder sb = new StringBuilder();
		for (;;) {
			Entry<String, Object> e = i.next();
			String key = e.getKey();
			Object value = e.getValue();
			sb.append(key);
			sb.append(':');
			if (value.getClass().isArray()) {
				sb.append("[");
				Object[] objs = (Object[]) value;

				for (int k = 0; k < objs.length; k++) {
					if (k > 0) {
						sb.append(",");
					}
					Object o = objs[k];
					sb.append(getBeanClass(o).getName());
				}
				sb.append("]");
			} else {
				sb.append(getBeanClass(value).getName());
			}

			if (!i.hasNext())
				return sb.toString();
			sb.append(',').append(' ');
		}
	}

	Object getBeanWrapper(String name) {
		return map.get(name);
	}

	void clear() {
		map.clear();
	}
}
