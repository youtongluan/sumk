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
package org.yx.db;

import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.redis.RecordRepository;
import org.yx.util.StringUtil;

public class DBUtil {

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
			String id = pm.getRedisID(src, false);
			if (StringUtil.isEmpty(id)) {
				continue;
			}
			RecordRepository.del(pm, id);
			total++;
		}
		return total;
	}
}
