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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.yx.annotation.spec.InjectSpec;
import org.yx.annotation.spec.Specs;
import org.yx.bean.watcher.BeanCreateWatcher;
import org.yx.bean.watcher.BeanInjectWatcher;
import org.yx.bean.watcher.PluginBooter;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.common.scaner.ClassScaner;
import org.yx.common.thread.SumkExecutorService;
import org.yx.conf.AppInfo;
import org.yx.db.sql.TableFactory;
import org.yx.exception.SimpleSumkException;
import org.yx.log.Logs;
import org.yx.main.StartConstants;
import org.yx.main.StartContext;
import org.yx.main.SumkThreadPool;
import org.yx.util.CollectionUtil;
import org.yx.util.kit.PriorityKits;

public final class Booter {

	private final Logger logger = Logs.ioc();
	private final List<Consumer<Class<?>>> parallelListeners;
	private final Predicate<String> excludeMatcher;

	public Booter() {
		this.excludeMatcher = createExcludeMatcher();
		this.parallelListeners = CollectionUtil.unmodifyList(getParallelListeners());
		logger.debug("bean exclude matcher:{}", excludeMatcher);
	}

	@SuppressWarnings("unchecked")
	private Consumer<Class<?>>[] getParallelListeners() {
		Consumer<Class<?>>[] defaults = new Consumer[] { new BeanFactory(), new TableFactory() };
		Object obj = StartContext.inst().get("sumk.bean.event.listener");
		return obj instanceof Consumer[] ? (Consumer<Class<?>>[]) obj : defaults;
	}

	public Predicate<String> excludeMatcher() {
		return this.excludeMatcher;
	}

	private Predicate<String> createExcludeMatcher() {
		final String name = "sumk.ioc.exclude";

		List<String> list = new ArrayList<>(AppInfo.subMap(name + ".").values());
		String exclude = AppInfo.get(name, null);
		if (exclude != null) {
			list.add(exclude);
		}
		if (list.isEmpty()) {
			return BooleanMatcher.FALSE;
		}
		StringBuilder sb = new StringBuilder();
		for (String v : list) {
			sb.append(v).append(Matchers.SPLIT);
		}
		return Matchers.createWildcardMatcher(sb.toString(), 2);
	}

	public synchronized void start(List<String> packageNames) throws Exception {
		if (packageNames.isEmpty()) {
			logger.warn("property [sumk.ioc] is empty");
		}
		packageNames = new ArrayList<>(packageNames);

		packageNames.remove(StartConstants.INNER_PACKAGE);
		packageNames.add(0, StartConstants.INNER_PACKAGE);

		initBeans(packageNames);
		autoWiredAll();
		new PluginBooter().start();
	}

	@SuppressWarnings("unchecked")
	private void initBeans(List<String> packageNames) {
		Predicate<String> optional = BooleanMatcher.FALSE;
		Object obj = StartContext.inst().get("sumk.bean.scan.option");
		if (obj instanceof Predicate) {
			optional = (Predicate<String>) obj;
		}

		Collection<String> clzs = ClassScaner.listClasses(packageNames);
		List<Class<?>> clazzList = new ArrayList<>(clzs.size());
		for (String c : clzs) {
			if (excludeMatcher.test(c)) {
				logger.info("{} excluded", c);
				continue;
			}
			try {
				if (logger.isTraceEnabled()) {
					logger.trace("{} begin loading");
				}

				Class<?> clz = Loader.loadClassExactly(c);
				if ((clz.getModifiers() & (Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PUBLIC
						| Modifier.INTERFACE)) != Modifier.PUBLIC || clz.isAnonymousClass() || clz.isLocalClass()
						|| clz.isAnnotation() || clz.isEnum()) {
					continue;
				}
				clazzList.add(clz);
			} catch (LinkageError e) {
				if (c.startsWith("org.yx.") || optional.test(c)) {
					logger.debug("{} ignored because: {}", c, e.getMessage());
					continue;
				}
				logger.error("{}加载失败，原因是:{}", c, e.getLocalizedMessage());
				throw e;
			} catch (Exception e) {
				logger.error(c + "加载失败", e);
			}
		}

		clazzList = PriorityKits.sort(clazzList);
		if (clazzList.size() > 5 && logger.isDebugEnabled()) {
			logger.debug("scan class size:{}, {} {}..{} {}", clazzList.size(), clazzList.get(0).getSimpleName(),
					clazzList.get(1).getSimpleName(), clazzList.get(clazzList.size() - 2).getSimpleName(),
					clazzList.get(clazzList.size() - 1).getSimpleName());
			logger.trace("ordered class:\n{}", clazzList);
		}
		parallelPublish(clazzList, optional);
	}

