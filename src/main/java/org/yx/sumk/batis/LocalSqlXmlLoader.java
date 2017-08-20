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
package org.yx.sumk.batis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.conf.MultiResourceLoader;
import org.yx.log.Log;

public class LocalSqlXmlLoader implements MultiResourceLoader {

	private File fileInClassPath(String uri) throws URISyntaxException {
		URL url = Loader.getResource(uri);
		return new File(url.toURI());
	}

	public File getParent(String dbName) throws Exception {
		String uri = AppInfo.get("sumk.db.batis.path", AppInfo.CLASSPATH_URL_PREFIX + "batis/#");
		uri = uri.trim().replace("#", dbName);
		if (uri.startsWith(AppInfo.CLASSPATH_ALL_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_ALL_URL_PREFIX.length()));
		}
		if (uri.startsWith(AppInfo.CLASSPATH_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_URL_PREFIX.length()));
		}
		return new File(uri);
	}

	@Override
	public Map<String, InputStream> openInputs(String db) throws Exception {
		Map<String, InputStream> map = new HashMap<>();
		List<File> xmlFiles = new ArrayList<>();
		parseFileList(xmlFiles, getParent(db));
		for (File f : xmlFiles) {
			Log.get("sumk.db.batis").debug("mybatis file:{}", f.getAbsolutePath());
			map.put(f.getAbsolutePath(), new FileInputStream(f));
		}
		return map;
	}

	public void parseFileList(List<File> filelist, File parent) {
		File[] files = parent.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					parseFileList(filelist, f);
				} else if (f.getName().endsWith(".xml")) {
					filelist.add(f);
				}
			}

		}
	}

	@Override
	public boolean startListen(Consumer<MultiResourceLoader> consumer) {
		return false;
	}

}
