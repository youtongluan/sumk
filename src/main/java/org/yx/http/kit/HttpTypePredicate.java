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
package org.yx.http.kit;

import java.util.Objects;
import java.util.function.BiPredicate;

public final class HttpTypePredicate {

	private static BiPredicate<String[], String> predicate = (types, type) -> {
		if (type == null) {
			return types == null || types.length == 0;
		}
		if (types == null) {
			return type.isEmpty();
		}
		for (String t : types) {
			if (type.equals(t)) {
				return true;
			}
		}
		return false;
	};

	public static boolean test(String[] types, String type) {
		return predicate.test(types, type);
	}

	public static BiPredicate<String[], String> getPredicate() {
		return predicate;
	}

	public static void setPredicate(BiPredicate<String[], String> predicate) {
		HttpTypePredicate.predicate = Objects.requireNonNull(predicate);
	}

}
