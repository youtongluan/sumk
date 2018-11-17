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
import java.util.HashMap;
import java.util.Map;

import org.yx.bean.Loader;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;

public final class DBConfigUtils {

	public static InputStream openConfig(String db) throws Exception {
		String resourceFactory = AppInfo.get("sumk.db.conf.loader." + db, LocalDBResourceLoader.class.getName());
		Class<?> factoryClz = Loader.loadClass(resourceFactory);
		Assert.isTrue(SingleResourceLoader.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from " + SingleResourceLoader.class.getSimpleName());
		SingleResourceLoader factory = (SingleResourceLoader) factoryClz.newInstance();
		return factory.openInput(db);
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
