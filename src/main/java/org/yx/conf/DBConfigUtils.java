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
package org.yx.conf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.yx.bean.Loader;
import org.yx.exception.SumkException;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;
import org.yx.util.Assert;

public final class DBConfigUtils {

	public static Map<String, Map<String, String>> pureMap(Map<String, Object> objectMap) {
		if (objectMap == null || objectMap.isEmpty()) {
			return Collections.emptyMap();
		}
		final Map<String, Map<String, String>> ret = new HashMap<String, Map<String, String>>();

		objectMap.forEach((catagory, value) -> {
			if (!Map.class.isInstance(value)) {
				ConsoleLog.get("sumk.db.config").info("{} is not valid config, value : {}", catagory, value);
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> config = (Map<String, Object>) value;
			Map<String, String> real = new HashMap<>();
			config.forEach((propertyName, v) -> {
				if (!String.class.isInstance(v)) {
					ConsoleLog.get("sumk.db.config").info("{}.{} is not valid config,value:{}", catagory, propertyName,
							v);
					return;
				}
				real.put(propertyName, (String) v);
			});
			if (real.size() > 0) {
				ret.put(catagory, real);
			}
		});
		return ret;
	}

	public static Map<String, DBConfig> parseConfigMap(Map<String, Map<String, String>> rawMap) throws Exception {
		if (rawMap == null || rawMap.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, DBConfig> map = new HashMap<>();
		for (String key : rawMap.keySet()) {
			Map<String, String> p = rawMap.get(key);
			DBConfig dc = new DBConfig();
			dc.setProperties(p);
			map.put(key, dc);
		}
		return map;
	}

	public static byte[] openConfig(String db) throws Exception {
		String resourceFactory = AppInfo.get("sumk.db.conf.loader." + db, LocalDBResourceLoader.class.getName());
		Class<?> factoryClz = Loader.loadClass(resourceFactory);
		Assert.isTrue(SingleResourceLoader.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from " + SingleResourceLoader.class.getSimpleName());
		SingleResourceLoader factory = (SingleResourceLoader) factoryClz.newInstance();
		return factory.readResource(db);
	}

	public static Map<String, Map<String, String>> parseIni(String filename) throws FileNotFoundException {
		return parseIni(new FileInputStream(filename));
	}

	public static Map<String, Map<String, String>> parseIni(InputStream in) {
		return new IniFile(in).sections;
	}

	private static class IniFile {
		protected Map<String, Map<String, String>> sections = new HashMap<>();
		private String currentSecion;
		private Map<String, String> current;

		IniFile(InputStream in) {
			if (in == null) {
				SumkException.throwException(245323425, "ini stream cannot be null");
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
				read(reader);
			} catch (Exception e) {
				Log.printStack("sumk.db.conf", e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
					}
				}

			}
		}

		protected void read(BufferedReader reader) throws IOException {
			String line;
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		}

		protected void parseLine(String line) {
			line = line.trim();
			if (line.matches("^\\[.*\\]")) {
				currentSecion = line.replaceFirst("\\[(.*)\\]", "$1");
				current = new HashMap<>();
				sections.put(currentSecion, current);
			} else if (line.matches(".*=.*")) {
				if (current != null && !line.startsWith("#")) {
					int i = line.indexOf('=');
					String name = line.substring(0, i).trim();
					String value = line.substring(i + 1).trim();
					if (value.isEmpty()) {
						return;
					}
					current.put(name, value);
				}
			}
		}

		@Override
		public String toString() {
			return this.sections.toString();
		}
	}

}
