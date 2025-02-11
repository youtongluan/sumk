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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.annotation.spec.InjectSpec;
import org.yx.annotation.spec.Specs;
import org.yx.base.context.AppContext;
import org.yx.base.matcher.BooleanMatcher;
import org.yx.base.matcher.Matchers;
import org.yx.base.scaner.ClassScaner;
import org.yx.bean.aop.AopExecutorManager;
import org.yx.bean.aop.AopExecutorSupplier;
import org.yx.bean.aop.asm.AsmUtils;
import org.yx.bean.watcher.BeanCreateWatcher;
import org.yx.bean.watcher.BeanInjectWatcher;
import org.yx.bean.watcher.BootWatcher;
import org.yx.common.util.kit.PriorityKits;
import org.yx.conf.Const;
import org.yx.exception.SimpleSumkException;
import org.yx.log.Logs;
import org.yx.main.SumkServer;
import org.yx.util.CollectionUtil;
import org.yx.util.Loader;
import org.yx.util.StringUtil;

public final class Booter {

	private final Logger logger = Logs.ioc();

	private static final String FACTORIES_FILE = "META-INF/sumk.factories";
	private static final String SUMK_IOC_EXCLUDE = "sumk.ioc.exclude";
	/**
	 * 这里定义精确扫描的类，这些类也需要@Bean注解
	 */
	private static final String SUMK_IOC_BEAN = "sumk.ioc.bean";

	private static final String SUMK_IOC_BOOT_WATCHER = "sumk.ioc.boot.watcher";
	/**
	 * 符合这里表达式的类，即使加载出错，即使出了问题，也认为是正常的。 这里的加载出错指的是class文件能找到，但是它的关联类加载出错
	 */
	private static final String SUMK_IOC_OPTIONAL = "sumk.ioc.optional";

	public void start(List<String> packageNames) throws Exception {
		onStart(packageNames);
		BeanScanerInstance definition = this.buildBeanScanerInstance(packageNames);
		List<Class<?>> orignClazzList = this.loadAndSortClasses(definition);
		List<BootWatcher> watchers = loadWatcher(definition);
		this.refresh(orignClazzList, watchers, definition);
		this.autoWiredAll(definition);
		new PluginBooter().start();
		Runtime.getRuntime().addShutdownHook(new Thread(SumkServer::destroy));
	}

	private BeanScanerInstance buildBeanScanerInstance(List<String> packageNames) throws IOException {
		FactoriesInstance fi = this.parseFactoriesInstance();
		BeanScanerInstance definition = new BeanScanerInstance(packageNames, fi);
		logger.debug("finally bean scan instance:{}", definition);
		return definition;
	}

	private void onStart(List<String> packageNames) {

		AopExecutorManager.reset();
		InnerIOC.clear();
		AsmUtils.clearProxyClassLoaders();
		if (packageNames.isEmpty()) {
			logger.warn("property [sumk.ioc] is empty");
			return;
		}
		logger.debug("start ioc with packages:[{}]", packageNames);
	}

