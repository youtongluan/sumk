package org.yx.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.yx.asm.ProxyClassFactory;
import org.yx.exception.TooManyBeanException;
import org.yx.log.Log;

public class BeanPool {
	private Map<String, Object> map = new ConcurrentHashMap<>();

	public static String getBeanName(Class<?> clz) {
		String name = StringUtils.uncapitalize(clz.getSimpleName());
		if (name.endsWith("Impl")) {
			name = name.substring(0, name.length() - 4);
		}
		return name;
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
	 * 获取pool中所有的bean
	 * 
	 * @return
	 */
	Collection<Object> allBeans() {
		List<Object> list = new ArrayList<>();
		Collection<Object> vs = map.values();
		for (Object v : vs) {
			if (!v.getClass().isArray()) {
				list.add(getBean(v));
				continue;
			}
			Object[] objs = (Object[]) v;
			for (Object o : objs) {
				list.add(getBean(o));
			}
		}
		return list;
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
	public <T> T putClass(String name, Class<T> clz) throws Exception {
		Assert.notNull(clz);
		if (name == null || name.isEmpty()) {
			name = BeanPool.getBeanName(clz);
		}
		name = name.trim();
		Class<?> proxyClz = ProxyClassFactory.proxyIfNeed(clz);
		Object bean = proxyClz.newInstance();
		BeanWrapper w = new BeanWrapper();
		w.setBean(bean);
		w.setTargetClass(clz);
		put(name, w);
		return this.getBean(name, clz);
	}

	/**
	 * 添加bean，一个类的实例，不能多次出现在一个name中
	 * 
	 * @param bean
	 * @param name
	 */
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
	 * @return
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
