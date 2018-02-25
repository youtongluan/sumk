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
package org.yx.db.visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yx.db.annotation.CacheType;
import org.yx.db.sql.PojoMeta;
import org.yx.util.CollectionUtil;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtil;

public class MapResultHandler implements ResultHandler {

	public static MapResultHandler handler = new MapResultHandler();

	private MapResultHandler() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parseFromJson(PojoMeta pm, List<String> jsons)
			throws InstantiationException, IllegalAccessException {
		if (CollectionUtil.isEmpty(jsons)) {
			return null;
		}
		List<Map<String, Object>> list = new ArrayList<>();
		for (String json : jsons) {
			if (StringUtil.isEmpty(json)) {
				continue;
			}

			if (pm.cacheType() == CacheType.LIST || (json.startsWith("[") && json.endsWith("]"))) {
				Object[] ts = GsonUtil.fromJson(json, pm.pojoArrayClz());
				if (ts == null || ts.length == 0) {
					continue;
				}
				for (Object obj : ts) {
					Map<String, Object> map = pm.populate(obj, false);
					if (map.size() > 0) {
						list.add(map);
					}
				}
				continue;
			}
			Object obj = GsonUtil.fromJson(json, pm.pojoClz);
			if (obj == null) {
				continue;
			}
			Map<String, Object> map = pm.populate(obj, false);
			if (map.size() > 0) {
				list.add(map);
			}
		}
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parse(PojoMeta pm, List<Map<String, Object>> list) {
		return (List<T>) list;
	}

}
