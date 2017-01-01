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

public class CollectionUtils {
	/**
	 * 获取的map，key、value都做了trim()处理。 跟Properties的区别是properties的文件要ascii结构，
	 * 而这个方法的文件，是UTF-8结构。如果全是英文，也可以用ASCII格式. 操作之后会关闭输入流
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> loadMap(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
		return loadMap(reader);
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
			return map;
		} finally {
			reader.close();
		}

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
