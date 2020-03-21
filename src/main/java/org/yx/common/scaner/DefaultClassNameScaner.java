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
package org.yx.common.scaner;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

public final class DefaultClassNameScaner implements Function<Collection<String>, Collection<String>> {
	private static final String DOT_CLASS = ".class";

	@Override
	public Collection<String> apply(Collection<String> packageNames) {
		Set<String> classNameList = new HashSet<>(240);
		if (packageNames == null || packageNames.isEmpty()) {
			return classNameList;
		}
		String packagePath;
		ClassLoader classLoader = DefaultClassNameScaner.class.getClassLoader();
		File file;
		URL url;
		Enumeration<URL> eUrl;
		for (String packageName : packageNames) {
			packagePath = StringUtil.toLatin(packageName).trim().replace('.', '/');
			if (packagePath.isEmpty()) {
				continue;
			}
			if (!packagePath.endsWith("/")) {
				packagePath += "/";
			}
			try {
				eUrl = classLoader.getResources(packagePath);
				while (eUrl.hasMoreElements()) {
					url = eUrl.nextElement();
					if (JarFileUtil.URL_PROTOCOL_JAR.equals(url.getProtocol())
							|| JarFileUtil.URL_PROTOCOL_ZIP.equals(url.getProtocol())) {
						this.findClassInJar(classNameList, url, packagePath);
						continue;
					}
					file = new File(url.toURI());
					if (!file.exists()) {
						SumkException.throwException(23423, file.getAbsolutePath() + " is not a file");
					}
					this.parseFile(classNameList, file, packagePath);
				}
			} catch (Exception ex) {
				Logs.system().error("parse " + packageName + "failed", ex);
				SumkException.throwException(23423, ex.getMessage(), ex);
			}
		}
		return classNameList;
	}

	private void parseFile(Collection<String> classNameList, final File root, final String packagePath) {
		File[] subFiles = root.listFiles();
		if (subFiles == null || subFiles.length == 0) {
			return;
		}
		for (File file : subFiles) {
			if (file.isDirectory()) {
				this.parseFile(classNameList, file, packagePath);
			} else if (file.getName().endsWith(DOT_CLASS)) {
				String absolutePath = file.getAbsolutePath().replace('\\', '/');
				int index = absolutePath.indexOf('/' + packagePath) + 1;
				if (index < 1) {
					Logs.system().error(absolutePath + " donot contain " + packagePath);
					continue;
				}
				addClz(classNameList, absolutePath.substring(index));
			}
		}
	}

	private void addClz(Collection<String> classNameList, String classPath) {
		if (!classPath.endsWith(DOT_CLASS) || classPath.contains("$")) {
			return;
		}
		String className = classPath.substring(0, classPath.length() - 6).replace('/', '.');
		if (!classNameList.contains(className)) {
			classNameList.add(className);
		}
	}

	private void findClassInJar(Collection<String> classNameList, URL url, String packagePath) throws IOException {
		JarFile jarFile = null;
		try {

			URLConnection conn = url.openConnection();
			if (!JarURLConnection.class.isInstance(conn)) {
				Logs.system().error("the connection of {} is {}", url.getPath(), conn.getClass().getName());
				SumkException.throwException(25345643, conn.getClass().getName() + " is not JarURLConnection");
			}
			jarFile = ((JarURLConnection) conn).getJarFile();
			Enumeration<JarEntry> jarEntryEnum = jarFile.entries();
			while (jarEntryEnum.hasMoreElements()) {
				String entityName = jarEntryEnum.nextElement().getName();
				if (entityName.startsWith("/")) {
					entityName = entityName.substring(1);
				}
				if (entityName.startsWith(packagePath)) {
					addClz(classNameList, entityName);
				}
			}
		} finally {
			if (jarFile != null) {
				jarFile.close();
			}
		}
	}

}
