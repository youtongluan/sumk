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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.yx.common.ItemJoiner;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class SelectBuilder extends AbstractSqlBuilder<List<Map<String, Object>>> {

	public SelectBuilder(SumkDbVisitor<List<Map<String, Object>>> visitor) {
		super(visitor);
		this.fromCache = DBSettings.fromCache();
		this.toCache = DBSettings.toCache();
	}

	protected static final String[] COMPARES = { ">", ">=", "<", "<=", " like ", " != " };// like前后要有空格
	protected static final int BIG = 0;
	protected static final int BIG_EQUAL = 1;
	protected static final int LESS = 2;
	protected static final int LESS_EQUAL = 3;
	protected static final int LIKE = 4;
	protected static final int NOT = 5;

	protected List<String> selectColumns;

	protected Map<String, Object>[] _compare;

	protected List<Order> orderby;

	protected int offset;

	protected int limit;

	protected boolean fromCache;
	protected boolean toCache;

	protected boolean allowEmptyWhere;

	protected CompareNullPolicy compareNullPolicy = CompareNullPolicy.CONTINUE;

	@Override
	public MapedSql toMapedSql() throws Exception {
		List<Object> paramters = new ArrayList<>(10);
		this.pojoMeta = parsePojoMeta(true);
		StringBuilder sql = new StringBuilder(32);
		sql.append("SELECT ").append(this.buildField()).append(" FROM ").append(this.pojoMeta.getTableName());
		CharSequence where = this.buildWhere(paramters);
		if (StringUtil.isEmpty(where) && !this.allowEmptyWhere) {
			SumkException.throwException(63254325, "empty where");
		}
		if (StringUtil.isNotEmpty(where)) {
			sql.append(" WHERE ").append(where);
		}
		CharSequence order = buildOrder();
		if (StringUtil.isNotEmpty(order)) {
			sql.append(" ORDER BY ").append(order);
		}
		if (this.limit > 0) {
			buildLimitAndOffset(sql);
		}
		return new MapedSql(sql.toString(), paramters);
	}

	/**
	 * 组装分页，也就是offset和limit
	 * 
	 * @param sql
	 *            已组装出来的sql
	 */
	protected void buildLimitAndOffset(StringBuilder sql) {

		sql.append(" LIMIT ").append(this.limit);
		if (offset > 0) {
			sql.append(" OFFSET ").append(this.offset);
		}
	}

	protected CharSequence buildOrder() {
		if (CollectionUtil.isEmpty(this.orderby)) {
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
		StringJoiner sj = new StringJoiner(",");
		if (this.selectColumns != null && this.selectColumns.size() > 0) {
			for (String filedName : this.selectColumns) {
				ColumnMeta cm = pojoMeta.getByFieldName(filedName);
				sj.add(cm.dbColumn);
			}
			return sj.toString();
		}
		for (ColumnMeta cm : this.pojoMeta.fieldMetas) {
			sj.add(cm.dbColumn);
		}
		return sj.toString();
	}

	protected CharSequence buildWhere(List<Object> paramters) {
		ItemJoiner joiner = new ItemJoiner(" AND ", null, null);
		joiner.appendNotEmptyItem(buildValid(paramters)).appendNotEmptyItem(buildIn(paramters))
				.appendNotEmptyItem(buildCompare(paramters));
		return joiner.toCharSequence();
	}

	private CharSequence buildValid(List<Object> paramters) {
		SoftDeleteMeta softDelete = this.pojoMeta.softDelete;
		if (softDelete == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (softDelete.equalValid) {
			sb.append(softDelete.columnName).append(" =? ");
			paramters.add(softDelete.validValue);
		} else {
			sb.append(softDelete.columnName).append(" != ? ");
			paramters.add(softDelete.inValidValue);
		}
		return sb;
	}

	private CharSequence buildCompare(List<Object> paramters) {
		if (this._compare == null) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create(" AND ", " ( ", " ) ");
		for (int i = 0; i < COMPARES.length; i++) {
			this.parseMap(joiner, this._compare[i], i, paramters);
		}
		return joiner.toCharSequence();
	}

	private CharSequence buildIn(List<Object> paramters) {
		if (this.in == null || this.in.isEmpty()) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create(" OR ", " ( ", " ) ");
		List<Map<String, Object>> list = this.in;
		for (Map<String, Object> map : list) {
			CharSequence sub = this.parseEqual(map, paramters);
			if (sub == null) {
				continue;
			}
			joiner.item().append(sub);
		}
		return joiner.toCharSequence();
	}

	private void parseMap(ItemJoiner joiner, Map<String, Object> src, int compareIndex, List<Object> paramters) {
		if (CollectionUtil.isEmpty(src)) {
			return;
		}

		for (Map.Entry<String, Object> en : src.entrySet()) {
			String filedName = en.getKey();
			Object v = en.getValue();
			ColumnMeta cm = pojoMeta.getByFieldName(filedName);
			if (cm == null) {
				if (this.failIfPropertyNotMapped) {
					SumkException.throwException(234, filedName + " has no mapper");
				}
				continue;
			}
			if (v == null) {
				if (compareIndex == NOT) {
					joiner.item().append(cm.dbColumn).append(" IS NOT NULL ");
					continue;
				}
				if (this.compareNullPolicy == null || this.compareNullPolicy == CompareNullPolicy.CONTINUE) {
					continue;
				}
				if (this.compareNullPolicy == CompareNullPolicy.FAIL) {
					SumkException.throwException(2342423, filedName + "的值为null");
				}
			}
			joiner.item().append(cm.dbColumn).append(COMPARES[compareIndex]).append(" ? ");
			paramters.add(v);
		}

	}

	private CharSequence parseEqual(Map<String, Object> src, List<Object> paramters) {
		if (CollectionUtil.isEmpty(src)) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create(" AND ", " ( ", " ) ");
		src.forEach((filedName, v) -> {

			ColumnMeta cm = pojoMeta.getByFieldName(filedName);
			if (cm == null) {
				if (this.failIfPropertyNotMapped) {
					SumkException.throwException(234, filedName + " has no mapper");
				}
				return;
			}
			if (v == null) {
				joiner.item().append(cm.dbColumn).append(" IS NULL ");
				return;
			}
			joiner.item().append(cm.dbColumn).append("=? ");
			paramters.add(v);
		});

		return joiner.toCharSequence();
	}

	protected static class Order {

		String name;

		boolean desc;

		public String toString(PojoMeta pm) {
			ColumnMeta cm = pm.getByFieldName(name);
			String dbName = cm == null ? name : cm.dbColumn;
			if (desc) {
				return dbName + " desc";
			}
			return dbName;
		}

	}

}
