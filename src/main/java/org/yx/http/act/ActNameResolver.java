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
package org.yx.http.act;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.yx.util.StringUtil;

public class ActNameResolver implements Function<String, String> {

	private final boolean ignoreCase;

	public ActNameResolver(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@Override
	public String apply(String name) {
		String act = this.solve(Objects.requireNonNull(name));
		if (ignoreCase) {
			return act.toLowerCase();
		}
		return act;
	}

	private String solve(String name) {
		List<String> list = StringUtil.splitAndTrim(name, "/", "\\");

		return String.join("/", list);
	}
}
