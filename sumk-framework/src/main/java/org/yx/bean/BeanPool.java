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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.yx.bean.aop.AopContext;
import org.yx.bean.aop.AopExecutor;
import org.yx.bean.aop.AopExecutorManager;
import org.yx.bean.aop.asm.AsmUtils;
import org.yx.bean.aop.asm.ProxyClassVistor;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.log.Logs;
import org.yx.util.Loader;
import org.yx.util.StringUtil;

public final class BeanPool {

	private final ConcurrentMap<String, NameSlot> slotMap = new ConcurrentHashMap<>(128);
	private AopExecutorManager manager = new AopExecutorManager(Collections.emptyList());

	public AopExecutorManager aopExecutorManager() {
		return manager;
	}

	void setAopExecutorManager(AopExecutorManager manager) {
		this.manager = manager;
	}

	private Class<?> proxyIfNeed(Class<?> clz) throws Exception {

		Method[] methods = clz.getDeclaredMethods();
		Map<Method, Integer> aopMethods = new HashMap<>();

		for (Method m : methods) {
			int modifier = m.getModifiers();
			if (!AsmUtils.canProxy(modifier)) {
				continue;
			}
			if (!Modifier.isPublic(modifier) && !Modifier.isProtected(modifier)) {
				continue;
			}
			List<AopContext> list = manager.willProxyExcutorSuppliers(clz, m);
			if (list.isEmpty()) {
				continue;
			}
			Class<?> returnType = m.getReturnType();
			if (returnType != null && returnType != Void.TYPE && returnType.isPrimitive()) {
				Logs.aop().warn("{}.{}()的返回值是{}，这个方法aop无法屏蔽异常！", clz.getName(), m.getName(), returnType);
			}
			int index = manager.indexSupplier(list);
			aopMethods.put(m, index);
			if (Logs.aop().isDebugEnabled()) {
				List<AopExecutor> aopList = new ArrayList<>(list.size());
				for (AopContext c : list) {
					aopList.add(c.getAopExecutor());
				}
				Logs.aop().debug("{}.{}被{}代理了", clz.getName(), m.getName(), aopList);
			}
		}

		if (aopMethods.isEmpty()) {
			return clz;
		}

		ClassReader cr = new ClassReader(AsmUtils.openStreamForClass(clz.getName()));

		ClassWriter cw = new ClassWriter(cr, AppInfo.getInt("sumk.asm.writer.box.compute", ClassWriter.COMPUTE_MAXS));

		String newClzName = AsmUtils.proxyCalssName(clz);
		ProxyClassVistor cv = new ProxyClassVistor(cw, newClzName, clz, aopMethods);
		cr.accept(cv, AsmUtils.asmVersion());
		return AsmUtils.loadClass(newClzName, cw.toByteArray());
	}

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
		Class<?> proxyClz = this.proxyIfNeed(clz);
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
		Logs.ioc().debug("add bean {} : {}", name, bean);
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
