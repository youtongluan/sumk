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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.yx.annotation.spec.InjectSpec;
import org.yx.annotation.spec.Specs;
import org.yx.base.context.AppContext;
import org.yx.base.matcher.BooleanMatcher;
import org.yx.base.matcher.Matchers;
import org.yx.base.scaner.ClassScaner;
import org.yx.bean.aop.AopExecutorManager;
import org.yx.bean.aop.AopExecutorSupplier;
import org.yx.bean.watcher.BeanCreateWatcher;
import org.yx.bean.watcher.BeanInjectWatcher;
import org.yx.bean.watcher.BootWatcher;
import org.yx.common.util.kit.PriorityKits;
import org.yx.conf.AppInfo;
import org.yx.exception.SimpleSumkException;
import org.yx.log.Logs;
import org.yx.main.StartConstants;
import org.yx.main.SumkServer;
import org.yx.util.CollectionUtil;
import org.yx.util.Loader;

public final class Booter {

	private final Logger logger = Logs.ioc();
	private final Predicate<String> excludeMatcher;
	private final BeanFieldFinder fieldFinder;
	private final List<BootWatcher> watchers = new ArrayList<>();

	public Booter() {
		this.excludeMatcher = createExcludeMatcher();
		logger.debug("bean exclude matcher:{}", excludeMatcher);
		this.fieldFinder = AppContext.inst().get(BeanFieldFinder.class, new DefaultBeanFieldFinder());
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

	public void start(List<String> packageNames) throws Exception {
		packageNames = rehandle(packageNames);
		Predicate<String> optional = this.createOptional();
		List<Class<?>> orignClazzList = this.loadClasses(packageNames, optional);
		this.publish(orignClazzList, optional);
		this.autoWiredAll();
		new PluginBooter().start();
		Runtime.getRuntime().addShutdownHook(new Thread(SumkServer::stop));
	}

	private List<Class<?>> unmodifyClassList(List<Class<?>> list) {
		return CollectionUtil.unmodifyList(list.toArray(new Class<?>[list.size()]));
	}

	private void publish(List<Class<?>> clazzList, Predicate<String> optional) throws Exception {
		clazzList = unmodifyClassList(clazzList);
		for (BootWatcher b : this.watchers) {
			List<Class<?>> temp = b.publish(clazzList, optional);
			if (temp != null) {
				clazzList = unmodifyClassList(temp);
			}
		}
	}

	private List<String> rehandle(List<String> packageNames) {
		if (packageNames.isEmpty()) {
			logger.warn("property [sumk.ioc] is empty");
		}
		packageNames = new ArrayList<>(packageNames);
		if (!packageNames.contains(StartConstants.INNER_PACKAGE)) {
			packageNames.add(StartConstants.INNER_PACKAGE);
		}
		return packageNames;
	}

	@SuppressWarnings("unchecked")
	private Predicate<String> createOptional() {
		Object obj = AppContext.inst().get("sumk.bean.scan.option");
		if (obj instanceof Predicate) {
			return (Predicate<String>) obj;
		}
		return Matchers.createWildcardMatcher(AppInfo.get("sumk.ioc.optional"));
	}

	private List<Class<?>> loadClasses(List<String> packageNames, Predicate<String> optional) throws Exception {
		Collection<String> clzs = ClassScaner.listClasses(packageNames);
		List<Class<?>> clazzList = new ArrayList<>(clzs.size());
		List<AopExecutorSupplier> advisors = new ArrayList<>();

		Predicate<String> excludeBooter = Matchers.createWildcardMatcher("sumk.ioc.booter.exclude", 1);
		for (String c : clzs) {
			if (excludeMatcher.test(c)) {
				logger.info("{} excluded", c);
				continue;
			}
			try {
				if (logger.isTraceEnabled()) {
					logger.trace("{} begin loading", c);
				}

				Class<?> clz = Loader.loadClass(c);
				if ((clz.getModifiers() & (Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PUBLIC
						| Modifier.INTERFACE)) != Modifier.PUBLIC || clz.isAnonymousClass() || clz.isLocalClass()
						|| clz.isAnnotation() || clz.isEnum()) {
					continue;
				}
				clazzList.add(clz);
				if (excludeBooter.test(clz.getName())) {
					continue;
				}
				if (BootWatcher.class.isAssignableFrom(clz)) {
					watchers.add((BootWatcher) Loader.newInstance(clz));
				}
				if (AopExecutorSupplier.class.isAssignableFrom(clz)) {
					advisors.add((AopExecutorSupplier) Loader.newInstance(clz));
				}
			} catch (LinkageError e) {
				if (c.startsWith("org.yx.") || optional.test(c)) {
					logger.debug("{} ignored because: {}", c, e.getMessage());
					continue;
				}
				logger.error("{}加载失败，原因是:{}", c, e.getLocalizedMessage());
				throw e;
			}
		}

		watchers.sort(null);
		AopExecutorManager cm = new AopExecutorManager(advisors);
		InnerIOC.pool.setAopExecutorManager(cm);
		clazzList = PriorityKits.sort(clazzList);
		if (clazzList.size() > 5 && logger.isDebugEnabled()) {
			logger.debug("scan class size:{}, {} {}..{} {}", clazzList.size(), clazzList.get(0).getSimpleName(),
					clazzList.get(1).getSimpleName(), clazzList.get(clazzList.size() - 2).getSimpleName(),
					clazzList.get(clazzList.size() - 1).getSimpleName());
			logger.trace("ordered class:\n{}", clazzList);
		}
		return clazzList;
	}

	private void injectField(Field f, Object bean, Object target) throws IllegalAccessException {
		if (!f.isAccessible()) {
			f.setAccessible(true);
		}
		f.set(bean, target);
	}

	private void autoWiredAll() throws Exception {
		List<Object> beans = CollectionUtil.unmodifyList(InnerIOC.beans().toArray());
		logger.trace("after beans create...");
		for (BeanCreateWatcher w : IOC.getBeans(BeanCreateWatcher.class)) {
			w.afterCreate(beans);
		}
		logger.trace("inject beans properties...");
		beans = CollectionUtil.unmodifyList(InnerIOC.beans().toArray());
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
		while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				InjectSpec inject = Specs.extractInject(bean, f);
				if (inject == null) {
					continue;
				}
				Object target = this.fieldFinder.findTarget(f, bean, inject);
				if (target == null) {
					if (inject.allowEmpty()) {
						continue;
					}
					throw new SimpleSumkException(235435658, bean.getClass().getName() + "." + f.getName()
							+ " cannot injected. because bean not founds");
				}
				injectField(f, bean, target);
			}
			tempClz = tempClz.getSuperclass();
		}
	}

}