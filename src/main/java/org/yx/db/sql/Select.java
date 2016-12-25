package org.yx.db.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.db.event.DBEventPublisher;
import org.yx.db.event.QueryEvent;
import org.yx.db.visit.Exchange;
import org.yx.db.visit.PojoResultHandler;
import org.yx.db.visit.ResultHandler;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.Assert;
import org.yx.util.CollectionUtils;
import org.yx.util.MapBuilder;

public class Select extends SelectBuilder {
	public Select(SumkDbVisitor<List<Map<String, Object>>> visitor) {
		super(visitor);
	}

	private static final int MAX_CACHE_LIMIT = 5000;
	private ResultHandler resultHandler;

	/**
	 * 默认返回的是List<Pojo>，如果想将pojo改为Map<String,Object>。就用
	 * <code>MapResultHandler.handler</code>做参数
	 * 
	 * @param resultHandler
	 * @return
	 */
	public Select resultHandler(ResultHandler resultHandler) {
		this.resultHandler = resultHandler;
		return this;
	}

	@SuppressWarnings("unchecked")
	private Select setCompare(int index, Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return this;
		}
		if (_compare == null) {
			_compare = new HashMap[4];
		}
		this._compare[index] = map;
		return this;
	}

	@SuppressWarnings("unchecked")
	private Select setCompare(int index, String key, Object value) {
		if (key == null || key.isEmpty() || value == null) {
			return this;
		}
		if (_compare == null) {
			_compare = new HashMap[4];
		}
		if (this._compare[index] == null) {
			this._compare[index] = new HashMap<>();
		}
		this._compare[index].put(key, value);
		return this;
	}

	/**
	 * 设置大于
	 */
	public Select bigThan(String key, Object value) {
		return setCompare(0, key, value);
	}

	/**
	 * 大于等于
	 * 
	 * @param big
	 * @return
	 */
	public Select bigOrEqual(String key, Object value) {
		return setCompare(1, key, value);
	}

	/**
	 * 小于
	 * 
	 * @param big
	 * @return
	 */
	public Select lessThan(String key, Object value) {
		return setCompare(2, key, value);
	}

	public Select lessOrEqual(String key, Object value) {
		return setCompare(3, key, value);
	}

	/**
	 * 设置大于
	 */
	public Select bigThan(Map<String, Object> map) {
		return setCompare(0, map);
	}

	/**
	 * 大于等于
	 * 
	 * @param big
	 * @return
	 */
	public Select bigOrEqual(Map<String, Object> map) {
		return setCompare(1, map);
	}

	/**
	 * 小于
	 */
	public Select lessThan(Map<String, Object> map) {
		return setCompare(2, map);
	}

	/**
	 * 小于或等于
	 * 
	 * @param map
	 * @return
	 */
	public Select lessOrEqual(Map<String, Object> map) {
		return setCompare(3, map);
	}

	/**
	 * 升序排列。asc和desc的调用顺序决定了在sql中出现的顺序
	 * 
	 * @param order
	 * @return
	 */
	public Select OrderByAsc(String field) {
		return this.addOrderBy(field, false);
	}

	private Select addOrderBy(String name, boolean desc) {
		if (this.orderby == null) {
			this.orderby = new ArrayList<>();
		}
		Order order = new Order();
		order.name = name;
		order.desc = desc;
		this.orderby.add(order);
		return this;
	}

	/**
	 * 增加降序排列
	 * 
	 * @param field
	 * @return
	 */
	public Select OrderByDesc(String field) {
		return this.addOrderBy(field, true);
	}

	/**
	 * 设置查询的便宜量，从0开始。
	 * 
	 * @param offset
	 * @return
	 */
	public Select offset(int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * 设置返回的最大条数。
	 * 
	 * @param limit
	 * @return
	 */
	public Select limit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * 设置查询放回的列，列名是java中的字段名。如果不设，将返回所有的字段
	 * 
	 * @param columns
	 * @return
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
	 * @param fromCache
	 *            默认为true。sumk.sql.fromCache=false可以将全局参数设为false
	 * @return
	 */
	public Select fromCache(boolean fromCache) {
		this.fromCache = fromCache;
		return this;
	}

	/**
	 * 如果为false，查出的结果将不会用于更新缓存
	 * 
	 * @param toCache
	 *            默认为true。sumk.sql.toCache=false可以将全局参数设为false
	 * @return
	 */
	public Select toCache(boolean toCache) {
		this.toCache = toCache;
		return this;
	}

	/**
	 * 如果为true，参数中存在无法解析的属性时，将会被忽略，而不是抛出异常
	 * 
	 * @param fail
	 *            默认为false。sumk.sql.failIfPropertyNotMapped=true可以将全局参数设为true
	 * @return
	 */
	public Select failIfPropertyNotMapped(boolean fail) {
		this.failIfPropertyNotMapped = fail;
		return this;
	}

	/**
	 * 如果为true，在判断是否相等时，会将null值解析成is null。否则将忽略null值
	 * 
	 * @param v
	 *            默认为false
	 * @return
	 */
	public Select parseNULL(boolean v) {
		this.withnull = v;
		return this;
	}

	/**
	 * 设置相等的条件。本方法可以被多次执行。 src中的各个条件是and关系。不同src之间是or关系
	 * 
	 * @param src
	 *            map或pojo类型。
	 * @return
	 */
	public Select addEqual(Object src) {
		this._addIn(src);
		return this;
	}

	/**
	 * 各个addEqual之间的条件是OR，如果要组装AND条件，请用addEqual(Object src)
	 * 
	 * @param field
	 *            字段名
	 * @param value
	 *            要查询的条件的值
	 * @return
	 */
	public Select addEqual(String field, Object value) {
		this._addIn(MapBuilder.create(field, value).toMap());
		return this;
	}

	/**
	 * 通过数据库主键列表查询主键，本方法只支持单主键类型。多主键请用addEqual()或addEquals()方法
	 * 
	 * @param ids
	 * @return 注意：调用本方法之前，要确保调用过tableClass()方法
	 */
	public Select byPrimaryId(Object... ids) {
		return byId(true, ids);
	}

	/**
	 * 通过redis主键列表查询主键，是addEquals()的快捷方式，本方法只支持单主键类型。多主键请用addEqual()或addEquals()
	 * 方法
	 * 
	 * @param ids
	 * @return 注意：调用本方法之前，要确保调用过tableClass()方法
	 */
	public Select byRedisId(Object... ids) {
		return byId(false, ids);
	}

	private Select byId(boolean dbPrimary, Object... ids) {
		if (ids == null || ids.length == 0) {
			return this;
		}
		this.pojoMeta = this.getPojoMeta();
		Assert.notNull(this.pojoMeta, "make suer tableClass() has been called");
		ColumnMeta[] cms = dbPrimary ? this.pojoMeta.getPrimaryIDs() : this.pojoMeta.getRedisIDs();
		Assert.isTrue(cms != null && cms.length == 1,
				pojoMeta.getTableName() + " is not an one " + (dbPrimary ? "primary" : "redis") + " key table");
		String key = cms[0].getFieldName();
		Arrays.asList(ids).forEach(id -> {
			Map<String, Object> map = new HashMap<>();
			map.put(key, id);
			addEqual(map);
		});
		return this;
	}

	/**
	 * 传入多个条件
	 * 
	 * @param in
	 * @return
	 */
	public Select addEquals(List<Map<String, Object>> ins) {
		this.in.addAll(ins);
		return this;
	}

	public Select tableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
		return this;
	}

	protected ResultHandler resultHandler() {
		return this.resultHandler == null ? PojoResultHandler.handler : this.resultHandler;
	}

	/**
	 * 查询列表
	 * 
	 * @return 默认返回的是pojo列表，如果想返回其它类型，比如List<Map<String,Object>>，参见typeHandler()
	 * @throws SumkException
	 */
	public <T> List<T> queryList() {
		try {
			ResultHandler handler = this.resultHandler();
			this.pojoMeta = this.getPojoMeta();
			List<T> list = new ArrayList<>();
			List<Map<String, Object>> origin = this.in;
			Exchange exchange = new Exchange(origin);

			if (fromCache && this.selectColumns == null && _compare == null && this.orderby == null
					&& this.offset == 0) {
				exchange.findFromCache(pojoMeta);
				if (exchange.getData() != null && exchange.getData().size() > 0) {
					List<T> tmp = handler.parseFromJson(pojoMeta, exchange.getData());
					if (tmp != null && tmp.size() > 0) {
						list.addAll(tmp);
					}
				}
			}

			if (this.in != exchange.getLeftIn() && CollectionUtils.isNotEmpty(this.in)
					&& CollectionUtils.isEmpty(exchange.getLeftIn())) {
				return list;
			}
			this.in = exchange.getLeftIn();
			List<T> tmp = handler.parse(pojoMeta, this.accept(visitor));
			this.in = origin;
			if (tmp == null || tmp.isEmpty()) {
				return list;
			}
			list.addAll(tmp);

			if (this.toCache && selectColumns == null && _compare == null && this.offset == 0
					&& (limit <= 0 || limit >= MAX_CACHE_LIMIT) && exchange.getCanToRedis() != null
					&& exchange.getCanToRedis().size() > 0) {
				QueryEvent event = new QueryEvent(this.getPojoMeta().getTableName());
				event.setIn(exchange.getCanToRedis());
				event.setResult(tmp);
				DBEventPublisher.publish(event);
			}
			if (this.limit > 0 && list.size() > this.limit) {
				return list.subList(0, this.limit);
			}
			return list;
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

	/**
	 * 如果没有满足条件的记录，就返回null。否则返回list的第一个值
	 * 
	 * @return
	 * @throws SumkException
	 */
	public <T> T queryOne() {
		List<T> list = this.queryList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
