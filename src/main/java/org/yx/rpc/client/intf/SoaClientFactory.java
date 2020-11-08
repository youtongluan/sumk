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
package org.yx.rpc.client.intf;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.annotation.Exclude;
import org.yx.annotation.Priority;
import org.yx.bean.FactoryBean;
import org.yx.bean.IOC;
import org.yx.bean.InterfaceBean;
import org.yx.bean.Loader;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.common.scaner.ClassScaner;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

@Priority(20000)
@Bean
public class SoaClientFactory implements FactoryBean {

	protected Set<String> getInterfaceNames() {
		Predicate<String> exclude = BooleanMatcher.FALSE;
		String patterns = AppInfo.get("sumk.rpc.intfclient.exclude", null);
		if (patterns != null) {
			exclude = Matchers.createWildcardMatcher(patterns, 1);
		}

		String packs = AppInfo.getLatin("sumk.rpc.intfclient.package", null);
		Set<String> clzNames = new HashSet<>();
		if (packs != null) {
			String[] ps = packs.split(",");
			Collection<String> temp = ClassScaner.listClasses(Arrays.asList(ps));
			for (String c : temp) {
				if (exclude.test(c)) {
					continue;
				}
				clzNames.add(c);
			}
		}
		String interfaces = AppInfo.getLatin("sumk.rpc.intfclient.interface", null);
		if (StringUtil.isNotEmpty(interfaces)) {
			for (String s : interfaces.split(",")) {
				s = s.trim();
				if (s.length() > 0) {
					if (exclude.test(s)) {
						continue;
					}
					clzNames.add(s);
				}
			}
		}
		return clzNames;
	}

	@Override
	public Collection<Object> beans() {

		if (AppInfo.getBoolean("sumk.rpc.intfclient.disable", false)) {
			return Collections.emptyList();
		}
		Logger log = Logs.ioc();
		List<Object> ret = new ArrayList<>();

		Set<String> clzNames = this.getInterfaceNames();
		for (String c : clzNames) {
			try {
				Class<?> intf = Loader.loadClassExactly(c);
				if (!intf.isInterface() || (intf.getModifiers() & Modifier.PUBLIC) == 0) {
					continue;
				}
				if (intf.isAnnotationPresent(Exclude.class)) {
					continue;
				}
				if (IOC.get(intf) != null) {
					log.debug("{}接口已经有对应的bean，类型为{},就不创建微服务客户端了", intf.getName(), IOC.get(intf).getClass().getName());
					continue;
				}
				Method[] ms = intf.getMethods();
				if (ms == null || ms.length == 0) {
					log.debug("{}接口没有任何方法，被过滤掉", c);
					continue;
				}
				log.debug("add soa client interface {}", c);
				ret.add(new InterfaceBean(intf, proxyInterface(intf)));
			} catch (NoClassDefFoundError e) {
				log.warn("soa client interface {} ignored.{}", c, e.getMessage());
			} catch (Exception e) {
				Log.printStack("sumk.error", e);
			}
		}
		return ret;
	}

	protected Object proxyInterface(Class<?> intf) {
		return Proxy.newProxyInstance(Loader.loader(), new Class[] { intf }, getInvocationHandler(intf));
	}

	protected InvocationHandler getInvocationHandler(Class<?> intf) {
		return InvocationHandlerFactory.create(intf);
	}

}
