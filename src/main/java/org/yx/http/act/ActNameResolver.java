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

import java.util.Objects;
import java.util.function.Function;

public class ActNameResolver implements Function<String, String> {

	private final boolean ingoreCase;

	public ActNameResolver(boolean ingoreCase) {
		this.ingoreCase = ingoreCase;
	}

	@Override
	public String apply(String name) {
		String act = this.solve(Objects.requireNonNull(name));
		if (ingoreCase) {
			return act.toLowerCase();
		}
		return act;
	}

	private String solve(String name) {
		return convertFromPath(name.replace('\\', '/').replace('.', '/'));
	}

	private String convertFromPath(String path) {
		while (path.endsWith("/") && path.length() > 1) {
			path = path.substring(0, path.length() - 1);
		}
		while (path.startsWith("/") && path.length() > 1) {
			path = path.substring(1);
		}
		return path;
	}
}
