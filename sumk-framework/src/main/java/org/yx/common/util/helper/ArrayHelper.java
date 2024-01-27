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
package org.yx.common.util.helper;

import java.util.Objects;
import java.util.function.IntFunction;

public final class ArrayHelper {

	public static <T> T[] add(T[] old, T obj, IntFunction<T[]> arrayFactory) {
		if (obj == null) {
			return old;
		}
		if (old == null || old.length == 0) {
			T[] ret = arrayFactory.apply(1);
			ret[0] = obj;
			return ret;
		}
		for (T f : old) {
			if (f.equals(obj)) {
				return old;
			}
		}
		T[] ret = arrayFactory.apply(old.length + 1);
		System.arraycopy(old, 0, ret, 0, old.length);
		ret[old.length] = obj;
		return ret;
	}

	public static <T> T[] remove(T[] old, T obj, IntFunction<T[]> arrayFactory) {
		if (old == null || old.length == 0 || obj == null) {
			return old;
		}
		for (int i = 0; i < old.length; i++) {
			T f = old[i];
			if (Objects.equals(f, obj)) {
				T[] ret = arrayFactory.apply(old.length - 1);
				System.arraycopy(old, 0, ret, 0, i);
				System.arraycopy(old, i + 1, ret, i, ret.length - i);
				return ret;
			}
		}
		return old;
	}
}
