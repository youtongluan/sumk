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

import static org.yx.db.sql.Operation.BIG;
import static org.yx.db.sql.Operation.BIG_EQUAL;
import static org.yx.db.sql.Operation.IN;
import static org.yx.db.sql.Operation.LESS;
import static org.yx.db.sql.Operation.LESS_EQUAL;
import static org.yx.db.sql.Operation.LIKE;
import static org.yx.db.sql.Operation.NOT;
import static org.yx.db.sql.Operation.NOT_IN;
import static org.yx.db.sql.Operation.NOT_LIKE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.yx.db.enums.CacheType;
import org.yx.db.event.DBEventPublisher;
import org.yx.db.event.QueryEvent;
import org.yx.db.kit.DBKits;
import org.yx.db.visit.Exchange;
import org.yx.db.visit.PojoResultHandler;
import org.yx.db.visit.ResultHandler;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;
import org.yx.util.kit.Asserts;

/**
 * 比较跟整个addEqual是add关系。同一种比较类型，比如less，它的一个key只能设置一次，后设置的会覆盖前面设置的<BR>
 * 比较中用到的key，都是java中的key，大小写敏感.
 */
public class Select extends SelectBuilder {
	public Select(SumkDbVisitor<List<Map<String, Object>>> visitor) {
		super(visitor);
	}

	/**
	 * @param onOff 如果为true，会验证map参数中，是否存在无效的key，预防开发人员将key写错。默认为true
	 * @return 当前对象
	 */
	public Select failIfPropertyNotMapped(boolean onOff) {
		this.setOnOff(DBFlag.FAIL_IF_PROPERTY_NOT_MAPPED, onOff);
		return this;
	}

	/**
	 * 物理分表的情况下，设置分区名。这个方法只能调用一次
	 * 
	 * @param sub 分区名
	 * @return 当前对象
	 */
	public Select partition(String sub) {
		sub(sub);
		return this;
	}

	/**
	 * 允许不设置where条件
	 * 
	 * @param empty true表示允许where条件为空
	 * @return 当前对象
	 */
	public Select allowEmptyWhere(boolean empty) {
		this.setOnOff(DBFlag.SELECT_ALLOW_EMPTY_WHERE, empty);
		return this;
	}

	protected ResultHandler resultHandler;

	public Select resultHandler(ResultHandler resultHandler) {
		this.resultHandler = Objects.requireNonNull(resultHandler);
		return this;
	}

	public Select compareAllowNull(boolean onOff) {
		this.setOnOff(DBFlag.SELECT_COMPARE_ALLOW_NULL, onOff);
		return this;
	}

	public Select compareIgnoreNull(boolean onOff) {
		this.setOnOff(DBFlag.SELECT_COMPARE_IGNORE_NULL, onOff);
		return this;
	}

	protected Select addCompares(Operation op, Object pojo) {
		Map<String, Object> map = this.populate(pojo, false);
		if (CollectionUtil.isEmpty(map)) {
			return this;
		}
		for (Map.Entry<String, Object> en : map.entrySet()) {
			this.setCompare(op, en.getKey(), en.getValue());
		}
		return this;
	}

	protected Select setCompare(Operation op, String key, Object value) {
		if (key == null || key.isEmpty()) {
			return this;
		}
		if (_compare == null) {
			_compare = new ArrayList<>(8);
		}
		this._compare.add(new ColumnOperation(key, op, value));
		return this;
	}

	/**
	 * 设置大于,一个key只能设置一次，后设置的会覆盖前面设置的。<BR>
	 * 
	 * @param key   java字段的名称
	 * @param value 值
	 * @return 当前对象
	 */
	public Select bigThan(String key, Object value) {
		return setCompare(BIG, key, value);
	}

	public Select bigOrEqual(String key, Object value) {
		return setCompare(BIG_EQUAL, key, value);
	}

	public Select lessThan(String key, Object value) {
		return setCompare(LESS, key, value);
	}

	public Select lessOrEqual(String key, Object value) {
		return setCompare(LESS_EQUAL, key, value);
	}

	/**
	 * like操作，%号要自己添加
	 * 
	 * @param key   字段名
	 * @param value 值，不会自动添加%。如果是Collection类型，会自动转化为( OR )的方式
	 * @return 当前对象
	 */
	public Select like(String key, Object value) {
		return setCompare(LIKE, key, value);
	}

	public Select notLike(String key, Object value) {
		return setCompare(NOT_LIKE, key, value);
	}

