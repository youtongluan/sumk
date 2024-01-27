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

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.yx.base.scaner.JarFileUtil;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.FileUtil;
import org.yx.util.Loader;

public class ClassPathXmlFilesLoader extends AbstractFilesLoader {

	public ClassPathXmlFilesLoader(String rootUri, String subfix) {
		super(rootUri, subfix);
	}

	@Override
	public Map<String, byte[]> openResources(String db) throws Exception {
		Enumeration<URL> urls = Loader.getResources(rootUri);
		if (urls == null || !urls.hasMoreElements()) {
			if (AppInfo.getBoolean("sumk.conf.classpath.force", false)) {
				throw new SumkException(356453425, "can not find path " + rootUri);
			}
			return Collections.emptyMap();
		}
		final Map<String, byte[]> map = new HashMap<>();
		Collection<File> files = new HashSet<>();
		Logger log = Log.get("sumk.conf");
		do {
			URL url = urls.nextElement();
			if (JarFileUtil.URL_PROTOCOL_JAR.equals(url.getProtocol())
					|| JarFileUtil.URL_PROTOCOL_ZIP.equals(url.getProtocol())) {
				Map<String, byte[]> tempMap = subfix == null ? JarFileUtil.exactResourcesInJar(url, null)
						: JarFileUtil.exactResourcesInJar(url, node -> node.endsWith(subfix));
				if (log.isDebugEnabled()) {
					log.debug("url:{},size:{}", url, tempMap == null ? 0 : tempMap.size());
				}
				if (tempMap != null && tempMap.size() > 0) {
					map.putAll(tempMap);
				}
				continue;
			}
			File f = new File(url.toURI());
			if (f.exists() && f.isDirectory()) {
				FileUtil.listAllSubFiles(files, f);
			}
		} while (urls.hasMoreElements());

		if (files.size() > 0) {
			List<FileModifyTime> timeList = new ArrayList<>(files.size());
			for (File f : files) {
				String path = f.getAbsolutePath();
				if (subfix != null && !path.endsWith(subfix)) {
					continue;
				}
				map.put(path, Files.readAllBytes(f.toPath()));
				timeList.add(new FileModifyTime(path, f.lastModified()));
			}
			this.times = timeList.toArray(new FileModifyTime[timeList.size()]);
		} else {
			this.times = null;
		}

		if (log.isDebugEnabled()) {
			FileModifyTime[] ts = this.times;
			Map<String, Long> timeMap = new HashMap<>();
			if (ts != null && ts.length > 0) {
				for (FileModifyTime t : ts) {
					timeMap.put(t.file, t.lastModify);
				}
			}
			log.debug("rootUri:{},times size:{},map size:{}", this.rootUri, timeMap.size(), map.size());
			for (String p : map.keySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(p).append(':').append(map.get(p).length);
				Long lastModify = timeMap.get(p);
				if (lastModify != null) {
					sb.append(" - ").append(lastModify);
				}
				log.debug(sb.toString());
			}
		}
		return map;
	}

}