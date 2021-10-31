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

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

public class ListEntrySet<K, V> extends AbstractSet<Entry<K, V>> {

	protected final List<Entry<K, V>> list;

	public ListEntrySet(List<Entry<K, V>> list) {
		this.list = Objects.requireNonNull(list);
	}

	@Override
	public boolean add(Entry<K, V> e) {
		Entry<K, V> old = this.getByKey(e.getKey());
		if (old != null) {
			old.setValue(e.getValue());
			return true;
		}
		return list.add(e);
	}

	@Override
	public int size() {
		return list.size();
	}

	public Entry<K, V> getByKey(K key) {
		int index = this.indexKey(key);
		if (index < 0) {
			return null;
		}
		return list.get(index);
	}

	public int indexKey(K key) {
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			Entry<K, V> en = list.get(i);
			if (Objects.equals(en.getKey(), key)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return list.iterator();
	}
}
