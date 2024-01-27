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
package org.yx.base.sumk.map;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yx.util.CollectionUtil;

/**
 * 支持key、value为null
 */
public class UnmodifiableListMap<K, V> extends AbstractMap<K, V> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ListEntrySet<K, V> data;

	public UnmodifiableListMap(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.size());
		for (Entry<K, V> en : map.entrySet()) {
			list.add(new AbstractMap.SimpleImmutableEntry<>(en));
		}
		this.data = new ListEntrySet<>(CollectionUtil.unmodifyList(list));
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return data;
	}

}
