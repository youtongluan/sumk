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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.yx.annotation.Inject;

public final class InjectParser {

	private static ConcurrentMap<String, BeanHandler> map = new ConcurrentHashMap<>();

	static Object get(Field field, Inject inject, Object bean) throws Exception {
		String handler = inject.handler();
		if (handler == null || handler.isEmpty()) {
			return null;
		}
		BeanHandler beanHandler = map.get(handler);
		if (beanHandler == null) {
			Class<?> clz = Loader.loadClass(handler);
			beanHandler = (BeanHandler) Loader.newInstance(clz);
			map.put(handler, beanHandler);
		}
		return beanHandler.handle(inject, field);
	}

	public static void putAlias(String alias, BeanHandler beanHandler) {
		map.put(alias, beanHandler);
	}
}
