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
package org.yx.base.scaner;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yx.util.IOUtil;

public class JarFileUtil {
	public static final String URL_PROTOCOL_JAR = "jar";

	public static final String URL_PROTOCOL_ZIP = "zip";

	public static final String JAR_URL_SEPARATOR = "!/";

	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol);
	}

	public static Map<String, byte[]> exactResourcesInJar(URL url, Predicate<String> tester) throws IOException {
		Map<String, byte[]> map = new HashMap<>();
		JarFile jarFile = null;
		try {
			String prefix = "";
			String urlPath = url.getPath();
			int separatorIndex = urlPath.lastIndexOf(JAR_URL_SEPARATOR);
			if (separatorIndex != -1 && separatorIndex + JAR_URL_SEPARATOR.length() < urlPath.length()) {
				prefix = urlPath.substring(separatorIndex + JAR_URL_SEPARATOR.length());
				if (!prefix.endsWith("/")) {
					prefix += "/";
				}
			}

			jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> jarEntryEnum = jarFile.entries();
			while (jarEntryEnum.hasMoreElements()) {
				JarEntry entry = jarEntryEnum.nextElement();
				String entityName = entry.getName();
				if (entry.isDirectory() || !entityName.startsWith(prefix)) {
					continue;
				}
				if (tester != null && !tester.test(entityName)) {
					continue;
				}
				byte[] bs = IOUtil.readAllBytes(jarFile.getInputStream(entry), true);
				map.put(entityName, bs);
			}
		} finally {
			if (jarFile != null) {
				jarFile.close();
			}
		}
		return map;
	}

}