	/**
	 * 不等于操作
	 * 
	 * @param key   字段名
	 * @param value 值，不会自动添加%
	 * @return 当前对象
	 */
	public Select not(String key, Object value) {
		return setCompare(NOT, key, value);
	}

	/**
	 * sql中的in查询
	 * 
	 * @param key    字段名
	 * @param values 值列表，不能为空
	 * @return 当前对象
	 */
	public Select in(String key, Collection<?> values) {
		return setCompare(IN, key, values.toArray(new Object[values.size()]));
	}

	public Select notIn(String key, Collection<?> values) {
		return setCompare(NOT_IN, key, values.toArray(new Object[values.size()]));
	}

	public Select bigThan(Object pojo) {
		return addCompares(BIG, pojo);
	}

	/**
	 * 大于等于
	 * 
	 * @param pojo 对pojo中所有的kv做大于等于操作
	 * @return 当前对象
	 */
	public Select bigOrEqual(Object pojo) {
		return addCompares(BIG_EQUAL, pojo);
	}

	public Select lessThan(Object pojo) {
		return addCompares(LESS, pojo);
	}

	/**
	 * 小于或等于
	 * 
	 * @param pojo 对pojo中所有的kv做小于等于操作
	 * @return 当前对象
	 */
	public Select lessOrEqual(Object pojo) {
		return addCompares(LESS_EQUAL, pojo);
	}

	public Select like(Object pojo) {
		return addCompares(LIKE, pojo);
	}

	public Select not(Object pojo) {
		return addCompares(NOT, pojo);
	}

	/**
	 * 根据字段名和判断条件移除所有符合条件的比较
	 * 
	 * @param key java字段名，可以为null。null表示所有的字段都要移除
	 * @param op  比较条件，可以为null。null表示所有的条件都要移除
	 * @return 当前对象
	 */
	public Select removeCompares(String key, Operation op) {
		if (_compare == null) {
			return this;
		}
		Iterator<ColumnOperation> it = this._compare.iterator();
		while (it.hasNext()) {
			ColumnOperation cp = it.next();
			if (key != null && !key.equals(cp.name)) {
				continue;
			}
			if (op != null && cp.type != op) {
				continue;
			}
			it.remove();
		}
		if (this._compare.isEmpty()) {
			this._compare = null;
		}
		return this;
	}

	/**
	 * 升序排列。asc和desc的调用顺序决定了在sql中出现的顺序。 此方法可以调用多次
	 * 
	 * @param field 升序字段
	 * @return 当前对象
	 */
	public Select orderByAsc(String field) {
		return this.addOrderBy(field, false);
	}

	protected Select addOrderBy(String name, boolean desc) {
		if (this.orderby == null) {
			this.orderby = new ArrayList<>(2);
		}
		this.orderby.add(new Order(name, desc));
		return this;
	}

	/**
	 * 增加降序排列
	 * 
	 * @param field 降序字段
	 * @return 当前对象
	 */
	public Select orderByDesc(String field) {
		return this.addOrderBy(field, true);
	}

	/**
	 * 设置查询的便宜量，从0开始。
	 * 
	 * @param offset from的位置
	 * @return 当前对象
	 */
	public Select offset(int offset) {
		if (this.isOn(DBFlag.SELECT_IGNORE_MAX_OFFSET)) {
			if (offset < 0 || offset > DBSettings.MaxOffset()) {
				throw new SumkException(-235345347,
						"offset需要在0-" + DBSettings.MaxOffset() + "之间,通过ignoreMaxOffset(true)可以取消这个限制");
			}
		}
		this.offset = offset;
		return this;
	}

	/**
	 * 
	 * @param limit 返回的最大条数。
	 * @return 当前对象
	 */
	public Select limit(int limit) {
		if (this.isOn(DBFlag.SELECT_IGNORE_MAX_LIMIT)) {
			if (limit <= 0 || limit > DBSettings.MaxLimit()) {
				throw new SumkException(-235345346,
						"limit需要在1-" + DBSettings.MaxLimit() + "之间,通过ignoreMaxLimit(true)可以取消这个限制");
			}
		}
		this.limit = limit;
		return this;
	}

	/**
	 * 
	 * @param columns 设置查询放回的列，列名是java中的字段名。如果不设，将返回所有的字段
	 * @return 当前对象
	 */
	public Select selectColumns(String... columns) {
		if (columns == null || columns.length == 0) {
			this.selectColumns = null;
			return this;
		}
		this.selectColumns = Arrays.asList(columns);
		return this;
	}

