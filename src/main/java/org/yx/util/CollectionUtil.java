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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.conf.AppInfo;

/**
 * 本类的许多方法都会对key、value做trim()处理
 */
public class CollectionUtil {

	public static Map<String, String> loadMap(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
		return loadMap(reader);
	}

	public static Map<String, String> loadMapFromText(String text, String bigDelimiter, String smallDelimiter) {
		Map<String, String> map = new HashMap<>();
		for (String entry : text.split(bigDelimiter)) {
			entry = entry.trim();
			String[] vs = entry.split(smallDelimiter, 2);
			switch (vs.length) {
			case 1:
				map.put(vs[0].trim(), null);
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

	public static String saveMapToText(Map<String, ?> map, String bigDelimiter, String smallDelimiter) {
		StringBuilder sb = new StringBuilder();
		map.forEach((k, v) -> {
			sb.append(k).append(smallDelimiter).append(v).append(bigDelimiter);
		});
		return sb.toString();
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
			return Collections.emptyList();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
		List<String> list = new ArrayList<>();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#") || line.isEmpty()) {
					continue;
				}
				list.add(line);
			}
			return list;
		} finally {
			reader.close();
		}

	}

	public static List<String> splitToList(String text, String delimiter) {
		if (text == null || text.isEmpty()) {
			return Collections.emptyList();
		}
		String[] arrray = text.split(delimiter);
		List<String> list = new ArrayList<>(arrray.length);
		for (String data : list) {
			data = data.trim();
			if (data == null || data.isEmpty()) {
				continue;
			}
			list.add(data);
		}
		return list;

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

	@SuppressWarnings("unchecked")
	public static Map<String, Object> flatMapToTree(Map<String, String> map) {
		Map<String, Object> ret = new HashMap<>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if (!k.contains(".")) {
				ret.put(k, v);
				continue;
			}
			String[] ks = k.split("\\.");
			int lastIndex = ks.length - 1;
			Map<String, Object> temp = ret;
			for (int i = 0; i < lastIndex; i++) {
				String k0 = ks[i];
				Object obj = temp.get(k0);
				if (obj == null) {
					Map<String, Object> temp2 = new HashMap<>();
					temp.put(k0, temp2);
					temp = temp2;
					continue;
				}
				temp = (Map<String, Object>) obj;
				continue;
			}
			temp.put(ks[lastIndex], v);
		}

		return ret;
	}
}
