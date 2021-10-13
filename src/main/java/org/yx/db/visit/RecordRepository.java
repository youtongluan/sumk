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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.yx.db.sql.PojoMeta;
import org.yx.exception.SumkException;
import org.yx.redis.RedisAccess;

public final class RecordRepository {
	private static RecordAccess access = new RedisAccess();

	public static RecordAccess access() {
		return access;
	}

	public static void setAccess(RecordAccess access) {
		RecordRepository.access = Objects.requireNonNull(access);
	}

	public static String get(PojoMeta m, String id) {
		if (!m.getCounter().willVisitCache(1)) {
			return null;
		}
		String s = access.get(m, id);
		if (s != null) {
			m.getCounter().incrCacheHit(1);
		}
		return s;
	}

	public static void set(PojoMeta m, String id, String json) {
		if (json == null) {
			return;
		}
		if (id == null || id.isEmpty()) {
			throw new SumkException(657645465, "key of redis value cannot be null");
		}
		access.set(m, id, json);
	}

	public static void del(PojoMeta m, String id) {
		access.del(m, id);
	}

	public static void delMulti(PojoMeta m, String[] ids) {
		if (ids == null || ids.length == 0) {
			return;
		}
		access.delMulti(m, ids);
	}

	public static List<String> getMultiValue(PojoMeta m, Collection<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}

		if (!m.getCounter().willVisitCache(ids.size())) {
			return Collections.emptyList();
		}

		List<String> ret = access.getMultiValue(m, ids);
		if (ret == null || ret.isEmpty()) {
			return Collections.emptyList();
		}
		if (ret != null && ret.size() > 0) {
			int c = 0;
			for (String v : ret) {
				if (v != null) {
					c++;
				}
			}
			m.getCounter().incrCacheHit(c);
		}
		return ret;
	}
}
