package org.yx.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.yx.bean.watcher.BeanWatcher;
import org.yx.bean.watcher.ContextWatcher;
import org.yx.bean.watcher.IOCWatcher;
import org.yx.bean.watcher.Scaned;
import org.yx.db.Cached;
import org.yx.exception.SumkException;
import org.yx.listener.Listener;
import org.yx.listener.ListenerGroup;
import org.yx.listener.ListenerGroupImpl;
import org.yx.log.Log;
import org.yx.util.ClassScaner;

public final class BeanPublisher {

	private static ListenerGroup<BeanEvent> group = new ListenerGroupImpl<>();

	/**
	 * 发布扫描包的事件，包括添加到IOC，SOA接口或HTTP接口中
	 * 
	 * @param packageNames
	 */
	public static synchronized void publishBeans(String... packageNames) {
		ClassScaner scaner = new ClassScaner();
		Collection<String> clzs = scaner.parse(packageNames);
		for (String c : clzs) {
			try {

				Class<?> clz = BeanPublisher.class.getClassLoader().loadClass(c);
				publish(new BeanEvent(clz));
			} catch (Exception e) {
				Log.printStack(e);
			} catch (NoClassDefFoundError e) {
				if (!c.startsWith("org.yx.")) {
					throw e;
				}
				Log.get("SYS.load").info(c + " load failed, ommit it.Error message:" + e.getMessage());
			}
		}
		Log.get(BeanPublisher.class).debug(IOC.info());
		autoWiredAll();
	}

	private static Object getInjectObject(Field f, Class<?> clz) {
		String name = f.getName();
		if (clz == Object.class) {
			clz = f.getType();
		}
		Object target = IOC.get(name, clz);
		if (target != null) {
			return target;
		}

		target = IOC.get(name, f.getType());
		if (target != null) {
			return target;
		}
		return IOC.get(clz);
	}

	private static Object getCacheObject(Field f) {
		String name = f.getName();
		Class<?> clz = f.getType();

		Object target = IOC.cache(name, f.getType());
		if (target != null) {
			return target;
		}
		return IOC.cache(null, clz);
	}

	private static void injectField(Field f, Object bean, Object target) {
		boolean access = f.isAccessible();
		if (!access) {
			f.setAccessible(true);
		}
		try {
			f.set(bean, target);
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private static void autoWiredAll() {
		BeanPool pool = InnerIOC.pool;
		Map<Object, BeanWrapper> beanMap = pool.allBeans();
		Collection<BeanWrapper> beanwrapers = beanMap.values();
		Collection<Object> beans = beanMap.keySet();
		List<IOCWatcher> watchers = new ArrayList<>(128);
		beans.forEach(b -> {
			if (IOCWatcher.class.isInstance(b)) {
				watchers.add((IOCWatcher) b);
			}
		});
		;
		watchers.sort(null);

		watchers.stream().filter(Scaned.class::isInstance).forEach(w -> ((Scaned) w).afterScaned());
		injectProperties(beans);
		watchers.stream().filter(BeanWatcher.class::isInstance).forEach(w -> {
			for (BeanWrapper bw : beanwrapers) {
				BeanWatcher watcher = (BeanWatcher) w;

				if (BeanWatcher.class.isAssignableFrom(bw.getTargetClass())
						|| !watcher.acceptClass().isInstance(bw.getBean())) {
					continue;
				}
				watcher.beanPost(bw);
			}
		});
		watchers.stream().filter(ContextWatcher.class::isInstance)
				.forEach(w -> ((ContextWatcher) w).contextInitialized());
	}

	private static void injectProperties(Collection<Object> beans) {

		beans.forEach(bean -> {
			Class<?> tempClz = bean.getClass();
			while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

				Field[] fs = tempClz.getDeclaredFields();
				for (Field f : fs) {
					Inject inj = f.getAnnotation(Inject.class);
					if (inj != null) {
						Class<?> clz = inj.beanClz();
						Object target = getInjectObject(f, clz);
						if (target == null) {
							SumkException.throwException(235435658,
									bean.getClass().getName() + "--" + f.getName() + " cannot injected");
						}
						injectField(f, bean, target);
						continue;
					}
					Cached c = f.getAnnotation(Cached.class);
					if (c != null) {
						Object target = getCacheObject(f);
						if (target == null) {
							SumkException.throwException(235435658,
									bean.getClass().getName() + "--" + f.getName() + " cannot injected");
						}
						injectField(f, bean, target);
						continue;
					}
				}
				tempClz = tempClz.getSuperclass();
			}
		});
	}

	/**
	 * 发布事件
	 * 
	 * @param event
	 */
	public static void publish(BeanEvent event) {
		group.listen(event);
	}

	/**
	 * 添加监听器
	 * 
	 * @param listener
	 * @return
	 */
	public static synchronized boolean addListener(Listener<BeanEvent> listener) {
		group.addListener(listener);
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static synchronized boolean addListeners(List<Listener> list) {
		for (Listener<BeanEvent> l : list) {
			addListener(l);
		}
		return true;
	}

	/**
	 * 移除监听器.如果监听器不存在，就返回null
	 * 
	 * @return
	 */
	public static synchronized void removeListener(Listener<BeanEvent> listener) {
		group.removeListener(listener);
	}

}