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
package org.yx.common.sumk.map;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ListMap<K, V> extends AbstractMap<K, V> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ListEntrySet<K, V> data;

	public ListMap() {
		this.data = new ListEntrySet<K, V>(new ArrayList<>());
	}

	public ListMap(int initialCapacity) {
		this.data = new ListEntrySet<K, V>(new ArrayList<>(initialCapacity));
	}

	public ListMap(Map<K, V> map) {
		this.data = new ListEntrySet<K, V>(new ArrayList<>());
		for (Entry<K, V> en : map.entrySet()) {
			data.add(new AbstractMap.SimpleEntry<>(en));
		}
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return data;
	}

	@Override
	public V put(K key, V value) {
		Entry<K, V> old = data.getByKey(key);
		V v = old != null ? old.getValue() : null;
		data.add(new AbstractMap.SimpleEntry<>(key, value));
		return v;
	}

}
