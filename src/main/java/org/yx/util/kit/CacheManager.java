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
package org.yx.util.kit;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class CacheManager<K, V> {
	private final Map<K, V> cache;

	public static CacheManager<Class<?>, Field[]> create(BeanConverter bc) {
		return new CacheManager<Class<?>, Field[]>(bc.getCache());
	}

	public CacheManager(Map<K, V> cache) {
		this.cache = cache;
	}

	public void clear() {
		this.cache.clear();
	}

	public V remove(K key) {
		return this.cache.remove(key);
	}

	public Set<K> keySet(K key) {
		return this.cache.keySet();
	}

	public int size() {
		return this.cache.size();
	}
}
