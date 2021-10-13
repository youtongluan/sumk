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
package org.yx.db.conn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.annotation.Bean;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;

@Bean
public class CommonConfigFactory implements DBConfigFactory {

	protected Map<String, Map<String, String>> parseFromAppInfo(String dbName) {
		Map<String, String> map = AppInfo.subMap("s.db." + dbName + ".");
		if (map.isEmpty()) {
			return null;
		}
		Map<String, Object> configMap = CollectionUtil.flatMapToTree(map);
		return pureMap(configMap);
	}

	@Override
	public List<DBConfig> create(String name) throws Exception {
		Map<String, Map<String, String>> maps = parseFromAppInfo(name);
		List<DBConfig> list = new ArrayList<>();
		if (maps == null || maps.isEmpty()) {
			return list;
		}
		for (String index : maps.keySet()) {
			list.add(DBConfig.create(index, maps.get(index)));
		}
		return list;
	}

	public static Map<String, Map<String, String>> pureMap(Map<String, Object> objectMap) {
		if (objectMap == null || objectMap.isEmpty()) {
			return Collections.emptyMap();
		}
		final Map<String, Map<String, String>> ret = new HashMap<String, Map<String, String>>();

		for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
			String catagory = entry.getKey();
			Object value = entry.getValue();
			if (!(value instanceof Map)) {
				Logs.db().info("{} is not valid config, value : {}", catagory, value);
				continue;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> config = (Map<String, Object>) value;
			Map<String, String> real = new HashMap<>();
			for (Map.Entry<String, Object> e2 : config.entrySet()) {
				String propertyName = e2.getKey();
				Object v = e2.getValue();
				if (!(v instanceof String)) {
					Logs.db().info("{}.{} is not valid config,value:{}", catagory, propertyName, v);
					continue;
				}
				real.put(propertyName, (String) v);
			}
			if (real.size() > 0) {
				ret.put(catagory, real);
			}
		}
		return ret;
	}

}
