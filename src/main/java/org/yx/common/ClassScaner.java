/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.common;

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

public class ClassScaner {

	public Collection<String> parse(final String... packageNames) {
		Set<String> classNameList = new HashSet<String>(240);
		if (packageNames == null) {
			return classNameList;
		}
		String packagePath;
		ClassLoader classLoader = ClassScaner.class.getClassLoader();
		File file;
		URL url;
		String filePath;
		Enumeration<URL> eUrl;
		for (String packageName : packageNames) {
			packagePath = packageName.replace('.', '/');
			try {
				eUrl = classLoader.getResources(packagePath);
				while (eUrl.hasMoreElements()) {
					url = eUrl.nextElement();
					filePath = url.getFile();
					if (filePath.indexOf("src/test/") == -1 && filePath.indexOf("src/main/") == -1) {
						file = new File(url.getPath());
						this.parseFile(classNameList, file, packagePath, url);
					}
				}
			} catch (IOException ex) {
				Log.get("ClassScaner").error("parse " + packageName + "failed", ex);
			}
		}
		return classNameList;
	}

	private void parseFile(Collection<String> classNameList, File file, String packagePath, URL url) {
		File[] subFiles;
		if (file.isDirectory()) {
			subFiles = file.listFiles();
			if (subFiles == null) {
				return;
			}
			for (File subFile : subFiles) {
				this.parseFile(classNameList, subFile, packagePath, url);
			}
		} else if (file.getPath().contains(".class")) {
			this.findClass(classNameList, file, packagePath);
		} else if (file.getPath().contains(".jar")) {
			this.findClassInJar(classNameList, url, packagePath);
		}
	}

	private void findClassInJar(Collection<String> classNameList, URL url, String packagePath) {

		JarFile jarFile = null;
		try {

			jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> jarEntryEnum = jarFile.entries();
			JarEntry jarEntry;
			String className;
			String packageName = packagePath.replaceAll("/", ".");
			while (jarEntryEnum.hasMoreElements()) {
				jarEntry = jarEntryEnum.nextElement();
				if (jarEntry.getName().endsWith(".class")) {
					className = jarEntry.getName().replaceAll("/", ".").substring(0, jarEntry.getName().length() - 6);
					if (className.startsWith(packageName) && !classNameList.contains(className)) {
						classNameList.add(className);
					}
				}
			}
		} catch (IOException ex) {
			Log.get("ClassScaner").error("findClassInJar can not found jar or class", ex);
		} finally {
			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (IOException ex) {
					Log.get("ClassScaner").error("can not close jarFile in findClassInJar", ex);
				}
			}
		}
	}

	private void findClass(Collection<String> classNameList, File file, String packagePath) {
		String absolutePath = file.getAbsolutePath().replaceAll("\\\\", "/");
		int index = absolutePath.indexOf(packagePath);
		String className = absolutePath.substring(index);
		className = className.replace('/', '.');
		className = className.substring(0, className.length() - 6);
		String packageName = packagePath.replace('/', '.');
		if (className.startsWith(packageName) && !classNameList.contains(className)) {
			classNameList.add(className);
		}
	}
}
