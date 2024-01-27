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
package org.yx.db.visit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yx.db.DBJson;
import org.yx.db.enums.CacheType;
import org.yx.db.sql.ColumnMeta;
import org.yx.db.sql.PojoMeta;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class MapResultHandler implements ResultHandler {

	public static final MapResultHandler handler = new MapResultHandler();

	public static Map<String, Object> filterSelectColumns(Map<String, Object> map, List<String> selectColumns) {
		if (selectColumns.isEmpty()) {
			return map;
		}
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (!selectColumns.contains(key)) {
				it.remove();
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parseFromJson(PojoMeta pm, List<String> jsons, List<String> selectColumns)
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
				Object[] ts = DBJson.operator().fromJson(json, pm.pojoArrayClz());
				if (ts == null || ts.length == 0) {
					continue;
				}
				for (Object obj : ts) {
					Map<String, Object> map = pm.populate(obj, false);
					map = filterSelectColumns(map, selectColumns);
					if (map.size() > 0) {
						list.add(map);
					}
				}
				continue;
			}
			Object obj = DBJson.operator().fromJson(json, pm.pojoClz());
			if (obj == null) {
				continue;
			}
			Map<String, Object> map = pm.populate(obj, false);
			map = filterSelectColumns(map, selectColumns);
			if (map.size() > 0) {
				list.add(map);
			}
		}
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parse(PojoMeta pm, List<Map<ColumnMeta, Object>> list) {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<Map<String, Object>> ret = new ArrayList<>(list.size());
		for (Map<ColumnMeta, Object> m0 : list) {
			Map<String, Object> map = new HashMap<>();
			for (Entry<ColumnMeta, Object> en : m0.entrySet()) {
				map.put(en.getKey().getFieldName(), en.getValue());
			}
			ret.add(map);
		}
		return (List<T>) ret;
	}

}
