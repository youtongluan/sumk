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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.util.FileUtil;

public class AbsoluteXmlFilesLoader extends AbstractFilesLoader {

	public AbsoluteXmlFilesLoader(String rootUri, String subfix) {
		super(rootUri, subfix);
	}

	@Override
	public Map<String, byte[]> openResources(String db) throws Exception {
		List<File> files = new ArrayList<>();
		File root = new File(rootUri);
		if (!root.exists() || !root.isDirectory()) {
			return Collections.emptyMap();
		}
		Map<String, byte[]> map = new HashMap<>();
		FileUtil.listAllSubFiles(files, root);
		List<FileModifyTime> timeList = new ArrayList<>(files.size());
		for (int i = 0; i < files.size(); i++) {
			File f = files.get(i);
			if (!f.getName().endsWith(".xml")) {
				continue;
			}
			map.put(f.getName(), Files.readAllBytes(f.toPath()));
			timeList.add(new FileModifyTime(f.getAbsolutePath(), f.lastModified()));
		}
		this.times = timeList.toArray(new FileModifyTime[timeList.size()]);
		return map;
	}
}