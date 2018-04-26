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
package org.yx.common;

import java.util.HashMap;
import java.util.Map;

public class StartContext {

	public static StartContext inst = new StartContext();

	private Map<String, Object> map = new HashMap<>();

	public void put(String key, Object obj) {
		map.put(key, obj);
	}

	public void put(Class<?> clz, Object obj) {
		map.put(clz.getName(), obj);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public Object get(Class<?> clz) {
		return map.get(clz.getName());
	}

	@SuppressWarnings("unchecked")
	public <T> T getOrCreate(String key, T t) {
		if (map.containsKey(key)) {
			return (T) map.get(key);
		}
		map.put(key, t);
		return t;
	}

	public <T> T getOrCreate(Class<?> clz, T t) {
		return this.getOrCreate(clz.getName(), t);
	}

	public static void clear() {
		inst.map = null;
		inst = null;
	}

}
