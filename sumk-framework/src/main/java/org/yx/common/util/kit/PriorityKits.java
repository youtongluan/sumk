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
package org.yx.common.util.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.yx.annotation.Priority;
import org.yx.conf.Const;

public class PriorityKits {

	private static final Comparator<Class<?>> priorityComparator = (a, b) -> {
		int pa = getPriority(a);
		int pb = getPriority(b);
		if (pa == pb) {
			return a.getName().compareTo(b.getName());
		}
		return Integer.compare(pa, pb);
	};

	public static int getPriority(Class<?> clz) {
		Priority p = clz.getAnnotation(Priority.class);
		if (p == null) {
			return Const.DEFAULT_ORDER;
		}
		return p.value();
	}

	public static List<Class<?>> sort(Collection<Class<?>> source) {
		List<Class<?>> list = new ArrayList<>(source);
		list.sort(priorityComparator);
		return list;
	}

	/**
	 * 
	 * @param sortedClasses 已排序好的列表
	 * @return 会有3个item，第一个是Priority小于默认值的，第二个是等于默认值或者没有@Priority注解，第三个Priority大于默认值的
	 */
	public static List<List<Class<?>>> split(List<Class<?>> sortedClasses) {
		List<Class<?>> low = new ArrayList<>();
		List<Class<?>> high = new ArrayList<>();
		List<Class<?>> middle = new ArrayList<>();
		final int size = sortedClasses.size();
		for (int i = 0; i < size; i++) {
			Class<?> clz = sortedClasses.get(i);
			int p = getPriority(clz);
			if (p == Const.DEFAULT_ORDER) {
				middle.add(clz);
			} else if (p < Const.DEFAULT_ORDER) {
				low.add(clz);
			} else {
				high.add(clz);
			}
		}
		return Arrays.asList(low, middle, high);
	}
}
