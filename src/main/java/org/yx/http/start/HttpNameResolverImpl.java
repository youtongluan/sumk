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
package org.yx.http.start;

import java.lang.reflect.Method;

public class HttpNameResolverImpl implements HttpNameResolver {

	@Override
	public String solve(Class<?> clz, Method m, String name) {
		if (name != null && name.length() > 0) {
			name = name.trim();
			if (name.length() > 0) {
				name = name.replace('\\', '/');
				if (name.contains("/")) {
					return convertFromPath(name);
				}
				return name;
			}
		}
		return m.getName();
	}

	private String convertFromPath(String path) {
		while (path.endsWith("/") && path.length() > 0) {
			path = path.substring(0, path.length() - 1);
		}
		while (path.startsWith("/") && path.length() > 0) {
			path = path.substring(1);
		}
		return path.replace('/', '.');
	}
}
