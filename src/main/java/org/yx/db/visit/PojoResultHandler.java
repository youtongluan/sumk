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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.yx.annotation.db.CacheType;
import org.yx.db.DBGson;
import org.yx.db.sql.PojoMeta;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class PojoResultHandler implements ResultHandler {

	public static final PojoResultHandler handler = new PojoResultHandler();

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parseFromJson(PojoMeta pm, List<String> jsons)
			throws InstantiationException, IllegalAccessException {
		if (CollectionUtil.isEmpty(jsons)) {
			return null;
		}
		List<Object> list = new ArrayList<>();
		for (String json : jsons) {
			if (StringUtil.isEmpty(json)) {
				continue;
			}

			if (pm.cacheType() == CacheType.LIST || (json.startsWith("[") && json.endsWith("]"))) {
				Object[] ts = DBGson.fromJson(json, pm.pojoArrayClz());
				if (ts == null || ts.length == 0) {
					continue;
				}
				list.addAll(Arrays.asList(ts));
				continue;
			}

			Object obj = DBGson.fromJson(json, pm.pojoClz);
			if (obj == null) {
				continue;
			}
			list.add(obj);
		}
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parse(PojoMeta pm, List<Map<String, Object>> list) throws Exception {
		List<Object> ret = new ArrayList<>();
		for (Map<String, Object> map : list) {
			if (map.isEmpty()) {
				continue;
			}
			ret.add(pm.buildPojo(map));
		}
		return (List<T>) ret;
	}

}
