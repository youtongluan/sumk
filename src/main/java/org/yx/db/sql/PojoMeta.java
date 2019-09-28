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
package org.yx.db.sql;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.yx.annotation.db.CacheType;
import org.yx.annotation.db.SoftDelete;
import org.yx.annotation.db.Table;
import org.yx.bean.IOC;
import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.redis.Counter;
import org.yx.redis.RedisPool;
import org.yx.util.StringUtil;

public class PojoMeta implements Cloneable {

	public static final String WILDCHAR = "?";
	private final static char KEY_SPLIT = ',';
	final Table table;

	final ColumnMeta[] fieldMetas;
	/**
	 * 数据库表所对应的java pojo类
	 */
	public final Class<?> pojoClz;

	private ColumnMeta[] redisIDs;

	private ColumnMeta[] primaryIDs;

	private Counter counter;
	private int ttlSec;

	private String pre;

	private long lastHitTime;
	private String tableName;
	final SoftDeleteMeta softDelete;

	private Map<String, ColumnMeta> columnDBNameMap = new HashMap<>();
	private Map<String, ColumnMeta> filedNameMap = new HashMap<>();

	private Type pojoArrayClz;

	public Type pojoArrayClz() {
		return this.pojoArrayClz;
	}

	public ColumnMeta getByColumnDBName(String columnDBName) {
		if (columnDBName == null || columnDBName.isEmpty()) {
			return null;
		}
		return this.columnDBNameMap.get(columnDBName.toLowerCase());
	}

	public ColumnMeta getByFieldName(String fieldName) {
		if (fieldName == null || fieldName.isEmpty()) {
			return null;
		}
		return this.filedNameMap.get(fieldName.toLowerCase());
	}

	public boolean isNoCache() {
		return table.cacheType() == CacheType.NOCACHE || RedisPool.defaultRedis() == null || this.redisIDs.length == 0;
	}

	public CacheType cacheType() {
		return table.cacheType();
	}

	public boolean isPrimeKeySameWithReids() {
		return primaryIDs == redisIDs;
	}

	public ColumnMeta[] getPrimaryIDs() {
		return primaryIDs;
	}

	public boolean isSoftDelete() {
		return this.softDelete != null;
	}

	public long getLastHitTime() {
		return lastHitTime;
	}

	public void setLastHitTime(long lastHitTime) {
		this.lastHitTime = lastHitTime;
	}

	public Counter getCounter() {
		return counter;
	}

	public int getTtlSec() {
		return ttlSec;
	}

	PojoMeta(Table table, ColumnMeta[] fieldMetas, Class<?> pojoClz) {
		super();
		this.table = table;
		this.fieldMetas = fieldMetas;
		this.pojoClz = pojoClz;
		List<ColumnMeta> rids = new ArrayList<>(4);
		List<ColumnMeta> pids = new ArrayList<>(4);
		for (ColumnMeta m : this.fieldMetas) {
			columnDBNameMap.put(m.dbColumn.toLowerCase(), m);
			filedNameMap.put(m.getFieldName().toLowerCase(), m);
			if (m.isRedisID()) {
				rids.add(m);
			}
			if (m.isDBID()) {
				pids.add(m);
			}
			this.redisIDs = rids.toArray(new ColumnMeta[rids.size()]);
			if (pids.equals(rids)) {
				this.primaryIDs = this.redisIDs;
			} else {
				this.primaryIDs = pids.toArray(new ColumnMeta[pids.size()]);
			}
		}
		this.softDelete = IOC.get(SoftDeleteParser.class).parse(this.pojoClz.getAnnotation(SoftDelete.class));
		parseTable();
		this.pojoArrayClz = Array.newInstance(this.pojoClz, 0).getClass();
	}

	private void parseTable() {
		int ttl = table.duration();
		if (ttl > 0) {
			this.ttlSec = ttl;
		} else if (ttl == 0) {
			this.ttlSec = AppInfo.getInt("sumk.cache.ttl", 3600 * 6);
		} else {
			this.ttlSec = -1;
		}
		int beats = table.maxBeats();
		if (beats == 0) {
			this.counter = new Counter(AppInfo.getInt("sumk.cache.count", 500));
		} else {
			this.counter = new Counter(Integer.MAX_VALUE);
		}

		String _pre = table.preInCache();

		this.pre = StringUtil.isEmpty(_pre) ? "{" + this.pojoClz.getSimpleName() + "}" : _pre;
		this.tableName = StringUtil.isEmpty(table.value())
				? DBNameResolvers.getResolver().resolveTableName(this.pojoClz.getSimpleName()) : table.value();
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getPre() {
		return this.pre;
	}

	public boolean isOnlyRedisID(Object condition) throws IllegalArgumentException, IllegalAccessException {
		if (this.pojoClz.isInstance(condition)) {
			for (ColumnMeta m : this.fieldMetas) {
				Object v = m.value(condition);
				if (m.isRedisID() == (v == null)) {
					return false;
				}
			}
			return true;
		}
		if (Map.class.isInstance(condition)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) condition;
			if (map.size() != this.redisIDs.length) {
				return false;
			}
			Set<Map.Entry<String, Object>> set = map.entrySet();
			for (Map.Entry<String, Object> entry : set) {
				if (entry.getValue() == null) {
					continue;
				}
				String key = entry.getKey();
				ColumnMeta cm = this.getByFieldName(key);
				if (cm == null || !cm.isRedisID()) {
					return false;
				}
			}
			return true;
		}
		return false;

	}

