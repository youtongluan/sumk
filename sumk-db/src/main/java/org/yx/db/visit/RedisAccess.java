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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.yx.db.sql.PojoMeta;
import org.yx.log.Log;
import org.yx.redis.Redis;
import org.yx.redis.RedisPool;

public final class RedisAccess implements RecordAccess {

	private static Logger logger = Log.get("sumk.redis.access");

	protected Redis muteRedis(String tableName) {
		return RedisPool.get(tableName).mute();
	}

	protected String getKey(PojoMeta m, String id) {
		return m.getPre() + id;
	}

	protected String[] getKeys(PojoMeta m, String[] ids) {
		String[] keys = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			keys[i] = getKey(m, ids[i]);
		}
		return keys;
	}

	@Override
	public String get(PojoMeta m, String id) {
		return muteRedis(m.getTableName()).get(getKey(m, id));
	}

	@Override
	public void set(PojoMeta m, String id, String json) {
		String key = getKey(m, id);
		String tableName = m.getTableName();
		int ttl = m.getTtlSec();
		if (ttl <= 0) {
			muteRedis(tableName).set(key, json);
			if (logger.isTraceEnabled()) {
				logger.trace("{} >> SET {} = {}", tableName, key, json);
			}
			return;
		}
		muteRedis(tableName).setex(key, ttl, json);
		if (logger.isTraceEnabled()) {
			logger.trace("{} >> SETEX {} = {}", tableName, key, json);
		}
	}

	@Override
	public void del(PojoMeta m, String id) {
		String key = getKey(m, id);
		String tableName = m.getTableName();
		muteRedis(tableName).del(key);
		if (logger.isTraceEnabled()) {
			logger.trace("{} >> DELETE {}", tableName, key);
		}
	}

	@Override
	public void delMulti(PojoMeta m, String[] ids) {
		String[] keys = getKeys(m, ids);
		muteRedis(m.getTableName()).del(keys);
		if (logger.isTraceEnabled()) {
			String ks = Arrays.toString(keys);
			ks = ks.substring(1, ks.length() - 1);
			logger.trace("{} >> DEL_MULTI {}", m.getTableName(), ks);
		}
	}

	@Override
	public List<String> getMultiValue(PojoMeta m, Collection<String> ids) {
		String[] keys = getKeys(m, ids.toArray(new String[ids.size()]));
		return muteRedis(m.getTableName()).mget(keys);
	}
}