	/**
	 * 如果为false，就不会从缓存中加载数据
	 * 
	 * @param fromCache 默认为true。sumk.sql.fromCache=false可以将全局参数设为false
	 * @return 当前对象
	 */
	public Select fromCache(boolean fromCache) {
		this.setOnOff(DBFlag.SELECT_FROM_CACHE, fromCache);
		return this;
	}

	/**
	 * 如果为false，查出的结果将不会用于更新缓存
	 * 
	 * @param toCache 该参数设为true的实际意义不大
	 * @return 当前对象
	 */
	public Select toCache(boolean toCache) {
		this.setOnOff(DBFlag.SELECT_TO_CACHE, toCache);
		return this;
	}

	public Select ignoreMaxLimit(boolean on) {
		this.setOnOff(DBFlag.SELECT_IGNORE_MAX_LIMIT, on);
		return this;
	}

	public Select ignoreMaxOffset(boolean on) {
		this.setOnOff(DBFlag.SELECT_IGNORE_MAX_OFFSET, on);
		return this;
	}

	/**
	 * 设置相等的条件。本方法可以被多次执行。 src中的各个条件是and关系。不同src之间是or关系<BR>
	 * <B>注意：如果pojo是map类型，那么它的null值是有效条件</B>
	 * 
	 * @param src map或pojo类型。
	 * @return 当前对象
	 */
	public Select addEqual(Object src) {
		this._addIn(src);
		return this;
	}

	/**
	 * 各个addEqual之间的条件是OR，如果要组装AND条件，请用addEqual(Object src)
	 * 
	 * @param field 字段名
	 * @param value 要查询的条件的值
	 * @return 当前对象
	 */
	public Select addEqual(String field, Object value) {
		this._addInByMap(Collections.singletonMap(field, value));
		return this;
	}

	/**
	 * 传入多个条件
	 * 
	 * @param ins 集合各元素之间是or关系，对象中各个kv是and关系
	 * @return 当前对象
	 */
	public Select addEquals(Collection<?> ins) {
		for (Object src : ins) {
			this._addIn(src);
		}
		return this;
	}

	/**
	 * 通过数据库主键列表查询主键，单主键ids就只有一个，多主键就传入多个 <B>注意：调用本方法之前，要确保调用过tableClass()方法</B>
	 * 
	 * @param ids id列表，顺序跟pojo中定义的一致(按order顺序或书写顺序)
	 * @return 注意：调用本方法之前，要确保调用过tableClass()方法
	 */
	public Select byDatabaseId(Object... ids) {
		return byId(true, ids);
	}

	/**
	 * 通过redis主键列表查询主键，单主键ids就只有一个，多主键就传入多个<BR>
	 * 所有id属于同一条记录，如果要使用单主键的in查询，请用本类的in方法<BR>
	 * <B>注意：调用本方法之前，要确保调用过tableClass()方法</B>
	 * 
	 * @param ids id列表，顺序跟pojo中定义的一致（按order顺序或书写顺序）
	 * @return 当前对象
	 * 
	 */
	public Select byCacheId(Object... ids) {
		return byId(false, ids);
	}

