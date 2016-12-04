package org.yx.db.sql;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.yx.bean.CachedBean;
import org.yx.conf.AppInfo;
import org.yx.db.annotation.CacheType;
import org.yx.db.annotation.SoftDelete;
import org.yx.db.annotation.Table;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.redis.Counter;
import org.yx.redis.RedisPool;
import org.yx.util.StringUtils;

public class PojoMeta {
	private final static String KEY_SPLIT = ",";
	private final Table table;

	final ColumnMeta[] fieldMetas;

	public final Class<?> pojoClz;
	/**
	 * 只有redis中是单主键的时候，这个才有用<br>
	 * 它有可能长度为0，但不为null
	 */
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

	public ColumnMeta getByFieldName(String filedName) {
		if (filedName == null || filedName.isEmpty()) {
			return null;
		}
		return this.filedNameMap.get(filedName.toLowerCase());
	}

	/**
	 * 这张表不需要进行缓存，或者没有配置redis
	 * 
	 * @return
	 */
	public boolean isNoCache() {
		return table.cacheType() == CacheType.NOCACHE || RedisPool.defaultRedis() == null;
	}

	public CacheType cacheType() {
		return table.cacheType();
	}

	/**
	 * 是否使用数据库主键作为redis的id
	 * 
	 * @return
	 */
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
		List<ColumnMeta> rids = new ArrayList<ColumnMeta>();
		List<ColumnMeta> pids = new ArrayList<ColumnMeta>();
		for (ColumnMeta m : this.fieldMetas) {
			columnDBNameMap.put(m.getDbColumn().toLowerCase(), m);
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
		this.softDelete = CachedBean.get(SoftDeleteParser.class).parse(this.pojoClz.getAnnotation(SoftDelete.class));
		parseTable();
		String arrayName = "[L" + this.pojoClz.getName() + ";";
		try {
			this.pojoArrayClz = Class.forName(arrayName, false, this.getClass().getClassLoader());
		} catch (Exception e) {
			SumkException.throwException(3425345, arrayName + " can not be loaded", e);
		}
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
		this.pre = StringUtils.isEmpty(_pre) ? this.pojoClz.getSimpleName() : _pre;
		this.tableName = StringUtils.isEmpty(table.value()) ? this.pojoClz.getSimpleName().toLowerCase()
				: table.value();
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getPre() {
		return this.pre;
	}

	/**
	 * 当且仅当condition的redis主键全不为null，而非redis主键全为null时，才返回true
	 */
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

	/**
	 * 
	 * @param map
	 *            里面的key是数据库字段的名字
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object buildFromDBColumn(Map<String, Object> map) throws InstantiationException, IllegalAccessException {
		if (map == null) {
			return null;
		}
		Object ret = this.pojoClz.newInstance();
		Set<Entry<String, Object>> set = map.entrySet();
		for (Entry<String, Object> en : set) {
			String key = en.getKey();
			ColumnMeta m = this.getByColumnDBName(key);
			m.setValue(ret, en.getValue());
		}
		return ret;
	}

	/**
	 * 将pojo转化成map
	 * 
	 * @param source
	 * @param withnull
	 *            如果为true，那么null字段也会被包含进来
	 * @return 如果对象类型不对，或者对象里面的值都是空的，就返回空map
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Map<String, Object> populateByFieldName(Object source, boolean withnull)
			throws InstantiationException, IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		if (!this.pojoClz.isInstance(source)) {
			Log.get("event").debug("{} is not instance of {}", source.getClass().getName(), this.pojoClz.getName());
			return map;
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

	/**
	 * 利用map组件成pojo
	 * 
	 * @param map
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 */
	public Object buildPojo(Map<String, Object> map)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		Object obj = this.pojoClz.newInstance();
		for (ColumnMeta m : this.fieldMetas) {
			Object v = map.get(m.getFieldName());
			if (v == null) {
				continue;
			}
			m.setValue(obj, v);
		}
		return obj;
	}

	public Map<String, Object> populateByDbColumn(Object source, boolean withnull)
			throws InstantiationException, IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		if (!this.pojoClz.isInstance(source)) {
			Log.get("event").debug("{} is not instance of {}", source.getClass().getName(), this.pojoClz.getName());
			return map;
		}
		for (ColumnMeta m : this.fieldMetas) {
			Object v = m.value(source);
			if (!withnull && v == null) {
				continue;
			}
			String name = m.getDbColumn();
			map.put(name, v);
		}
		return map;
	}

	/**
	 * 获取redis的id（不包含前缀），必须全部的值都不为null，返回值才不为null
	 * 
	 * @param source
	 *            pojo对象，或者Map<String,Object>
	 * @param exceptionIfHasNull
	 *            如果为true，有null存在就抛异常，如果为false，就返回null
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getRedisID(Object source, boolean exceptionIfHasNull) throws Exception {
		if (Map.class.isInstance(source)) {
			return this.getRedisIDByMap((Map<String, Object>) source, exceptionIfHasNull);
		}
		String key = "";
		for (ColumnMeta m : this.redisIDs) {
			Object v = m.value(source);
			if (v == null) {
				if (exceptionIfHasNull) {
					SumkException.throwException(1232142356, "value of " + m.getFieldName() + " cannot be null");
				}
				return null;
			}
			if (key.isEmpty()) {
				key = String.valueOf(v);
			} else {
				key += KEY_SPLIT + v;
			}
		}
		return key;
	}

	private String getRedisIDByMap(Map<String, Object> map, boolean exceptionIfHasNull) {
		String key = "";
		for (ColumnMeta m : this.redisIDs) {
			Object v = map.get(m.getFieldName());
			if (v == null) {
				if (exceptionIfHasNull) {
					SumkException.throwException(1232142356, "value of " + m.getFieldName() + " cannot be null");
				}
				return null;
			}
			if (key.isEmpty()) {
				key = String.valueOf(v);
			} else {
				key += KEY_SPLIT + v;
			}
		}
		return key;
	}

}
