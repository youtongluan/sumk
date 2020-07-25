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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.yx.annotation.Inject;
import org.yx.bean.watcher.BeanCreateWatcher;
import org.yx.bean.watcher.BeanPropertiesWatcher;
import org.yx.bean.watcher.PluginHandler;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.common.scaner.ClassScaner;
import org.yx.conf.AppInfo;
import org.yx.exception.SimpleSumkException;
import org.yx.listener.ListenerGroup;
import org.yx.listener.ListenerGroupImpl;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.util.kit.PriorityKits;

public final class BeanPublisher {

	private ListenerGroup<BeanEventListener> group = new ListenerGroupImpl<>();

	public synchronized void publishBeans(List<String> packageNames) throws Exception {
		if (packageNames.isEmpty()) {
			Logs.ioc().warn("property [sumk.ioc] is empty");
		}

		packageNames.remove(StartConstants.INNER_PACKAGE);
		packageNames.add(0, StartConstants.INNER_PACKAGE);

		Collection<String> clzs = ClassScaner.listClasses(packageNames);
		List<Class<?>> clazzList = new ArrayList<>(clzs.size());
		for (String c : clzs) {
			try {

				Class<?> clz = Loader.loadClassExactly(c);
				int modify = clz.getModifiers();
				if ((modify & (Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PUBLIC
						| Modifier.INTERFACE)) != Modifier.PUBLIC || clz.isAnonymousClass() || clz.isLocalClass()
						|| clz.isAnnotation() || clz.isEnum()) {
					continue;
				}
				clazzList.add(clz);
			} catch (LinkageError e) {
				if (!c.startsWith("org.yx.")) {
					throw e;
				}
				Logs.ioc().debug("{} ignored.{}", c, e.getMessage());
			} catch (Exception e) {
				Logs.ioc().error(c + "加载失败", e);
			}
		}

		clazzList = PriorityKits.sort(clazzList);
		for (Class<?> clz : clazzList) {
			try {
				publish(new BeanEvent(clz));
			} catch (LinkageError e) {
				if (!clz.getName().startsWith("org.yx.")) {
					throw e;
				}
				Logs.ioc().debug("{} ignored.{}", clz.getName(), e.getMessage());
			}
		}
		if (clazzList.size() > 5) {
			Logs.ioc().debug("scan class size:{}, {} {}..{} {}", clazzList.size(), clazzList.get(0).getSimpleName(),
					clazzList.get(1).getSimpleName(), clazzList.get(clazzList.size() - 2).getSimpleName(),
					clazzList.get(clazzList.size() - 1).getSimpleName());
			Logs.ioc().trace("ordered class:\n{}", clazzList);
		}
		autoWiredAll();
	}

	private Object getBean(Field f, Class<?> clz) {
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

	private void injectField(Field f, Object bean, Object target) throws IllegalAccessException {
		boolean access = f.isAccessible();
		if (!access) {
			f.setAccessible(true);
		}
		f.set(bean, target);
	}

	private void autoWiredAll() throws Exception {
		Logger logger = Logs.ioc();
		Object[] bs = InnerIOC.beans().toArray(new Object[0]);
		final List<Object> beans = Arrays.asList(bs);
		StartContext.inst().setBeans(beans);
		logger.trace("after beans create...");
		IOC.getBeans(BeanCreateWatcher.class).forEach(w -> w.afterCreate(beans));
		logger.trace("inject beans properties...");
		for (Object bean : beans) {
			injectProperties(bean);
		}
		logger.trace("after beans installed...");
		IOC.getBeans(BeanPropertiesWatcher.class).forEach(watcher -> {
			watcher.afterInject(beans);
		});
		logger.trace("plugins starting...");
		PluginHandler.instance.start();
	}

	private void injectProperties(Object bean) throws Exception {
		Class<?> tempClz = bean.getClass();
		while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				Inject inject = f.getAnnotation(Inject.class);
				if (inject != null) {
					final Class<?> clz = inject.beanClz();
					final Class<?> fieldType = f.getType();
					Object target = null;
					if (inject.handler().length() > 0) {
						try {
							target = InjectParser.get(f, inject, bean);
						} catch (Exception e) {
							Log.printStack("sumk.error", e);
							throw new SimpleSumkException(-235435628, bean.getClass().getName() + "." + f.getName()
									+ " cannot injected with " + inject.handler());
						}
					} else if (fieldType.isArray()) {
						target = getArrayField(f, clz, bean);
					} else if (List.class == fieldType || Collection.class == fieldType) {
						target = getListField(f, clz, bean);
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
			}
			tempClz = tempClz.getSuperclass();
		}
	}

	private List<?> getListField(Field f, Class<?> clz, Object bean) throws ClassNotFoundException {
		if (clz == null || clz == Object.class) {
			String genericName = f.getGenericType().getTypeName();
			if (genericName == null || genericName.isEmpty() || !genericName.contains("<")) {
				throw new SimpleSumkException(-239845611,
						"" + bean.getClass().getName() + "." + f.getName() + "is List,but not List<T>");
			}
			genericName = genericName.substring(genericName.indexOf("<") + 1, genericName.length() - 1);
			clz = Class.forName(genericName);
		}
		if (clz == Object.class) {
			throw new SimpleSumkException(-23984568, "" + bean.getClass().getName() + "." + f.getName()
					+ ": beanClz of @Inject in list type cannot be null");
		}
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			return allowEmptyCollection() ? Collections.emptyList() : null;
		}
		return target;
	}

	private boolean allowEmptyCollection() {
		return AppInfo.getBoolean("sumk.ioc.inject.allowEmpty", true);
	}

	private Object getArrayField(Field f, Class<?> clz, Object bean) {
		Class<?> filedType = f.getType().getComponentType();
		if (clz == null || clz == Object.class) {
			clz = filedType;
		} else if (!filedType.isAssignableFrom(clz)) {
			throw new SimpleSumkException(-239864568, bean.getClass().getName() + "." + f.getName() + " is "
					+ filedType.getSimpleName() + "[], is not " + clz.getSimpleName() + "[]");
		}
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			return allowEmptyCollection() ? Array.newInstance(filedType, 0) : null;
		}
		return target.toArray((Object[]) Array.newInstance(filedType, target.size()));
	}

	private void publish(BeanEvent event) {
		group.listen(event);
	}

	public synchronized void setListeners(BeanEventListener[] array) {
		group.setListener(array);
	}
}