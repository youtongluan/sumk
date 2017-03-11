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
package org.yx.db.sql;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.yx.conf.AppInfo;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.Assert;
import org.yx.util.CollectionUtils;
import org.yx.util.StringUtils;

public class SelectBuilder extends AbstractSqlBuilder<List<Map<String, Object>>> {

	public SelectBuilder(SumkDbVisitor<List<Map<String, Object>>> visitor) {
		super(visitor);
		this.failIfPropertyNotMapped = FAIL_IF_PROPERTY_NOT_MAPPED;
		this.fromCache = !"false".equalsIgnoreCase(AppInfo.get("sumk.sql.fromCache"));
		this.toCache = !"false".equalsIgnoreCase(AppInfo.get("sumk.sql.toCache"));
		this.withnull = false;
	}

	protected static String[] COMPARES = { ">", ">=", "<", "<=" };

	List<String> selectColumns;

	/**
	 * 0：大于 1：大于等于 2：小于 3：小于等于
	 */
	protected Map<String, Object>[] _compare;

	List<Order> orderby;

	int offset;

	int limit;

	boolean fromCache;
	boolean toCache;

	private MapedSql ms;

	protected boolean allowEmptyWhere;

	protected boolean failIfPropertyNotMapped;

	/**
	 * sql不包含"where"
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public MapedSql toMapedSql() throws Exception {
		this.ms = new MapedSql();
		this.pojoMeta = getPojoMeta();
		Assert.notNull(pojoMeta, "pojo meta cannot be null");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(this.buildField()).append(" FROM ").append(this.pojoMeta.getTableName());
		CharSequence where = this.buildWhere();
		if (StringUtils.isEmpty(where) && !this.allowEmptyWhere) {
			SumkException.throwException(63254325, "empty where");
		}
		if (StringUtils.isNotEmpty(where)) {
			sql.append(" WHERE ").append(where);
		}
		CharSequence order = buildOrder();
		if (StringUtils.isNotEmpty(order)) {
			sql.append(" ORDER BY ").append(order);
		}
		if (this.offset >= 0 && this.limit > 0) {
			sql.append(" LIMIT ").append(this.offset).append(',').append(this.limit);
		}
		ms.sql = sql.toString();
		return ms;
	}

	protected CharSequence buildOrder() {
		if (CollectionUtils.isEmpty(this.orderby)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Order order : this.orderby) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append(order.toString(this.pojoMeta));
		}
		return sb;
	}

	protected CharSequence buildField() {
		if (this.selectColumns != null && this.selectColumns.size() > 0) {
			return String.join(",", this.selectColumns.toArray(new String[this.selectColumns.size()]));
		}
		StringJoiner sj = new StringJoiner(",");
		for (ColumnMeta cm : this.pojoMeta.fieldMetas) {
			sj.add(cm.getDbColumn());
		}
		return sj.toString();
	}

	protected CharSequence buildWhere() {
		ItemJoiner joiner = new ItemJoiner(" AND ", "", "");
		joiner.addNotEmptyItem(buildValid()).addNotEmptyItem(buildIn()).addNotEmptyItem(buildCompare());
		return joiner.toCharSequence();
	}

	private CharSequence buildValid() {
		SoftDeleteMeta sm = this.pojoMeta.softDelete;
		if (sm == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(sm.columnName).append(" =? ");
		ms.addParam(sm.validValue);
		return sb;
	}

	private CharSequence buildCompare() {
		if (this._compare == null) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create();
		for (int i = 0; i < COMPARES.length && i < this._compare.length; i++) {
			Map<String, Object> map = this._compare[i];
			CharSequence sub = this.parseMap(map, COMPARES[i]);
			if (sub == null) {
				continue;
			}
			joiner.item().append(sub);
		}
		return joiner.toCharSequence();
	}

	private CharSequence buildIn() {
		if (this.in == null || this.in.isEmpty()) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create(" OR ");
		List<Map<String, Object>> list = this.in;
		for (Map<String, Object> map : list) {
			CharSequence sub = this.parseEqual(map);
			if (sub == null) {
				continue;
			}
			joiner.item().append(sub);
		}
		return joiner.toCharSequence();
	}

	/**
	 * 用AND方式解析src中的key-value，如果存在不能被匹配的key，就抛出异常<BR>
	 * 返回的sql，没有用()包围起来，如果需要的话，要自己完成这一步。
	 * 
	 * @param src
	 * @param 比较符号：=、>、>=等
	 * @return 如果src为空，就返回null
	 */
	private CharSequence parseMap(Map<String, Object> src, String compare) {
		if (CollectionUtils.isEmpty(src)) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create();
		src.forEach((filedName, v) -> {
			ColumnMeta cm = pojoMeta.getByFieldName(filedName);
			if (v == null) {
				return;
			}
			if (cm == null) {
				if (failIfPropertyNotMapped) {
					SumkException.throwException(234, filedName + " has no mapper");
				}
				return;
			}
			joiner.item().append(cm.getDbColumn()).append(compare).append(" ? ");
			ms.addParam(v);
		});

		return joiner.toCharSequence();
	}

	private CharSequence parseEqual(Map<String, Object> src) {
		if (CollectionUtils.isEmpty(src)) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create();
		src.forEach((filedName, v) -> {
			if (v == null && !this.withnull) {

				return;
			}
			ColumnMeta cm = pojoMeta.getByFieldName(filedName);
			if (cm == null) {
				if (failIfPropertyNotMapped) {
					SumkException.throwException(234, filedName + " has no mapper");
				}
				return;
			}
			if (v == null) {
				joiner.item().append(cm.getDbColumn()).append(" IS NULL ");
				return;
			}
			joiner.item().append(cm.getDbColumn()).append(" = ? ");
			ms.addParam(v);
		});

		return joiner.toCharSequence();
	}

	protected static class Order {
		/**
		 * java中的字段名
		 */
		String name;
		/**
		 * true表示降序
		 */
		boolean desc;

		/**
		 * order by的语句
		 */
		public String toString(PojoMeta pm) {
			ColumnMeta cm = pm.getByFieldName(name);
			String dbName = cm == null ? name : cm.getDbColumn();
			if (desc) {
				return dbName + " desc";
			}
			return dbName;
		}

	}

}
