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
package org.yx.bean.watcher;

import java.io.InputStream;
import java.util.Map;

import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class IntfImplement {

	public static void beforeScan() {
		try {
			InputStream in = Loader.getResourceAsStream("META-INF/sumk-intf");
			if (in == null) {
				Logs.system().error("sumk-intf file cannot be found");
				return;
			}
			Map<String, String> map = CollectionUtil.loadMap(in, false);
			for (String key : map.keySet()) {
				if (StringUtil.isEmpty(key)) {
					continue;
				}
				String impl = AppInfo.get("sumk.intf." + key);
				if (StringUtil.isEmpty(impl)) {
					impl = map.get(key);
				}
				if (StringUtil.isEmpty(impl)) {
					continue;
				}
				Class<?> intfClz = Loader.loadClass(key);
				Class<?> implClz = Loader.loadClass(impl);
				InnerIOC.putClassByInterface(intfClz, implClz);
			}
		} catch (Exception e) {
			Log.printStack("sumk.error", e);
			System.exit(1);
		}

	}

}