	public ColumnMeta[] getRedisIDs() {
		return this.redisIDs;
	}

	public Object buildFromDBColumn(Map<String, Object> map) throws Exception {
		if (map == null) {
			return null;
		}
		Object ret = Loader.newInstance(this.pojoClz);
		Set<Entry<String, Object>> set = map.entrySet();
		for (Entry<String, Object> en : set) {
			String key = en.getKey();
			ColumnMeta m = this.getByColumnDBName(key);
			m.setValue(ret, en.getValue());
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> populate(Object source, boolean withnull)
			throws InstantiationException, IllegalAccessException {
		if (Map.class.isInstance(source)) {
			return (Map<String, Object>) source;
		}
		Map<String, Object> map = new HashMap<>();
		if (!this.pojoClz.isInstance(source)) {
			SumkException.throwException(548092345,
					source.getClass().getName() + " is not instance of " + this.pojoClz.getName());
		}
		for (ColumnMeta m : this.fieldMetas) {
			Object v = m.value(source);
			if (!withnull && v == null) {
				continue;
			}
			String name = m.getFieldName();
			map.put(name, v);
		}
		return map;
	}

	public Object buildPojo(Map<String, Object> map) throws Exception {
		Object obj = Loader.newInstance(this.pojoClz);
		for (ColumnMeta m : this.fieldMetas) {
			Object v = map.get(m.getFieldName());
			if (v == null) {
				continue;
			}
			m.setValue(obj, v);
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> populateByDbColumn(Object source, boolean withnull)
			throws InstantiationException, IllegalAccessException {
		if (Map.class.isInstance(source)) {
			return (Map<String, Object>) source;
		}
		Map<String, Object> map = new HashMap<>();
		if (!this.pojoClz.isInstance(source)) {
			Log.get("sumk.event").debug("{} is not instance of {}", source.getClass().getName(),
					this.pojoClz.getName());
			return map;
		}
		for (ColumnMeta m : this.fieldMetas) {
			Object v = m.value(source);
			if (!withnull && v == null) {
				continue;
			}
			String name = m.dbColumn;
			map.put(name, v);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public String getRedisID(Object source, boolean exceptionIfHasNull) throws Exception {
		if (Map.class.isInstance(source)) {
			return this.getRedisIDByMap((Map<String, Object>) source, exceptionIfHasNull);
		}
		StringBuilder key = new StringBuilder();
		for (ColumnMeta m : this.redisIDs) {
			Object v = m.value(source);
			if (v == null) {
				if (exceptionIfHasNull) {
					SumkException.throwException(1232142356, "value of " + m.getFieldName() + " cannot be null");
				}
				return null;
			}
			if (key.length() > 0) {
				key.append(KEY_SPLIT);
			}
			key.append(v);
		}
		return key.toString();
	}

	public String getRedisIDWithNULL(Map<String, Object> map) throws Exception {
		StringBuilder key = new StringBuilder();
		for (ColumnMeta m : this.redisIDs) {
			Object v = map.get(m.getFieldName());
			if (key.length() > 0) {
				key.append(KEY_SPLIT);
			}
			key.append(v);
		}
		return key.toString();
	}

	private String getRedisIDByMap(Map<String, Object> map, boolean exceptionIfHasNull) {
		StringBuilder key = new StringBuilder();
		for (ColumnMeta m : this.redisIDs) {
			Object v = map.get(m.getFieldName());
			if (v == null) {
				if (exceptionIfHasNull) {
					SumkException.throwException(1232142356, "redis key [" + m.getFieldName() + "] cannot be null");
				}
				return null;
			}
			if (key.length() > 0) {
				key.append(KEY_SPLIT);
			}
			key.append(v);
		}
		return key.toString();
	}

	public PojoMeta subPojoMeta(String sub) {
		if (!this.tableName.contains(WILDCHAR)) {
			return this;
		}
		PojoMeta clone;
		try {
			clone = (PojoMeta) this.clone();
		} catch (CloneNotSupportedException e) {
			throw new SumkException(3456346, e.getMessage());
		}
		clone.tableName = subTableName(sub);
		clone.pre = this.pre.replace(WILDCHAR, sub);
		return clone;
	}

	String subTableName(String sub) {
		return this.tableName.replace(WILDCHAR, sub);
	}

}
