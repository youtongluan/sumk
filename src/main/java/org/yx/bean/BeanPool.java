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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.asm.ProxyClassFactory;
import org.yx.common.Ordered;
import org.yx.exception.TooManyBeanException;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.StringUtils;

public class BeanPool {

	private final Map<String, Object> map = new ConcurrentHashMap<>(128, 0.5f);

	public static String getBeanName(Class<?> clz) {
		String name = StringUtils.uncapitalize(clz.getSimpleName());
		if (name.endsWith("Impl")) {
			name = name.substring(0, name.length() - 4);
		}
		return name;
	}

	/**
	 * 获取所有用户定义的类以及方法，用户定义指定是非java.开头的类或接口。<BR>
	 * 用于IOC使用
	 * 
	 * @param clazz
	 * @return
	 */
	static Set<String> getBeanNames(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		Set<Class<?>> interfaces = new HashSet<>();
		resloveSuperClassAndInterface(clazz, interfaces);
		Set<String> names = new HashSet<>();
		interfaces.forEach(clz -> names.add(getBeanName(clz)));
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

	/**
	 * 获取pool中所有的bean.key是bean
	 * 
	 * @return
	 */
	Map<Object, BeanWrapper> allBeans() {
		Map<Object, BeanWrapper> beans = new HashMap<>();
		Collection<Object> vs = map.values();
		for (Object v : vs) {
			if (!v.getClass().isArray()) {
				Object bean = getBean(v);
				if (beans.containsKey(bean)) {
					continue;
				}
				beans.put(bean, (BeanWrapper) v);
				continue;
			}
			Object[] objs = (Object[]) v;
			for (Object o : objs) {
				Object bean = getBean(o);
				if (beans.containsKey(bean)) {
					continue;
				}
				beans.put(bean, (BeanWrapper) o);
			}
		}
		return beans;
	}

	/**
	 * 将类的实例添加的IOC中。这里会做aop等操作
	 * 
	 * @param name
	 *            null的话，将根据clz的名字自动生成
	 * @param clz
	 * @return 添加到IOC中的实例
	 * @throws Exception
	 */
	public <T> T putClass(String beanName, Class<T> clz) throws Exception {
		Assert.notNull(clz);
		Set<String> names = (beanName == null || (beanName = beanName.trim()).isEmpty()) ? getBeanNames(clz)
				: Collections.singleton(beanName);
		if (names == null || names.isEmpty()) {
			names = Collections.singleton(getBeanName(clz));
		}
		Class<?> proxyClz = ProxyClassFactory.proxyIfNeed(clz);
		Object bean = proxyClz.newInstance();
		BeanWrapper w = new BeanWrapper(bean, clz);
		for (String name : names) {
			put(name, w);
		}
		return this.getBean(beanName, clz);
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
					Log.get(this.getClass(), "put").info("{}={} duplicate,will be ignored", name, clz.getName());
					return;
				}
				map.put(name, new BeanWrapper[] { (BeanWrapper) oldWrapper, w });
				return;
			}
			BeanWrapper[] objs = (BeanWrapper[]) oldWrapper;
			for (BeanWrapper o : objs) {
				if (clz == this.getBeanClass(o)) {
					Log.get(this.getClass(), "put").info("{}={} duplicate,will be ignored.", name, clz.getName());
					return;
				}
			}
			BeanWrapper[] beans = new BeanWrapper[objs.length + 1];
			System.arraycopy(objs, 0, beans, 0, objs.length);
			beans[beans.length - 1] = w;
			map.put(name, beans);
		}

	}

	/**
	 * name和clz都有可能为null，但不能同时为null
	 * 
	 * @param name
	 * @param clz
	 * @return 返回最符合条件的bean。不存在最符合条件的bean，就抛出异常
	 * @exception TooManyBeanException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> clz) {
		if (name == null || name.length() == 0) {
			name = getBeanName(clz);
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

		Object bean = null;
		for (BeanWrapper w : objs) {
			Object o = w.getBean();
			if (clz.isInstance(o)) {
				if (bean != null) {
					Log.get(this.getClass(), "getBean")
							.error(name + "存在多个实例:" + o.getClass().getName() + "," + bean.getClass().getName());
					throw new TooManyBeanException(name + "存在多个" + clz.getName() + "实例");
				}
				bean = o;
			}
		}
		return (T) bean;

	}

	/**
	 * 如果不存在，返回空的list，如果实现了Ordered接口，将会自动排序
	 * 
	 * @param name
	 * @param clz
	 * @return 返回所有符合条件的bean，如果不存在就返回null，不会抛异常
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getBeans(String name, Class<T> clz) {
		List<T> list = new ArrayList<>(4);
		if (name == null || name.length() == 0) {
			name = getBeanName(clz);
		}
		if (clz == Object.class) {
			clz = null;
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

}
