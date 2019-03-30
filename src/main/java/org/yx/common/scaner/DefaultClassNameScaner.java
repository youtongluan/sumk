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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yx.log.Log;

public final class DefaultClassNameScaner implements ClassNameScaner {
	private static final String DOT_CLASS = ".class";

	@Override
	public Collection<String> parse(final String... packageNames) {
		Set<String> classNameList = new HashSet<>(240);
		if (packageNames == null) {
			return classNameList;
		}
		String packagePath;
		ClassLoader classLoader = DefaultClassNameScaner.class.getClassLoader();
		File file;
		URL url;
		Enumeration<URL> eUrl;
		for (String packageName : packageNames) {
			packagePath = packageName.replace('.', '/');
			if (!packagePath.endsWith("/")) {
				packagePath += "/";
			}
			try {
				eUrl = classLoader.getResources(packagePath);
				while (eUrl.hasMoreElements()) {
					url = eUrl.nextElement();
					file = new File(url.getPath());
					if ("jar".equals(url.getProtocol()) || !file.exists()) {
						this.findClassInJar(classNameList, url, packagePath);
						continue;
					}
					this.parseFile(classNameList, file, packagePath);
				}
			} catch (IOException ex) {
				Log.get("sumk.scaner").error("parse " + packageName + "failed", ex);
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
					Log.get("sumk.scaner").error(absolutePath + " donot contain " + packagePath);
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
			jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
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
