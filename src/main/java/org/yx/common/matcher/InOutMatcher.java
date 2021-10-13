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
package org.yx.common.matcher;

import java.util.Objects;
import java.util.function.Predicate;

public class InOutMatcher implements Predicate<String> {

	private final Predicate<String> include;
	private final Predicate<String> exclude;

	InOutMatcher(Predicate<String> include, Predicate<String> exclude) {
		this.include = Objects.requireNonNull(include);
		this.exclude = Objects.requireNonNull(exclude);
	}

	@Override
	public boolean test(String t) {
		if (exclude.test(t)) {
			return false;
		}
		return include.test(t);
	}

	@Override
	public String toString() {
		return "[include: " + include + ", exclude: " + exclude + "]";
	}

}
