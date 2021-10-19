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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.yx.common.ItemJoiner;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class SelectBuilder extends AbstractSqlBuilder<List<Map<String, Object>>> {

	protected List<String> selectColumns;

	protected List<ColumnOperation> _compare;

	protected List<Order> orderby;

	protected int offset;

	protected int limit;

	public SelectBuilder(SumkDbVisitor<List<Map<String, Object>>> visitor) {
		super(visitor);
		this.limit = DBSettings.MaxLimit();
	}

	/**
	 * 这个方法用于两个地方，一个是框架内部调用，一旦外部调用有可能影响最终结果 另一个是开发调试的时候，调用本方法查看最终的sql，这时候它不需要放在事务中
	 * 
	 * @throws Exception 异常信息
	 */
	@Override
	public MapedSql toMapedSql() throws Exception {
		List<Object> paramters = new ArrayList<>(10);
		makeSurePojoMeta();
		StringBuilder sql = new StringBuilder(32);
		sql.append("SELECT ").append(this.buildField()).append(" FROM ").append(this.pojoMeta.getTableName());
		CharSequence where = this.buildWhere(paramters);
		if (StringUtil.isEmpty(where) && !this.isOn(DBFlag.SELECT_ALLOW_EMPTY_WHERE)) {
			throw new SumkException(63254325, "empty where");
		}
		if (StringUtil.isNotEmpty(where)) {
			sql.append(" WHERE ").append(where);
		}
		CharSequence order = buildOrder();
		if (StringUtil.isNotEmpty(order)) {
			sql.append(" ORDER BY ").append(order);
		}
		buildLimitAndOffset(sql);
		return new MapedSql(sql.toString(), paramters);
	}

	/**
	 * 组装分页，也就是offset和limit
	 * 
	 * @param sql 已组装出来的sql
	 */
	protected void buildLimitAndOffset(StringBuilder sql) {

		if (limit > 0) {
			sql.append(" LIMIT ").append(this.limit);
		}
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
		PojoMeta pojoMeta = this.pojoMeta;
		StringJoiner sj = new StringJoiner(",");
		if (this.selectColumns != null && this.selectColumns.size() > 0) {
			for (String filedName : this.selectColumns) {
				ColumnMeta cm = pojoMeta.getByFieldName(filedName);
				if (cm == null) {
					throw new SumkException(-5345340, filedName + "不是有效的java字段");
				}
				sj.add(cm.dbColumn);
			}
			return sj.toString();
		}
		for (ColumnMeta cm : pojoMeta.fieldMetas) {
			sj.add(cm.dbColumn);
		}
		return sj.toString();
	}

	protected CharSequence buildWhere(List<Object> paramters) {
		ItemJoiner joiner = new ItemJoiner(" AND ", null, null);
		joiner.appendNotEmptyItem(buildValid(paramters)).appendNotEmptyItem(buildEquals(paramters))
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
		if (CollectionUtil.isEmpty(this._compare)) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create(" AND ", " ( ", " ) ");
		for (ColumnOperation op : this._compare) {
			this.parseOperation(joiner, op, paramters);
		}
		return joiner.toCharSequence();
	}

	private CharSequence buildEquals(List<Object> paramters) {
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

	private void parseOperation(ItemJoiner joiner, ColumnOperation cop, List<Object> paramters) {
		String filedName = cop.name;
		Object v = cop.value;
		Operation type = cop.type;
		ColumnMeta cm = pojoMeta.getByFieldName(filedName);
		if (cm == null) {
			if (this.isOn(DBFlag.FAIL_IF_PROPERTY_NOT_MAPPED)) {
				throw new SumkException(-54675234, filedName + "这个字段没有在java的pojo类中定义");
			}
			return;
		}
		if (v == null) {
			if (type == Operation.NOT) {
				joiner.item().append(cm.dbColumn).append(" IS NOT NULL ");
				return;
			}
			if (!this.isOn(DBFlag.SELECT_COMPARE_ALLOW_NULL)) {
				throw new SumkException(2342423, filedName + "的值为null");
			}
			if (this.isOn(DBFlag.SELECT_COMPARE_IGNORE_NULL)) {
				return;
			}
		}

		if (v instanceof Collection) {
			ItemJoiner orJoiner = ItemJoiner.create(" OR ", " (", ") ");
			Collection<?> col = (Collection<?>) v;
			for (Object obj : col) {
				orJoiner.item().append(cm.dbColumn).append(type.op).append(" ? ");
				paramters.add(obj);
			}
			joiner.item().append(orJoiner, null, null);
			return;
		}

		joiner.item().append(cm.dbColumn).append(type.op);
		if (type == Operation.IN || type == Operation.NOT_IN) {
			joiner.append("(");
			boolean first = true;
			for (Object obj : (Object[]) v) {
				if (first) {
					joiner.append("?");
					first = false;
				} else {
					joiner.append(",?");
				}
				paramters.add(obj);
			}
			joiner.append(") ");
			return;
		}

		joiner.append(" ? ");
		paramters.add(v);

	}

	private CharSequence parseEqual(Map<String, Object> src, List<Object> paramters) {
		if (CollectionUtil.isEmpty(src)) {
			return null;
		}
		ItemJoiner joiner = ItemJoiner.create(" AND ", " ( ", " ) ");
		for (Map.Entry<String, Object> entry : src.entrySet()) {
			String filedName = entry.getKey();
			Object v = entry.getValue();

			ColumnMeta cm = pojoMeta.getByFieldName(filedName);
			if (cm == null) {
				if (this.isOn(DBFlag.FAIL_IF_PROPERTY_NOT_MAPPED)) {
					throw new SumkException(-7331234, filedName + "这个字段没有在java的pojo类中定义");
				}
				continue;
			}
			if (v == null) {
				joiner.item().append(cm.dbColumn).append(" IS NULL ");
				continue;
			}
			joiner.item().append(cm.dbColumn).append("=? ");
			paramters.add(v);
		}

		return joiner.toCharSequence();
	}

	protected static class Order {

		final String name;

		final boolean desc;

		public Order(String name, boolean desc) {
			this.name = name;
			this.desc = desc;
		}

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
