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
package org.yx.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.function.Consumer;

public class LocalDBResourceLoader implements SingleResourceLoader {

	private InputStream fileInClassPath(String uri) throws URISyntaxException, FileNotFoundException {
		return this.getClass().getClassLoader().getResourceAsStream(uri);
	}

	public InputStream openInput(String dbName) throws Exception {
		String uri = AppInfo.get("sumk.db.config.path", AppInfo.CLASSPATH_URL_PREFIX + "db/#.ini");
		uri = uri.trim().replace("#", dbName);
		if (uri.startsWith(AppInfo.CLASSPATH_ALL_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_ALL_URL_PREFIX.length()));
		}
		if (uri.startsWith(AppInfo.CLASSPATH_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_URL_PREFIX.length()));
		}
		return new FileInputStream(new File(uri));
	}

	@Override
	public boolean startListen(Consumer<MultiResourceLoader> consumer) {
		return false;
	}

}
