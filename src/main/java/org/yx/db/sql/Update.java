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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.conf.AppInfo;
import org.yx.db.annotation.ColumnType;
import org.yx.db.event.UpdateEvent;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;

public class Update extends AbstractSqlBuilder<Integer> {

	protected ColumnType _byType;
	protected Object _where;
	protected boolean _updateDBID = true;

	/**
	 * 只有在where条件不为null的时候，这个条件才能起作用
	 * 
	 * @param update
	 *            如果为false，则数据库主键不会被更新。默认为true。
	 * @return
	 */
	public Update updateDBID(boolean update) {
		this._updateDBID = update;
		return this;
	}

	/**
	 * 设置where条件，如果没有设置该条件。就用pojo的数据库主键或者redis主键<BR>
	 * 调用本方法后，byDBID和byCacheID方法将被忽略<BR>
	 * <B>注意：如果本表使用了缓存，本参数必须包含所有redis主键</B>
	 * 
	 * @param where
	 * @return
	 */
	public Update where(Object where) {
		this._where = where;
		return this;
	}

	public int execute() {
		return this.accept(visitor);
	}

	public Update(SumkDbVisitor<Integer> visitor) {
		super(visitor);
	}

	public Update fullUpdate() {
		this.withnull = true;
		return this;
	}

	protected ColumnType byType() {
		if (_byType != null) {
			return _byType;
		}
		return AppInfo.modifyByColumnType;
	}

	/**
	 * 默认是根据数据库主键更新
	 * 
	 * @return
	 */
	public Update byDBID() {
		this._byType = ColumnType.ID_DB;
		return this;
	}

	/**
	 * 根据缓存id更新数据。默认是根据数据库主键更新
	 * 
	 * @return
	 */
	public Update byCacheID() {
		this._byType = ColumnType.ID_CACHE;
		return this;
	}

	/**
	 * 设置对象类型的参数， <B>目前不支持批量</B>
	 * 
	 * @param pojo
	 *            Pojo或Map类型.如果是Map类型，要设置tableClass。
	 *            <B>如果本表使用了缓存，并且没有where条件，本参数必须包含所有redis主键</B>
	 * @return
	 */
	public Update update(Object pojo) {
		this._addIn(pojo);
		return this;
	}

	public Update tableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
		return this;
	}

	/**
	 * 
	 * @param pojo
	 *            只能是pojo对象，不能是map
	 * @param withnull
	 *            这个值为true，就会更新全部字段，否则只更新不为null的字段
	 * @param byType
	 *            ColumnType.ID_DB或ColumnType.ID_REDIS
	 * @return
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */

	public MapedSql toMapedSql() throws Exception {
		this.checkIn();
		Map<String, Object> pojo = this.in.get(0);
		this.pojoMeta = this.getPojoMeta();
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(pojoMeta.getTableName());
		ColumnMeta[] fms = pojoMeta.fieldMetas;
		ItemJoiner whereItem = new ItemJoiner(" AND ", "", "");
		ColumnType byType = byType();
		boolean notFirst = false;
		List<Object> whereParams = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		Map<String, Object> where = null;
		boolean updateDBID = this._updateDBID;
		if (updateDBID) {
			updateDBID = this._where != null;
		}
		if (this._where != null) {

			where = pojoMeta.populate(_where, false);
		}
		for (ColumnMeta fm : fms) {
			Object value = null;
			if (where != null) {
				if (where.containsKey(fm.getFieldName())) {
					value = where.remove(fm.getFieldName());
					whereItem.item().append(fm.getDbColumn()).append(" =? ");
					whereParams.add(value);
					paramMap.put(fm.getFieldName(), value);
				}
			} else if (fm.accept(byType)) {
				value = fm.value(pojo);
				if (value == null) {
					SumkException.throwException(234, fm.getFieldName() + " cannot be null");
				}
				whereItem.item().append(fm.getDbColumn()).append(" =? ");
				whereParams.add(value);
				paramMap.put(fm.getFieldName(), value);
				continue;
			}
			value = fm.value(pojo);
			if (value == null && !withnull) {
				continue;
			}
			if (fm.accept(ColumnType.ID_DB) && !updateDBID) {
				continue;
			}
			sb.append(notFirst ? " , " : " SET ");
			notFirst = true;
			sb.append(fm.getDbColumn()).append(" =? ");
			ms.addParam(value);
		}
		if (FAIL_IF_PROPERTY_NOT_MAPPED && where != null && where.size() > 0) {
			SumkException.throwException(234234, where.keySet() + " is not valid filed name");
		}
		CharSequence whereStr = whereItem.toCharSequence();
		if (whereStr == null || whereStr.length() == 0) {
			SumkException.throwException(345445, "where cannot be null");
		}
		sb.append(" WHERE ").append(whereStr);
		ms.addParams(whereParams);
		ms.sql = sb.toString();
		UpdateEvent event = new UpdateEvent(pojoMeta.getTableName(), pojoMeta.populate(pojo, false), paramMap,
				this.withnull);
		ms.event = event;
		return ms;
	}
}
