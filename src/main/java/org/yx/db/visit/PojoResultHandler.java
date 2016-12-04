package org.yx.db.visit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.yx.db.annotation.CacheType;
import org.yx.db.sql.PojoMeta;
import org.yx.util.CollectionUtils;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;

/**
 * 返回值是List<Pojo>
 */
public class PojoResultHandler implements ResultHandler {

	public static PojoResultHandler handler = new PojoResultHandler();

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> parseFromJson(PojoMeta pm, List<String> jsons)
			throws InstantiationException, IllegalAccessException {
		if (CollectionUtils.isEmpty(jsons)) {
			return null;
		}
		List<Object> list = new ArrayList<>();
		for (String json : jsons) {
			if (StringUtils.isEmpty(json)) {
				continue;
			}

			if (pm.cacheType() == CacheType.LIST || (json.startsWith("[") && json.endsWith("]"))) {
				Object[] ts = GsonUtil.fromJson(json, pm.pojoArrayClz());
				if (ts == null || ts.length == 0) {
					continue;
				}
				list.addAll(Arrays.asList(ts));
				continue;
			}

			Object obj = GsonUtil.fromJson(json, pm.pojoClz);
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
