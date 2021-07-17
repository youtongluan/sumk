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
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

import org.yx.annotation.db.AutoCreateTime;
import org.yx.annotation.doc.Comment;
import org.yx.annotation.doc.NotNull;
import org.yx.annotation.spec.TableSpec;
import org.yx.bean.Loader;
import org.yx.common.date.TimeUtil;
import org.yx.conf.AppInfo;
import org.yx.db.enums.CacheType;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.redis.RedisPool;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public final class PojoMeta implements Cloneable {

	public static final String WILDCHAR = "#";
	private static final char KEY_SPLIT = ':';

	final List<ColumnMeta> fieldMetas;
	/**
	 * 数据库表所对应的java pojo类
	 */
	final Class<?> pojoClz;

	@NotNull
	final List<ColumnMeta> cacheIDs;

	@NotNull
	final List<ColumnMeta> databaseIds;

	private VisitCounter counter;
	private int ttlSec;

	private String pre;

	private final CacheType cacheType;

	private String tableName;
	final SoftDeleteMeta softDelete;

	private final Map<String, ColumnMeta> columnDBNameMap = new HashMap<>();
	private final Map<String, ColumnMeta> filedNameMap = new HashMap<>();

	final List<ColumnMeta> createColumns;

	final Type pojoArrayClz;

	public Type pojoArrayClz() {
		return this.pojoArrayClz;
	}

	public List<ColumnMeta> fieldMetas() {
		return this.fieldMetas;
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
		return cacheType == CacheType.NOCACHE || RedisPool.defaultRedis() == null || this.cacheIDs.isEmpty();
	}

	public CacheType cacheType() {
		return cacheType;
	}

	public boolean isPrimeKeySameWithCache() {
		return databaseIds == cacheIDs;
	}

	public List<ColumnMeta> getDatabaseIds() {
		return databaseIds;
	}

	public boolean isSoftDelete() {
		return this.softDelete != null;
	}

	public VisitCounter getCounter() {
		return counter;
	}

	/**
	 * 重置计数器
	 * 
	 * @return 旧计数器
	 */
	public VisitCounter resetCounter() {
		VisitCounter old = this.counter;
		int maxHit = AppInfo.getInt("sumk.db.table.cache.maxHit." + this.tableName, old.getInterval());
		IntFunction<VisitCounter> factory = DBSettings.visitCounterFactory();
		VisitCounter c = factory != null ? factory.apply(maxHit) : new EstimateVisitCounter(maxHit);
		this.counter = Objects.requireNonNull(c);
		return old;
	}

	public int getTtlSec() {
		return ttlSec;
	}

	public PojoMeta(TableSpec table, ColumnMeta[] fieldMetas, Class<?> pojoClz) {
		this.cacheType = table.cacheType();
		this.fieldMetas = CollectionUtil.unmodifyList(fieldMetas);
		this.pojoClz = pojoClz;
		List<ColumnMeta> rids = new ArrayList<>();
		List<ColumnMeta> pids = new ArrayList<>();
		List<ColumnMeta> ctimes = new ArrayList<>();
		for (ColumnMeta m : this.fieldMetas) {
			columnDBNameMap.put(m.dbColumn.toLowerCase(), m);
			filedNameMap.put(m.getFieldName().toLowerCase(), m);
			if (m.isCacheID()) {
				rids.add(m);
			}
			if (m.isDBID()) {
				pids.add(m);
			}
			if (m.field.isAnnotationPresent(AutoCreateTime.class)) {
				if (TimeUtil.isGenericDate(m.field.getType()) && !timeOnly(m.field.getType())) {
					ctimes.add(m);
				} else {
					Logs.db().warn("{}.{}的类型{}不是@CreateTime支持的类型", pojoClz.getSimpleName(), m.field.getName(),
							m.field.getType());
				}
			}
		}
		this.cacheIDs = CollectionUtil.unmodifyList(rids);
		this.databaseIds = pids.equals(rids) ? this.cacheIDs : CollectionUtil.unmodifyList(pids);
		this.createColumns = CollectionUtil.unmodifyList(ctimes);
		this.softDelete = DBSettings.softDeleteParser().parse(this.pojoClz, this.fieldMetas);
		this.parseTable(table);
		this.pojoArrayClz = Array.newInstance(this.pojoClz, 0).getClass();
	}

	private void parseTable(TableSpec table) {
		int ttl = table.duration();
		if (ttl > 0) {
			this.ttlSec = ttl;
		} else if (ttl == 0) {
			this.ttlSec = AppInfo.getInt("sumk.cache.ttl", 3600);
		} else {
			this.ttlSec = -1;
		}

		this.tableName = StringUtil.isEmpty(table.value())
				? DBNameResolvers.getTableNameResolver().apply(this.pojoClz.getSimpleName())
				: table.value().replace('?', '#');
		String _pre = table.preInCache();

		if (StringUtil.isEmpty(_pre)) {
			_pre = DBNameResolvers.getCachePrefixResolver().apply(this.tableName);
		}
		this.pre = _pre.replace('?', '#');

		this.counter = new EstimateVisitCounter(table.maxHit());
		this.resetCounter();
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getPre() {
		return this.pre;
	}

	public boolean isOnlyCacheID(Object condition) throws IllegalArgumentException, IllegalAccessException {
		if (this.pojoClz.isInstance(condition)) {
			for (ColumnMeta m : this.fieldMetas) {
				Object v = m.value(condition);
				if (m.isCacheID() == (v == null)) {
					return false;
				}
			}
			return true;
		}
		if (condition instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) condition;
			if (map.size() != this.cacheIDs.size()) {
				return false;
			}
			Set<Map.Entry<String, Object>> set = map.entrySet();
			for (Map.Entry<String, Object> entry : set) {
				if (entry.getValue() == null) {
					continue;
				}
				String key = entry.getKey();
				ColumnMeta cm = this.getByFieldName(key);
				if (cm == null || !cm.isCacheID()) {
					return false;
				}
			}
			return true;
		}
		return false;

	}

	public List<ColumnMeta> getCacheIDs() {
		return this.cacheIDs;
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
			if (m == null) {
				Logs.db().warn("{}数据库字段{}找不到对应的属性", this.tableName, key);
				continue;
			}
			m.setValue(ret, en.getValue());
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> populate(Object source, boolean keepNull)
			throws InstantiationException, IllegalAccessException {
		if (source instanceof Map) {
			return (Map<String, Object>) source;
		}
		Map<String, Object> map = new HashMap<>();
		if (!this.pojoClz.isInstance(source)) {
			throw new SumkException(548092345,
					source.getClass().getName() + " is not instance of " + this.pojoClz.getName());
		}
		for (ColumnMeta m : this.fieldMetas) {
			Object v = m.value(source);
			if (!keepNull && v == null) {
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
		if (source instanceof Map) {
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

	public String getCacheID(Object source, boolean exceptionIfHasNull) throws Exception {
		return joinColumns(source, exceptionIfHasNull, this.cacheIDs);
	}

	public String getCacheIDWithNULL(Map<String, Object> map) throws Exception {
		StringBuilder key = new StringBuilder();
		for (ColumnMeta m : this.cacheIDs) {
			Object v = map.get(m.getFieldName());
			if (key.length() > 0) {
				key.append(KEY_SPLIT);
			}
			key.append(v);
		}
		return key.toString();
	}

	private String joinColumnsFromMap(Map<String, Object> map, boolean exceptionIfHasNull, List<ColumnMeta> cols) {
		StringBuilder key = new StringBuilder();
		for (ColumnMeta m : cols) {
			Object v = map.get(m.getFieldName());
			if (v == null) {
				if (exceptionIfHasNull) {
					throw new SumkException(1232142356,
							this.pojoClz.getName() + ": redis key [" + m.getFieldName() + "] cannot be null");
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

	@SuppressWarnings("unchecked")
	public String joinColumns(Object source, boolean exceptionIfHasNull, List<ColumnMeta> cols) throws Exception {
		if (source instanceof Map) {
			return this.joinColumnsFromMap((Map<String, Object>) source, exceptionIfHasNull, cols);
		}
		StringBuilder key = new StringBuilder();
		for (ColumnMeta m : cols) {
			Object v = m.value(source);
			if (v == null) {
				if (exceptionIfHasNull) {
					throw new SumkException(1232142356,
							this.pojoClz.getName() + ": value of " + m.getFieldName() + " cannot be null");
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
		clone.pre = this.pre.replace(WILDCHAR, sub.toLowerCase());
		return clone;
	}

	String subTableName(String sub) {
		return this.tableName.replace(WILDCHAR, sub);
	}

	public List<ColumnMeta> createColumns() {
		return createColumns;
	}

	private static boolean timeOnly(Class<?> type) {
		return type == Time.class || type == LocalTime.class;
	}

	public Class<?> pojoClz() {
		return this.pojoClz;
	}

	public String getComment() {
		Comment c = this.pojoClz.getAnnotation(Comment.class);
		return c == null ? "" : c.value();
	}

	public SoftDeleteMeta getSoftDelete() {
		return softDelete;
	}

}
