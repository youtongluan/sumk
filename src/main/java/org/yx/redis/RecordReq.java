/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.yx.db.sql.PojoMeta;
import org.yx.exception.SumkException;
import org.yx.util.Assert;

public class RecordReq {

	public static String get(PojoMeta m, String id) {
		if (m.getCounter().isCacheRefresh()) {
			return null;
		}
		String s = _get(m, id);
		if (s != null) {
			m.getCounter().incCached();
		}
		return s;
	}

	protected static String getKey(PojoMeta m, String id) {
		return m.getPre() + id;
	}

	private static String _get(PojoMeta m, String id) {
		Redis redis = RedisPool.get(m.getTableName());
		return redis.get(getKey(m, id));
	}

	public static void set(PojoMeta m, String id, String json) {
		if (json == null) {
			return;
		}
		Assert.notEmpty(id, "key of redis value cannot be null");
		String key = getKey(m, id);
		RedisPool.get(m.getTableName()).setex(key, m.getTtlSec(), json);
	}

	public static void del(PojoMeta m, String id) {
		String key = getKey(m, id);
		RedisPool.get(m.getTableName()).del(key);
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
		RedisPool.get(m.getTableName()).del(getKeys(m, ids));
	}

	public static List<String> getMultiValue(PojoMeta m, Collection<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<String>();
		}
		String[] keys = getKeys(m, ids.toArray(new String[ids.size()]));
		return RedisPool.get(m.getTableName()).mget(keys);
	}

	/**
	 * 批量获取id对应的值.值的顺序和ids的顺序一致 注意：这个方法仅适用于不需要分库的表
	 * 
	 * @param ids
	 * @return
	 */
	public static List<String> getMultiValue(PojoMeta m, String[] ids) {
		if (ids == null || ids.length == 0) {
			return new ArrayList<String>();
		}
		String[] keys = getKeys(m, ids);
		return RedisPool.get(m.getTableName()).mget(keys);
	}

	public static void setMultiValue(PojoMeta m, String[] ids, final String[] values) {
		if (ids == null || ids.length == 0) {
			return;
		}
		if (ids.length != values.length) {
			SumkException.throwException(23432, "the length of ids is not equal to values");
		}
		String[] keys = getKeys(m, ids);
		Redis redis = RedisPool.get(m.getTableName());
		for (int i = 0; i < keys.length; i++) {
			redis.setex(keys[i], m.getTtlSec(), values[i]);
		}
	}

}
