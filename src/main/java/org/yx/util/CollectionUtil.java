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
package org.yx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.conf.AppInfo;

public class CollectionUtil {

	public static Map<String, String> loadMap(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
		return loadMap(reader);
	}

	public static Map<String, String> loadMapWithDefaultKey(String text, String bigFilter, String smallFilter,
			String defaultKey) {
		Map<String, String> map = new HashMap<>();
		for (String entry : text.split(bigFilter)) {
			entry = entry.trim();
			String[] vs = entry.split(smallFilter, 2);
			switch (vs.length) {
			case 1:
				if (defaultKey != null) {
					map.put(defaultKey, vs[0]);
				}
				break;
			case 2:
				map.put(vs[0].trim(), vs[1].trim());
				break;
			default:
				continue;
			}
		}
		return map;
	}

	public static Map<String, String> loadMap(Reader in) throws IOException {
		BufferedReader reader = BufferedReader.class.isInstance(in) ? (BufferedReader) in : new BufferedReader(in);
		Map<String, String> map = new HashMap<>();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				}
				String[] vs = line.split("=", 2);
				if (vs.length != 2) {
					continue;
				}
				map.put(vs[0].trim(), vs[1].trim());
			}
		} finally {
			reader.close();
		}
		return map;

	}

	public static List<String> loadList(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
		List<String> list = new ArrayList<>();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				}
				list.add(line);
			}
			return list;
		} finally {
			reader.close();
		}

	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static boolean isEmpty(Collection<?> colletion) {
		return colletion == null || colletion.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> colletion) {
		return colletion != null && colletion.size() > 0;
	}
}
