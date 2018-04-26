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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InjectParser {

	private static Map<String, Object> map = new ConcurrentHashMap<>();

	static Object get(Field field, Inject inject, Object bean)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String handler = inject.handler();
		Object handlerClz = map.get(handler);
		BeanHandler beanHandler = null;
		if (handlerClz == null) {
			beanHandler = (BeanHandler) map.get(handler);
			if (beanHandler == null) {
				Class<?> clz = Loader.loadClass(handler);
				beanHandler = (BeanHandler) clz.newInstance();
				map.put(handler, beanHandler);
			}
			return beanHandler.handle(inject, field);
		}
		if (String.class.isInstance(handlerClz)) {
			Class<?> clz = Loader.loadClass((String) handlerClz);
			beanHandler = (BeanHandler) clz.newInstance();
			map.put(handler, beanHandler);
			return beanHandler.handle(inject, field);
		}
		if (BeanHandler.class.isInstance(handlerClz)) {
			beanHandler = (BeanHandler) handlerClz;
			return beanHandler.handle(inject, field);
		}
		return null;
	}
}