	protected Select byId(boolean databaseId, Object... ids) {
		if (ids == null || ids.length == 0) {
			return this;
		}
		makeSurePojoMeta();
		List<ColumnMeta> cms = databaseId ? this.pojoMeta.getDatabaseIds() : this.pojoMeta.getCacheIDs();
		Asserts.requireTrue(cms != null && cms.size() == ids.length, pojoMeta.getTableName() + "没有设置主键，或者主键个数跟参数个数不一致");
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < ids.length; i++) {
			map.put(cms.get(i).getFieldName(), ids[i]);
		}
		_addInByMap(map);
		return this;
	}

	public Select tableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
		return this;
	}

	protected ResultHandler resultHandler() {
		return this.resultHandler == null ? PojoResultHandler.handler : this.resultHandler;
	}

	protected boolean canUseInCache() {
		return this.pojoMeta.cacheIDs.size() == 1 && CollectionUtil.isEmpty(this.in)
				&& this.pojoMeta.cacheType() == CacheType.SINGLE && _compare != null && _compare.size() == 1
				&& _compare.get(0).type == IN
				&& _compare.get(0).name.equals(this.pojoMeta.cacheIDs.get(0).field.getName());
	}

	protected <T> List<T> queryFromCache(Exchange exchange) throws Exception {
		if (CollectionUtil.isEmpty(this.in) && CollectionUtil.isEmpty(this._compare)) {
			return null;
		}
		boolean fromCache = this.isOn(DBFlag.SELECT_FROM_CACHE);
		if (!(fromCache && this.orderby == null && this.offset == 0 && !pojoMeta.isNoCache())) {
			return null;
		}

		String singleKeyName = this.canUseInCache() ? this.pojoMeta.cacheIDs.get(0).field.getName() : null;
		if (singleKeyName != null) {
			Object[] vs = (Object[]) _compare.get(0).value;
			List<Map<String, Object>> newIN = new ArrayList<>(vs.length);
			for (Object v : vs) {
				newIN.add(Collections.singletonMap(singleKeyName, v));
			}
			exchange.setLeftIn(newIN);
		} else if (CollectionUtil.isEmpty(_compare)) {
			exchange.setLeftIn(this.in);
		} else {
			return null;
		}

		List<T> list = new ArrayList<>();
		exchange.findFromCache(pojoMeta);
		if (exchange.getData() != null && exchange.getData().size() > 0) {
			List<T> tmp = this.resultHandler().parseFromJson(pojoMeta, exchange.getData(),
					CollectionUtil.unmodifyList(this.selectColumns));
			if (tmp != null && tmp.size() > 0) {
				list.addAll(tmp);
			}
		}
		List<Map<String, Object>> left = exchange.getLeftIn();

		if (left == null) {
			return list;
		}

		if (singleKeyName != null) {
			List<Object> vs = new ArrayList<>(left.size());
			for (Map<String, Object> m : left) {
				vs.add(m.get(singleKeyName));
			}
			this._compare = Collections.singletonList(new ColumnOperation(singleKeyName, IN, vs.toArray()));
		} else {
			this.in = left;
		}
		return list;
	}

	public <T> List<T> queryList() {

		final List<Map<String, Object>> origin = this.in = CollectionUtil.unmodifyList(this.in);
		final List<ColumnOperation> orginCompare = this._compare = CollectionUtil.unmodifyList(this._compare);
		try {
			makeSurePojoMeta().getCounter().incrQueryCount();
			Exchange exchange = new Exchange();
			List<T> list = this.queryFromCache(exchange);
			if (list != null && CollectionUtil.isEmpty(exchange.getLeftIn())) {
				return list;
			}
			boolean canUseCache = true;
			if (list == null) {
				list = Collections.emptyList();
				canUseCache = false;
			}
			List<T> dbData = this.resultHandler().parse(pojoMeta, this.accept(visitor));
			if (dbData == null || dbData.isEmpty()) {
				return list;
			}
			list = merge(list, dbData);
			List<Map<String, Object>> eventIn = canUseCache ? exchange.getLeftIn() : this.in;

			if (this.isOn(DBFlag.SELECT_TO_CACHE) && selectColumns == null && this.offset <= 0
					&& (canUseCache || CollectionUtil.isEmpty(this._compare))
					&& (limit <= 0 || limit >= DBSettings.MaxLimit()) && CollectionUtil.isNotEmpty(eventIn)
					&& dbData.size() < DBSettings.maxQueryCacheSize()) {

				QueryEvent event = new QueryEvent(this.pojoMeta.getTableName());
				event.setIn(eventIn);
				event.setResult(dbData);
				DBEventPublisher.publishQuery(event);
			}
			if (this.limit > 0 && list.size() > this.limit) {
				return list.subList(0, this.limit);
			}
			return list;
		} catch (Exception e) {
			throw SumkException.wrap(e);
		} finally {
			this.in = origin;
			this._compare = orginCompare;
		}
	}

	protected <T> List<T> merge(List<T> cacheList, List<T> dbList) throws Exception {
		if (cacheList.isEmpty()) {
			return dbList;
		}
		List<T> ret = new ArrayList<>(cacheList.size() + dbList.size());
		ret.addAll(dbList);
		Set<String> keys = new HashSet<>();
		PojoMeta pm = this.pojoMeta;
		List<ColumnMeta> columns = pm.databaseIds;
		for (T t : dbList) {
			String key = pm.joinColumns(t, false, columns);
			if (key != null) {
				keys.add(key);
			}
		}
		for (T t : cacheList) {
			String key = pm.joinColumns(t, false, columns);
			if (key == null || keys.add(key)) {
				ret.add(t);
			}
		}
		return ret;
	}

	public <T> T queryOne() {
		return DBKits.queryOne(this.queryList());
	}

	/**
	 * 根据select的条件，查询符合条件的记录数。其中offset、limit、order by属性被过滤掉<BR>
	 * 这个方法可以在select执行前调用，也可以在select执行后调用
	 * 
	 * @return 符合条件的数据库记录数
	 */
	public long count() {
		return new Count(this).execute();
	}
}
