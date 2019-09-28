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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.annotation.Inject;
import org.yx.annotation.db.Cached;
import org.yx.asm.AsmUtils;
import org.yx.bean.watcher.BeanCreate;
import org.yx.bean.watcher.BeanWatcher;
import org.yx.bean.watcher.IntfImplement;
import org.yx.bean.watcher.PluginHandler;
import org.yx.common.StartConstants;
import org.yx.common.scaner.ClassScaner;
import org.yx.conf.AppInfo;
import org.yx.exception.SimpleSumkException;
import org.yx.listener.Listener;
import org.yx.listener.ListenerGroup;
import org.yx.listener.ListenerGroupImpl;
import org.yx.log.Log;

public final class BeanPublisher {

	private static ListenerGroup<BeanEvent> group = new ListenerGroupImpl<>();

	public static synchronized void publishBeans(List<String> packageNames) {
		if (packageNames.isEmpty()) {
			Log.get("sumk.SYS").warn("property [sumk.ioc] is empty");
		}

		packageNames.remove(StartConstants.INNER_PACKAGE);
		packageNames.add(0, StartConstants.INNER_PACKAGE);

		IntfImplement.beforeScan();
		Collection<String> clzs = ClassScaner.listClasses(packageNames.toArray(new String[packageNames.size()]));
		for (String c : clzs) {
			try {

				Class<?> clz = Loader.loadClassExactly(c);
				if (AsmUtils.notPublicOnly(clz.getModifiers()) || clz.isInterface() || clz.isAnonymousClass()
						|| clz.isLocalClass() || clz.isAnnotation() || clz.isEnum()) {
					continue;
				}
				publish(new BeanEvent(clz));
			} catch (Exception e) {
				Log.printStack("sumk.error", e);
			} catch (NoClassDefFoundError e) {
				if (!c.startsWith("org.yx.")) {
					throw e;
				}
				Log.get("sumk.SYS").debug("{} ignored.{}", c, e.getMessage());
			}
		}
		if (AppInfo.getBoolean("sumk.ioc.showinfo", false)) {
			Log.get("sumk.SYS").debug(IOC.info());
		}
		autoWiredAll();
	}

	private static Object getBean(Field f, Class<?> clz) {
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
			Log.printStack("sumk.error", e);
		}
	}

	private static void autoWiredAll() {
		final Object[] beans = InnerIOC.beans().toArray(new Object[0]);
		Log.get("sumk.ioc").trace("after beans create...");
		IOC.getBeans(BeanCreate.class).forEach(w -> w.afterCreate(beans));
		Log.get("sumk.ioc").trace("inject beans properties...");
		for (Object bean : beans) {
			injectProperties(bean);
		}
		Log.get("sumk.ioc").trace("after beans installed...");
		IOC.getBeans(BeanWatcher.class).forEach(watcher -> {
			watcher.afterInstalled(beans);
		});
		Log.get("sumk.ioc").trace("plugins starting...");
		PluginHandler.instance.start();
	}

	private static void injectProperties(Object bean) {
		Class<?> tempClz = bean.getClass();
		while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				Inject inject = f.getAnnotation(Inject.class);
				if (inject != null) {
					Class<?> clz = inject.beanClz();
					Object target = null;
					if (inject.handler().length() > 0) {
						try {
							target = InjectParser.get(f, inject, bean);
						} catch (Exception e) {
							Log.printStack("sumk.error", e);
							throw new SimpleSumkException(-235435628, bean.getClass().getName() + "." + f.getName()
									+ " cannot injected with " + inject.handler());
						}
					} else if (f.getType().isArray()) {
						target = getArrayField(f, clz, bean);
					} else if (List.class == f.getType()) {
						target = getListField(f, clz, bean);
					} else if (Set.class == f.getType()) {
						target = getSetField(f, clz, bean);
					} else {
						target = getBean(f, clz);
					}
					if (target == null) {
						throw new SimpleSumkException(-235435658,
								bean.getClass().getName() + "." + f.getName() + " cannot injected.");
					}
					injectField(f, bean, target);
					continue;
				}

				Cached c = f.getAnnotation(Cached.class);
				if (c != null) {
					Object target = getCacheObject(f);
					if (target == null) {
						throw new SimpleSumkException(23526568,
								bean.getClass().getName() + "." + f.getName() + " cannot injected");
					}
					injectField(f, bean, target);
					continue;
				}
			}
			tempClz = tempClz.getSuperclass();
		}
	}

	private static Set<?> getSetField(Field f, Class<?> clz, Object bean) {
		if (clz == Object.class) {
			throw new SimpleSumkException(-23984568, "" + bean.getClass().getName() + "." + f.getName()
					+ ": beanClz of @Inject in list type cannot be null");
		}
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			return null;
		}
		return new HashSet<>(target);
	}

	private static List<?> getListField(Field f, Class<?> clz, Object bean) {
		if (clz == Object.class) {
			throw new SimpleSumkException(-23984568, "" + bean.getClass().getName() + "." + f.getName()
					+ ": beanClz of @Inject in list type cannot be null");
		}
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			return null;
		}
		return target;
	}

	private static Object getArrayField(Field f, Class<?> clz, Object bean) {
		Class<?> filedType = f.getType().getComponentType();
		if (clz == Object.class) {
			clz = filedType;
		} else if (!filedType.isAssignableFrom(clz)) {
			throw new SimpleSumkException(-239864568, bean.getClass().getName() + "." + f.getName() + " is "
					+ filedType.getSimpleName() + "[], is not " + clz.getSimpleName() + "[]");
		}
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			return null;
		}
		return target.toArray((Object[]) Array.newInstance(filedType, target.size()));
	}

	public static void publish(BeanEvent event) {
		group.listen(event);
	}

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

	public static synchronized void removeListener(Listener<BeanEvent> listener) {
		group.removeListener(listener);
	}

}