	private void parallelPublish(List<Class<?>> clazzList, Predicate<String> optional) {
		int size = this.parallelListeners.size();
		CountDownLatch latch = new CountDownLatch(size);
		SumkExecutorService executor = SumkThreadPool.executor();
		for (Consumer<Class<?>> c : parallelListeners) {
			executor.execute(() -> {
				try {
					publish(c, clazzList, optional);
					latch.countDown();
					Logs.ioc().debug("{} finished", c.getClass().getSimpleName());
				} catch (Throwable e) {
					System.exit(1);
				}
			});
		}
		long timeout = AppInfo.getLong("sumk.ioc.publish.timeout", 1000L * 60);
		try {
			if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
				Logs.ioc().error("plugins failed to start in {}ms", timeout);
				System.exit(1);
			}
		} catch (InterruptedException e) {
			Logs.ioc().error("receive InterruptedException in ioc publishing");
			Thread.currentThread().interrupt();
			System.exit(1);
		}
	}

	private void publish(Consumer<Class<?>> consumer, List<Class<?>> clazzList, Predicate<String> optional) {
		for (Class<?> clz : clazzList) {
			try {
				consumer.accept(clz);
			} catch (LinkageError e) {
				String c = clz.getName();
				if (c.startsWith("org.yx.") || optional.test(c)) {
					logger.debug("{} ignored in {} publish because: {}", c, consumer.getClass().getName(),
							e.getMessage());
					continue;
				}
				logger.error("{} publish失败，原因是:{}", consumer.getClass().getName(), e.getLocalizedMessage());
				Logs.ioc().error(e.getMessage(), e);
				throw e;
			}
		}
	}

	private Object getBean(Field f) {
		String name = f.getName();
		Class<?> clz = f.getType();

		List<?> list = InnerIOC.pool.getBeans(name, clz);
		if (list.size() == 1) {
			return list.get(0);
		}
		if (list.size() > 1) {
			for (Object obj : list) {

				if (clz == BeanKit.getTargetClass(obj)) {
					return obj;
				}
			}
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
		final List<Object> beans = CollectionUtil.unmodifyList(InnerIOC.beans().toArray());
		StartContext.inst().setBeans(beans);
		logger.trace("after beans create...");
		for (BeanCreateWatcher w : IOC.getBeans(BeanCreateWatcher.class)) {
			w.afterCreate(beans);
		}
		logger.trace("inject beans properties...");
		for (Object bean : beans) {
			injectProperties(bean);
		}
		logger.trace("after beans installed...");
		for (BeanInjectWatcher watcher : IOC.getBeans(BeanInjectWatcher.class)) {
			watcher.afterInject(beans);
		}
		logger.trace("plugins starting...");
	}

	private void injectProperties(Object bean) throws Exception {
		Class<?> tempClz = bean.getClass();
		Class<?> fieldType;
		while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				InjectSpec inject = Specs.extractInject(bean, f);
				if (inject == null) {
					continue;
				}
				fieldType = f.getType();
				Object target = null;
				if (fieldType.isArray()) {
					target = getArrayField(f, bean, inject.allowEmpty());
				} else if (List.class == fieldType || Collection.class == fieldType) {
					target = getListField(f, bean, inject.allowEmpty());
				} else {
					target = getBean(f);
				}
				if (target == null) {
					if (inject.allowEmpty()) {
						continue;
					}
					throw new SimpleSumkException(-235435658,
							bean.getClass().getName() + "." + f.getName() + " cannot injected.");
				}
				injectField(f, bean, target);
			}
			tempClz = tempClz.getSuperclass();
		}
	}

	private List<?> getListField(Field f, Object bean, boolean allowEmpty) throws ClassNotFoundException {
		String genericName = f.getGenericType().getTypeName();
		if (genericName == null || genericName.isEmpty() || !genericName.contains("<")) {
			throw new SimpleSumkException(-239845611,
					bean.getClass().getName() + "." + f.getName() + "is List,but not List<T>");
		}
		genericName = genericName.substring(genericName.indexOf("<") + 1, genericName.length() - 1);
		Class<?> clz = Loader.loadClassExactly(genericName);
		if (clz == Object.class) {
			throw new SimpleSumkException(-23984568,
					bean.getClass().getName() + "." + f.getName() + ": beanClz of @Inject in list type cannot be null");
		}
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			if (!allowEmpty) {
				throw new SimpleSumkException(-235435652, bean.getClass().getName() + "." + f.getName() + " is empty.");
			}
			return Collections.emptyList();
		}
		return CollectionUtil.unmodifyList(target.toArray());
	}

	private Object[] getArrayField(Field f, Object bean, boolean allowEmpty) {
		Class<?> clz = f.getType().getComponentType();
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			if (!allowEmpty) {
				throw new SimpleSumkException(-235435651, bean.getClass().getName() + "." + f.getName() + " is empty.");
			}
			return (Object[]) Array.newInstance(clz, 0);
		}
		return target.toArray((Object[]) Array.newInstance(clz, target.size()));
	}

}