	/**
	 * 包含了从配置以及jar中的meta文件加载的
	 * 
	 * @return
	 * @throws IOException
	 */
	private FactoriesInstance parseFactoriesInstance() throws IOException {
		FactoriesInstance ret = FactoriesInstance.fromContext();
		Enumeration<URL> urls = Loader.getResources(FACTORIES_FILE);
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			try (InputStream is = url.openStream()) {
				Properties properties = new Properties();
				properties.load(is);
				FactoriesInstance f = FactoriesInstance.fromProperties(properties);
				logger.debug("factories [{}]: {}", url, f);
				ret.addAll(f);
			}
		}
		return ret;
	}

	private List<Class<?>> unmodifyClassList(List<Class<?>> list) {
		return CollectionUtil.unmodifyList(list.toArray(new Class<?>[list.size()]));
	}

	private void refresh(List<Class<?>> clazzList, List<BootWatcher> watchers, BeanScanerInstance definition)
			throws Exception {
		clazzList = unmodifyClassList(clazzList);
		watchers.sort(null);
		for (BootWatcher b : watchers) {
			List<Class<?>> temp = b.publish(clazzList, definition.optionalMatcher);
			if (temp != null) {
				clazzList = unmodifyClassList(temp);
			}
		}
	}

	private List<Class<?>> loadAndSortClasses(BeanScanerInstance definition) throws Exception {
		Collection<String> clzsFromPackage = ClassScaner.listClasses(definition.packageNames);
		Set<String> clzs = new HashSet<>(clzsFromPackage);
		if (CollectionUtil.isNotEmpty(definition.beans)) {
			clzs.addAll(definition.beans);
		}
		List<Class<?>> clazzList = new ArrayList<>(clzs.size());

		for (String c : clzs) {
			Class<?> clz = this.loadClass(c, definition);
			if (clz == null) {
				continue;
			}
			if (AopExecutorSupplier.class.isAssignableFrom(clz) && clz.isAnnotationPresent(Bean.class)) {
				InnerIOC.putBean(null, Loader.newInstance(clz));
				continue;
			}
			clazzList.add(clz);
		}

		List<Class<?>> sortedClazzList = PriorityKits.sort(clazzList);
		this.printClassListDebugInfo(sortedClazzList);
		return sortedClazzList;
	}

	private void printClassListDebugInfo(List<Class<?>> sortedClazzList) {
		if (!logger.isDebugEnabled()) {
			return;
		}
		logger.debug("scan class size:{}, {} {}..{} {}", sortedClazzList.size(), sortedClazzList.get(0).getSimpleName(),
				sortedClazzList.get(1).getSimpleName(), sortedClazzList.get(sortedClazzList.size() - 2).getSimpleName(),
				sortedClazzList.get(sortedClazzList.size() - 1).getSimpleName());
		logger.trace("ordered class:\n{}", sortedClazzList);
	}

	private List<BootWatcher> loadWatcher(BeanScanerInstance definition) throws Exception {
		Set<String> ws = definition.watchers;
		if (CollectionUtil.isEmpty(ws)) {
			return Collections.emptyList();
		}
		List<BootWatcher> watchers = new ArrayList<>();
		for (String w : ws) {
			Class<?> bw = this.loadClass(w, definition);
			if (bw == null) {
				continue;
			}
			if (!BootWatcher.class.isAssignableFrom(bw)) {
				logger.error("[{}] must implement BootWatcher", bw);
				AppContext.startFailed();
				continue;
			}
			watchers.add((BootWatcher) Loader.newInstance(bw));
		}
		return watchers;
	}

	private Class<?> loadClass(String fullName, BeanScanerInstance definition) {
		if (definition.excludeMatcher.test(fullName)) {
			logger.info("[{}] ingored by [{}]", fullName, SUMK_IOC_EXCLUDE);
			return null;
		}
		try {
			if (logger.isTraceEnabled()) {
				logger.trace("{} begin loading", fullName);
			}

			Class<?> clz = Loader.loadClass(fullName);
			if ((clz.getModifiers() & (Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PUBLIC
					| Modifier.INTERFACE)) != Modifier.PUBLIC || clz.isAnonymousClass() || clz.isLocalClass()
					|| clz.isAnnotation() || clz.isEnum()) {
				logger.trace("{} ingored because it is not normal public class", fullName);
				return null;
			}
			return clz;
		} catch (Throwable e) {
			if ((e instanceof LinkageError) && definition.optionalMatcher.test(fullName)) {
				logger.debug("{} ignored when load because: [{}]", fullName, e);
				return null;
			}
			logger.error(fullName + "加载失败", e);
			AppContext.startFailed();
			throw new RuntimeException(e);
		}
	}

	private void injectField(Field f, Object bean, Object target) throws IllegalAccessException {
		if (!f.isAccessible()) {
			f.setAccessible(true);
		}
		f.set(bean, target);
	}

	private void autoWiredAll(BeanScanerInstance definition) throws Exception {
		List<Object> beans = CollectionUtil.unmodifyList(InnerIOC.beans().toArray());
		logger.trace("after beans create...");
		for (BeanCreateWatcher w : IOC.getBeans(BeanCreateWatcher.class)) {
			w.afterCreate(beans);
		}
		logger.trace("inject beans properties...");
		beans = CollectionUtil.unmodifyList(InnerIOC.beans().toArray());
		for (Object bean : beans) {
			injectProperties(bean, definition);
		}
		logger.trace("after beans installed...");
		for (BeanInjectWatcher watcher : IOC.getBeans(BeanInjectWatcher.class)) {
			watcher.afterInject(beans);
		}
		logger.trace("plugins starting...");
	}

	private void injectProperties(Object bean, BeanScanerInstance definition) throws Exception {
		Class<?> tempClz = bean.getClass();
		while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				InjectSpec inject = Specs.extractInject(bean, f);
				if (inject == null) {
					continue;
				}
				Object target = definition.fieldFinder.findTarget(f, bean, inject);
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

	private static final class FactoriesInstance {

		final Set<String> beans = new HashSet<>();

		final Set<String> bootWatchers = new HashSet<>();

		final Set<String> excludes = new HashSet<>();

		final Set<String> optionals = new HashSet<>();

		private static void addValues(Set<String> set, String factoryNamesProperty) {
			if (StringUtil.isEmpty(factoryNamesProperty)) {
				return;
			}
			List<String> terms = StringUtil.splitAndTrim(factoryNamesProperty, Const.COMMA);
			set.addAll(terms);
		}

		public void addAll(FactoriesInstance f) {
			this.beans.addAll(f.beans);
			this.bootWatchers.addAll(f.bootWatchers);
			this.excludes.addAll(f.excludes);
			this.optionals.addAll(f.optionals);
		}

		public static FactoriesInstance fromProperties(Properties properties) {
			FactoriesInstance fi = new FactoriesInstance();
			addValues(fi.beans, properties.getProperty(SUMK_IOC_BEAN));
			addValues(fi.bootWatchers, properties.getProperty(SUMK_IOC_BOOT_WATCHER));
			addValues(fi.excludes, properties.getProperty(SUMK_IOC_EXCLUDE));
			addValues(fi.optionals, properties.getProperty(SUMK_IOC_OPTIONAL));
			return fi;
		}

		public static FactoriesInstance fromContext() {
			FactoriesInstance fi = new FactoriesInstance();
			addValues(fi.beans, AppContext.inst().getAppInfo(SUMK_IOC_BEAN, ""));
			addValues(fi.bootWatchers, AppContext.inst().getAppInfo(SUMK_IOC_BOOT_WATCHER, ""));
			addValues(fi.excludes, AppContext.inst().getAppInfo(SUMK_IOC_EXCLUDE, ""));
			addValues(fi.optionals, AppContext.inst().getAppInfo(SUMK_IOC_OPTIONAL, ""));
			return fi;
		}

		@Override
		public String toString() {
			return "FactoriesInstance [beans=" + beans + ", bootWatchers=" + bootWatchers + ", excludes=" + excludes
					+ ", optionals=" + optionals + "]";
		}

	}

	private static final class BeanScanerInstance {

		final BeanFieldFinder fieldFinder = AppContext.inst().get(BeanFieldFinder.class, new DefaultBeanFieldFinder());

		final List<String> packageNames;

		final Set<String> beans;

		final Set<String> watchers;

		final Predicate<String> excludeMatcher;

		final Predicate<String> optionalMatcher;

		public BeanScanerInstance(List<String> packageNames, FactoriesInstance fi) {
			this.packageNames = packageNames;
			this.beans = fi.beans;
			this.watchers = fi.bootWatchers;
			this.excludeMatcher = createWildcardMatcher(fi.excludes);
			this.optionalMatcher = createWildcardMatcher(fi.optionals);
		}

		private Predicate<String> createWildcardMatcher(Collection<String> patterns) {
			if (CollectionUtil.isEmpty(patterns)) {
				return BooleanMatcher.FALSE;
			}
			StringBuilder sb = new StringBuilder();
			for (String v : patterns) {
				sb.append(v).append(Matchers.SPLIT);
			}
			return Matchers.createWildcardMatcher(sb.toString(), 2);
		}

		@Override
		public String toString() {
			return "BeanScanerInstance [packageNames=" + packageNames + ", excludeMatcher=" + excludeMatcher
					+ ", optionalMatcher=" + optionalMatcher + ", beans=" + beans + ", watchers=" + watchers + "]";
		}
	}

}