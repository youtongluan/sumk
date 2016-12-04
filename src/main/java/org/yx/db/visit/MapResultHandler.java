package org.yx.db.visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yx.db.annotation.CacheType;
import org.yx.db.sql.PojoMeta;
import org.yx.util.CollectionUtils;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;

/**
 * 返回值是List<Map<String,Object>>
 */
public class MapResultHandler implements ResultHandler {

	public static MapResultHandler handler = new MapResultHandler();

	private MapResultHandler() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parseFromJson(PojoMeta pm, List<String> jsons)
			throws InstantiationException, IllegalAccessException {
		if (CollectionUtils.isEmpty(jsons)) {
			return null;
		}
		List<Map<String, Object>> list = new ArrayList<>();
		for (String json : jsons) {
			if (StringUtils.isEmpty(json)) {
				continue;
			}

			if (pm.cacheType() == CacheType.LIST || (json.startsWith("[") && json.endsWith("]"))) {
				Object[] ts = GsonUtil.fromJson(json, pm.pojoArrayClz());
				if (ts == null || ts.length == 0) {
					continue;
				}
				for (Object obj : ts) {
					Map<String, Object> map = pm.populateByFieldName(obj, false);
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
			Map<String, Object> map = pm.populateByFieldName(obj, false);
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
