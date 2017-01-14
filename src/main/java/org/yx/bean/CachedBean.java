/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.util.concurrent.ConcurrentHashMap;

import org.yx.exception.SumkException;

/**
 * 对IOC中的bean进行缓存，以加快获取速度
 * 
 * @author 游夏
 *
 */
public final class CachedBean {
	private static ConcurrentHashMap<Class<?>, Object> map = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> intf) {
		Object obj = map.get(intf);
		if (obj == null) {
			obj = IOC.get(intf);
			if (obj == null) {
				return null;
			}
			if (!intf.isInstance(obj)) {
				SumkException.throwException(65465435, obj.getClass().getName() + " is not an instance of " + intf);
			}
			map.put(intf, obj);
		}
		return (T) obj;
	}

}
