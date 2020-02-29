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
		for (Map<String, String> map : maps.values()) {
			list.add(DBConfig.create(map));
		}
		return list;
	}

	public static Map<String, Map<String, String>> pureMap(Map<String, Object> objectMap) {
		if (objectMap == null || objectMap.isEmpty()) {
			return Collections.emptyMap();
		}
		final Map<String, Map<String, String>> ret = new HashMap<String, Map<String, String>>();

		objectMap.forEach((catagory, value) -> {
			if (!Map.class.isInstance(value)) {
				Logs.db().info("{} is not valid config, value : {}", catagory, value);
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> config = (Map<String, Object>) value;
			Map<String, String> real = new HashMap<>();
			config.forEach((propertyName, v) -> {
				if (!String.class.isInstance(v)) {
					Logs.db().info("{}.{} is not valid config,value:{}", catagory, propertyName, v);
					return;
				}
				real.put(propertyName, (String) v);
			});
			if (real.size() > 0) {
				ret.put(catagory, real);
			}
		});
		return ret;
	}

}
