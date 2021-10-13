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

import static org.yx.conf.Const.LN;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yx.annotation.doc.NotNull;
import org.yx.common.sumk.UnmodifiableArrayList;
import org.yx.common.sumk.map.UnmodifiableListMap;
import org.yx.conf.Const;

/**
 * 本类的许多方法都会对key、value做trim()处理
 */
public final class CollectionUtil {

	private static final String IGNORE_PREFIX = "#";

	public static Map<String, String> loadMapFromText(String text, String bigDelimiter, String smallDelimiter) {
		return fillMapFromText(new HashMap<String, String>(), text, bigDelimiter, smallDelimiter);
	}

	public static Map<String, String> fillMapFromText(Map<String, String> map, String text, String bigDelimiter,
			String smallDelimiter) {
		for (String entry : text.split(bigDelimiter)) {
			entry = entry.trim();
			if (StringUtil.isEmpty(entry)) {
				continue;
			}
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
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			String k = entry.getKey();
			Object v = entry.getValue();
			sb.append(k);
			if (v != null) {
				sb.append(smallDelimiter).append(v);
			}
			sb.append(bigDelimiter);
		}
		return sb.toString();
	}

	public static Map<String, String> fillConfigFromText(final Map<String, String> map, String text) {
		if (text == null || text.isEmpty()) {
			return map;
		}
		final String CONFIG_NEW_LINE2 = Const.CONFIG_NEW_LINE.replace("\t", "  ");

		text = StringUtil.formatNewLineFlag(text).replace(Const.CONFIG_NEW_LINE, "").replace(CONFIG_NEW_LINE2, "");
		Map<String, String> temp = fillMapFromText(new LinkedHashMap<String, String>(), text, Const.LN, "=");
		for (Map.Entry<String, String> entry : temp.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if (k.startsWith(IGNORE_PREFIX) || v == null || v.isEmpty()) {
				continue;
			}
			map.put(k, v);
		}
		return map;
	}

	public static List<String> loadList(InputStream in) throws IOException {
		if (in == null) {
			return Collections.emptyList();
		}
		byte[] bs = IOUtil.readAllBytes(in, true);
		if (bs == null || bs.length == 0) {
			return Collections.emptyList();
		}
		String text = new String(bs, StandardCharsets.UTF_8);
		text = StringUtil.formatNewLineFlag(text);
		return StringUtil.splitAndTrim(text, LN);
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
	public static Map<String, Object> flatMapToTree(@NotNull Map<String, String> map) {
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

	public static <T> Map<String, T> subMap(@NotNull Map<String, T> source, @NotNull String prefix) {
		int len = prefix.length();
		Map<String, T> map = new HashMap<>();
		for (Map.Entry<String, T> entry : source.entrySet()) {
			String key = entry.getKey();
			T value = entry.getValue();
			if (key.startsWith(prefix)) {
				map.put(key.substring(len), value);
			}
		}
		return map;
	}

	public static <K, V> Map<K, V> removeNull(@NotNull Map<K, V> map) {
		Map<K, V> ret = new HashMap<>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			K k = entry.getKey();
			V v = entry.getValue();
			if (k == null || v == null) {
				continue;
			}
			ret.put(k, v);
		}
		return ret;
	}

	/**
	 * 返回一个不可变的list，这个list是原来的副本，它不会保存原来col的引用
	 * 
	 * @param     <T> 类型
	 * @param col 原始集合，可以为null
	 * @return 返回值不可修改，且不为null
	 */
	public static <T> List<T> unmodifyList(Collection<T> col) {
		if (col == null || col.isEmpty()) {
			return Collections.emptyList();
		}
		if (col instanceof List && (col instanceof UnmodifiableArrayList
				|| "java.util.Collections$SingletonList".equals(col.getClass().getName()))) {
			return (List<T>) col;
		}
		if (col.size() == 1) {
			return Collections.singletonList(col.iterator().next());
		}
		return new UnmodifiableArrayList<>(col);
	}

	/**
	 * 支持参数为null
	 * 
	 * @param     <T> 类型
	 * @param arr 原始数组，对原始数组的修改有可能会修改本集合。它可以为null
	 * @return 返回值不可修改，且不为null
	 */
	public static <T> List<T> unmodifyList(T[] arr) {
		if (arr == null || arr.length == 0) {
			return Collections.emptyList();
		}
		if (arr.length == 1) {
			return Collections.singletonList(arr[0]);
		}
		return new UnmodifiableArrayList<>(arr);
	}

	/**
	 * 生成不可变map
	 * 
	 * @param   <K> key的类型
	 * @param   <V> value的类型
	 * @param m 原始map
	 * @return 返回值不可修改，且不为null
	 */
	public static <K, V> Map<K, V> unmodifyMap(Map<K, V> m) {
		if (m == null || m.isEmpty()) {
			return Collections.emptyMap();
		}
		if (UnmodifiableListMap.class.equals(m.getClass())) {
			return m;
		}

		String clzName = m.getClass().getName();
		if ("java.util.Collections$SingletonMap".equals(clzName)
				|| "java.util.Collections$UnmodifiableMap".equals(clzName)) {
			return m;
		}
		final int size = m.size();
		if (size == 1) {
			Entry<K, V> kv = m.entrySet().iterator().next();
			return Collections.singletonMap(kv.getKey(), kv.getValue());
		}
		if (size < 16) {
			return new UnmodifiableListMap<>(m);
		}
		return Collections.unmodifiableMap(m);
	}
}
