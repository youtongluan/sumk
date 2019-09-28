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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.yx.annotation.http.Web;
import org.yx.util.StringUtil;

public class WebNameResolverImpl implements WebNameResolver {

	@Override
	public List<String> solve(Class<?> clz, Method m, Web web) {
		String name = web.value();
		if (name == null || name.isEmpty()) {
			return Collections.singletonList(m.getName());
		}
		List<String> list = new ArrayList<String>(1);
		String[] names = StringUtil.toLatin(name).split(",");
		for (String n : names) {
			String realName = solve(n);
			if (realName != null) {
				list.add(realName);
			}
		}
		return list.size() > 0 ? list : Collections.singletonList(m.getName());
	}

	private String solve(String name) {
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
		return null;
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
