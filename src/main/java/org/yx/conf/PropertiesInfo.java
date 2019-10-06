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

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yx.log.Log;
import org.yx.util.CollectionUtil;

public abstract class PropertiesInfo implements FileHandler, SystemConfig {

	protected String fileName;

	public PropertiesInfo(String fileName) {
		this.fileName = fileName;
	}

	protected Map<String, String> map = new HashMap<>();

	public String get(String key) {
		return map.get(key);
	}

	@Override
	public URL[] listFile() {
		URL url = PropertiesInfo.class.getClassLoader().getResource(fileName);
		if (url == null) {
			System.err.println(fileName + " cannot found");
			return null;
		}
		return new URL[] { url };
	}

	@Override
	public void deal(InputStream in) throws Exception {
		if (in == null) {
			return;
		}
		map = CollectionUtil.loadMap(in, false);
	}

	public void start() {
		try {
			FileMonitor.inst.addHandle(this);
			FileMonitor.inst.handle(this, false);
		} catch (URISyntaxException e) {
			Log.printStack("sumk.error", e);
		}
		FileMonitor.inst.start();
	}

	@Override
	public Set<String> keys() {
		return map.keySet();
	}

	@Override
	public void stop() {
		FileMonitor.inst.remove(this);
	}

}
