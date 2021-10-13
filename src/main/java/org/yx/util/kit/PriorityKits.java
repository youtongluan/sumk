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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.yx.annotation.Priority;
import org.yx.conf.Const;

public class PriorityKits {

	private static final Comparator<Class<?>> priorityComparator = (a, b) -> {
		int v = a.getAnnotation(Priority.class).value() - b.getAnnotation(Priority.class).value();
		if (v != 0) {
			return v;
		}
		return a.getName().compareTo(b.getName());
	};

	public static List<Class<?>> sort(Collection<Class<?>> source) {
		List<Class<?>> low = new ArrayList<>();
		List<Class<?>> high = new ArrayList<>();
		Map<String, Class<?>> middle = new TreeMap<>();
		for (Class<?> c : source) {
			Priority p = c.getAnnotation(Priority.class);
			if (p == null || p.value() == Const.DEFAULT_ORDER) {
				middle.put(c.getName(), c);
			} else if (p.value() < Const.DEFAULT_ORDER) {
				low.add(c);
			} else {
				high.add(c);
			}
		}
		low.sort(priorityComparator);
		high.sort(priorityComparator);
		List<Class<?>> ret = new ArrayList<>(source.size());
		ret.addAll(low);
		ret.addAll(middle.values());
		ret.addAll(high);
		return ret;
	}
}
