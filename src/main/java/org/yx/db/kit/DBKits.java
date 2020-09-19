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
package org.yx.db.kit;

import java.util.List;

import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.exception.SumkException;
import org.yx.exception.SumkExceptionCode;
import org.yx.redis.RecordRepository;
import org.yx.util.StringUtil;

public final class DBKits {

	public static <T> T queryOne(List<T> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			throw new SumkException(SumkExceptionCode.DB_TOO_MANY_RESULTS,
					"result size:" + list.size() + " more than one");
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Number> T toType(Number v, Class<?> type, boolean failIfNotSupport) {
		if (v.getClass() == type) {
			return (T) v;
		}
		if (Byte.class == type || byte.class == type) {
			return (T) Byte.valueOf(v.byteValue());
		}

		if (Short.class == type || short.class == type) {
			return (T) Short.valueOf(v.shortValue());
		}

		if (Integer.class == type || int.class == type) {
			return (T) Integer.valueOf(v.intValue());
		}

		if (Long.class == type || long.class == type) {
			return (T) Long.valueOf(v.longValue());
		}

		if (Float.class == type || float.class == type) {
			return (T) Float.valueOf(v.floatValue());
		}

		if (Double.class == type || double.class == type) {
			return (T) Double.valueOf(v.doubleValue());
		}
		if (failIfNotSupport || !Number.class.isAssignableFrom(type)) {
			throw new SumkException(927816546, type.getClass().getName() + "is not valid Number type");
		}
		return (T) v;
	}

	@SafeVarargs
	public static <T> int clearCache(T... pojos) throws Exception {
		int total = 0;
		if (pojos == null || pojos.length == 0) {
			return total;
		}
		T t = null;
		for (int i = 0; i < pojos.length; i++) {
			t = pojos[i];
			if (t != null) {
				break;
			}
		}
		if (t == null) {
			return total;
		}
		PojoMeta pm = PojoMetaHolder.getPojoMeta(t.getClass(), null);
		if (pm == null || pm.isNoCache()) {
			return total;
		}
		for (T src : pojos) {
			if (src == null) {
				continue;
			}
			String id = pm.getCacheID(src, false);
			if (StringUtil.isEmpty(id)) {
				continue;
			}
			RecordRepository.del(pm, id);
			total++;
		}
		return total;
	}
}
