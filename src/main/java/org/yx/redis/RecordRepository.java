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
package org.yx.redis;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.yx.db.sql.PojoMeta;
import org.yx.log.Log;
import org.yx.util.Asserts;

public final class RecordRepository {

	private static Logger logger = Log.get("sumk.db.redis");

	public static String get(PojoMeta m, String id) {
		if (!m.getCounter().visit()) {
			return null;
		}
		String s = _get(m, id);
		if (s != null) {
			m.getCounter().incCacheMeet();
		}
		return s;
	}

	protected static String getKey(PojoMeta m, String id) {
		return m.getPre() + id;
	}

	private static String _get(PojoMeta m, String id) {
		return muteRedis(m.getTableName()).get(getKey(m, id));
	}

	private static Redis muteRedis(String tableName) {
		return RedisPool.get(tableName).mute();
	}

	public static void set(PojoMeta m, String id, String json) {
		if (json == null) {
			return;
		}
		Asserts.notEmpty(id, "key of redis value cannot be null");
		String key = getKey(m, id);
		String tableName = m.getTableName();
		int ttl = m.getTtlSec();
		if (ttl <= 0) {
			muteRedis(tableName).set(key, json);
			logger.trace("{} >> SET {} = {}", tableName, key, json);
			return;
		}
		muteRedis(tableName).setex(key, ttl, json);
		logger.trace("{} >> SETEX {} = {}", tableName, key, json);
	}

	public static void del(PojoMeta m, String id) {
		String key = getKey(m, id);
		String tableName = m.getTableName();
		muteRedis(tableName).del(key);
		logger.trace("{} >> DELETE {}", tableName, key);
	}

	protected static String[] getKeys(PojoMeta m, String[] ids) {
		String[] keys = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			keys[i] = getKey(m, ids[i]);
		}
		return keys;
	}

	public static void delMulti(PojoMeta m, String[] ids) {
		if (ids == null || ids.length == 0) {
			return;
		}
		String[] keys = getKeys(m, ids);
		muteRedis(m.getTableName()).del(keys);
		if (logger.isTraceEnabled()) {
			String ks = Arrays.toString(keys);
			ks = ks.substring(1, ks.length() - 1);
			logger.trace("{} >> DEL_MULTI {}", m.getTableName(), ks);
		}
	}

	public static List<String> getMultiValue(PojoMeta m, Collection<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}

		if (!m.getCounter().visit()) {
			return Collections.emptyList();
		}

		String[] keys = getKeys(m, ids.toArray(new String[ids.size()]));
		List<String> ret = muteRedis(m.getTableName()).mget(keys);
		if (ret != null && ret.size() > 0) {
			m.getCounter().incCacheMeet();
		}
		return ret;
	}
}
