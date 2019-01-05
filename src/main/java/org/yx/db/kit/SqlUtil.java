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
package org.yx.db.kit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.yx.conf.AppInfo;

public class SqlUtil {

	public static Map<String, String> loadMap(byte[] in) throws IOException {
		if (in == null) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new StringReader(new String(in, AppInfo.systemCharset())));
		return loadMap(reader);
	}

	public static Map<String, String> loadMap(Reader in) throws IOException {
		BufferedReader reader = BufferedReader.class.isInstance(in) ? (BufferedReader) in : new BufferedReader(in);
		Map<String, String> map = new HashMap<>();
		try {
			StringBuilder value = new StringBuilder();
			String line = null;
			String name = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("--") || line.startsWith("\\\\") || line.startsWith("//")) {
					continue;
				}

				if (line.startsWith("[") && line.endsWith("]")) {
					if (name != null && value.length() > 0) {
						map.put(name, value.toString());
					}
					name = line.substring(1, line.length() - 1).trim();
					value.setLength(0);
					continue;
				}

				if (value.length() > 0) {
					value.append(' ');
				}
				value.append(line);
			}
			if (name != null && value.length() > 0) {
				map.put(name, value.toString());
			}
		} finally {
			reader.close();
		}
		return map;

	}
}
