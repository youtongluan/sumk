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

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;

import org.yx.bean.Loader;
import org.yx.conf.AppInfo;

public final class FileUtil {
	/**
	 * 列出该目录下的所有子文件（不包含目录）
	 * 
	 * @param filelist
	 *            目标对象
	 * @param parent
	 *            目录
	 */
	public static void listAllSubFiles(Collection<File> filelist, File parent) {
		File[] files = parent.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					listAllSubFiles(filelist, f);
				} else {
					filelist.add(f);
				}
			}

		}
	}

	public static File file(String path) throws URISyntaxException {
		if (path.startsWith(AppInfo.CLASSPATH_URL_PREFIX)) {
			return new File(Loader.loader().getResource(path.substring(AppInfo.CLASSPATH_URL_PREFIX.length())).toURI());
		}
		return new File(path);
	}
}
