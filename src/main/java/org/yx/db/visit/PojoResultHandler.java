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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yx.bean.Loader;
import org.yx.db.DBJson;
import org.yx.db.enums.CacheType;
import org.yx.db.sql.ColumnMeta;
import org.yx.db.sql.PojoMeta;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class PojoResultHandler implements ResultHandler {

	public static final PojoResultHandler handler = new PojoResultHandler();

	public void filterSelectColumns(PojoMeta pm, Object obj, List<String> selectColumns) throws Exception {
		if (selectColumns.isEmpty()) {
			return;
		}
		for (ColumnMeta cm : pm.fieldMetas()) {
			if (!selectColumns.contains(cm.getFieldName())) {
				cm.getField().set(obj, null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parseFromJson(PojoMeta pm, List<String> jsons, List<String> selectColumns) throws Exception {
		if (CollectionUtil.isEmpty(jsons)) {
			return null;
		}
		List<Object> list = new ArrayList<>(jsons.size());
		for (String json : jsons) {
			if (StringUtil.isEmpty(json)) {
				continue;
			}

			if (pm.cacheType() == CacheType.LIST || (json.startsWith("[") && json.endsWith("]"))) {
				Object[] ts = DBJson.operator().fromJson(json, pm.pojoArrayClz());
				if (ts == null || ts.length == 0) {
					continue;
				}
				for (Object t : ts) {
					this.filterSelectColumns(pm, t, selectColumns);
					list.add(t);
				}
				continue;
			}

			Object obj = DBJson.operator().fromJson(json, pm.pojoClz());
			if (obj == null) {
				continue;
			}
			this.filterSelectColumns(pm, obj, selectColumns);
			list.add(obj);
		}
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parse(PojoMeta pm, List<Map<ColumnMeta, Object>> list) throws Exception {
		List<Object> ret = new ArrayList<>();
		for (Map<ColumnMeta, Object> map : list) {
			if (map.isEmpty()) {
				continue;
			}
			ret.add(buildPojo(pm, map));
		}
		return (List<T>) ret;
	}

	private Object buildPojo(PojoMeta pm, Map<ColumnMeta, Object> map) throws Exception {
		Object obj = Loader.newInstance(pm.pojoClz());
		for (Entry<ColumnMeta, Object> en : map.entrySet()) {
			if (en.getValue() == null) {
				continue;
			}
			en.getKey().setValue(obj, en.getValue());
		}
		return obj;
	}
}
