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

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Map;

import org.yx.bean.Bean;
import org.yx.util.CollectionUtil;

@Bean
public class DBConfigFactoryImpl implements DBConfigFactory {

	@Override
	public Map<String, DBConfig> create(String dbName) throws Exception {
		Map<String, Map<String, String>> rawMap = parseFromAppInfo(dbName);
		if (rawMap == null || rawMap.isEmpty()) {
			rawMap = parseFromDBIni(dbName);
		}
		return DBConfigUtils.parseConfigMap(rawMap);
	}

	protected Map<String, Map<String, String>> parseFromAppInfo(String dbName) {
		Map<String, String> map = AppInfo.subMap("s.db." + dbName + ".");
		if (map.isEmpty()) {
			return null;
		}
		Map<String, Object> configMap = CollectionUtil.flatMapToTree(map);
		return DBConfigUtils.pureMap(configMap);
	}

	protected Map<String, Map<String, String>> parseFromDBIni(String db) throws Exception {
		byte[] bs = DBConfigUtils.openConfig(db);
		if (bs == null || bs.length == 0) {
			return Collections.emptyMap();
		}
		return DBConfigUtils.parseIni(new ByteArrayInputStream(bs));
	}

}
