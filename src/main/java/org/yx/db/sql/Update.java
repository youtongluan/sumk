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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.db.annotation.ColumnType;
import org.yx.db.event.UpdateEvent;
import org.yx.db.kit.NumUtil;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;

public final class Update extends AbstractSqlBuilder<Integer> implements Executable {

	private ColumnType _byType;

	private Map<String, Object> updateTo;
	private boolean _updateDBID = true;

	private Map<String, Number> incrMap;

	/**
	 * @param update
	 *            如果为false，则数据库主键不会被更新。默认为true。
	 * @return 当前对象
	 */
	public Update updateDBID(boolean update) {
		this._updateDBID = update;
		return this;
	}

	/**
	 * 
	 * @param fail
	 *            如果为true，会验证map参数中，是否存在无效的key，预防开发人员将key写错。默认为true
	 * @return 当前对象
	 */
	public Update failIfPropertyNotMapped(boolean fail) {
		this.failIfPropertyNotMapped = fail;
		return this;
	}

	/**
	 * 设置where条件，如果没有设置该条件。就用pojo的数据库主键或者redis主键
	 * <UL>
	 * <LI>调用本方法后，byDBID和byCacheID方法将被忽略</LI>
	 * <LI>本方法可以被多次调用，多次调用之间是OR关系</LI>
	 * <LI><B>注意：如果本表使用了缓存，本参数必须包含所有redis主键</B></LI>
	 * </UL>
	 * 
	 * @param pojo
	 *            bean类型或Map.如果是pojo对象，其中的null字段会被忽略掉
	 * @return 当前对象
	 */

	public Update addWhere(Object pojo) {
		this._addIn(pojo, false);
		return this;
	}

	@Override
	public int execute() {
		return this.accept(visitor);
	}

	public Update(SumkDbVisitor<Integer> visitor) {
		super(visitor);
	}

	/**
	 * @param fullUpdate
	 *            设置为true的话，整条记录全部更新，包括null字段。默认为false
	 * @return 当前对象
	 */
	public Update fullUpdate(boolean fullUpdate) {
		this.withnull = fullUpdate;
		return this;
	}

	private ColumnType byType() {
		if (_byType != null) {
			return _byType;
		}
		return OrmSettings.modifyByColumnType;
	}

	/**
	 * 默认是根据数据库主键更新
	 * 
	 * @return 当前对象
	 */
	public Update byDBID() {
		this._byType = ColumnType.ID_DB;
		return this;
	}

	/**
	 * 根据缓存id更新数据。默认是根据数据库主键更新
	 * 
	 * @return 当前对象
	 */
	public Update byCacheID() {
		this._byType = ColumnType.ID_CACHE;
		return this;
	}

	/**
	 * 记录被更新后的最终状态。
	 * <UL>
	 * <LI>有可能是部分字段，有可能是全部字段</LI>
	 * <LI>有可能只是单条记录变成这样，有可能是多条记录变成这样</LI>
	 * </UL>
	 * 
	 * @param pojo
	 *            Pojo或Map类型.如果是Map类型，要设置tableClass。
	 *            <B>如果本表使用了缓存，并且没有where条件，本参数必须包含所有redis主键</B>
	 *            <B>如果本字段包含在自增长里面，那它将会被排除掉</B>
	 * @return 当前对象
	 */
	@SuppressWarnings("unchecked")
	public Update updateTo(Object pojo) {
		if (Map.class.isInstance(pojo)) {
			this.updateTo = new HashMap<>((Map<String, Object>) pojo);
			return this;
		}
		this.pojoMeta = PojoMetaHolder.getPojoMeta(pojo.getClass());
		if (this.pojoMeta == null) {
			SumkException.throwException(36541, pojo.getClass() + " does not config as a table");
		}
		try {
			this.updateTo = this.pojoMeta.populate(pojo, withnull);
		} catch (Exception e) {
			SumkException.throwException(-345461, e.getMessage(), e);
		}
		return this;
	}

	public Update tableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
		return this;
	}

	public MapedSql toMapedSql() throws Exception {
		if (this.updateTo == null || this.updateTo.isEmpty()) {
			SumkException.throwException(-34601, "updateTo is null or empty");
		}
		this.pojoMeta = this.parsePojoMeta(true);
		this.checkMap(this.updateTo, this.pojoMeta);
		if (CollectionUtil.isEmpty(this.in)) {
			return this.toMapedSqlWithoutWhere();
		}
		return this.toMapedSqlWithWhere();
	}

	private MapedSql toMapedSqlWithWhere() throws Exception {
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder();
		ColumnMeta[] fms = pojoMeta.fieldMetas;
		sb.append("UPDATE ").append(pojoMeta.getTableName());
		boolean notFirst = false;
		Map<String, Object> to = new HashMap<>(this.updateTo);

		for (ColumnMeta fm : fms) {
			String fieldName = fm.getFieldName();

			if (this.incrMap != null && this.incrMap.containsKey(fieldName)) {
				to.remove(fieldName);
				sb.append(notFirst ? " , " : " SET ").append(fm.dbColumn).append('=').append(fm.dbColumn)
						.append(" +? ");
				notFirst = true;
				ms.addParam(this.incrMap.get(fieldName));
				continue;
			}
			Object value = fm.value(this.updateTo);
			if (value == null && !withnull) {
				continue;
			}
			if (fm.accept(ColumnType.ID_DB) && !this._updateDBID) {
				continue;
			}
			sb.append(notFirst ? " , " : " SET ");
			sb.append(fm.dbColumn).append("=? ");
			notFirst = true;
			ms.addParam(value);
		}

		ItemJoiner orItem = new ItemJoiner(" OR ", " WHERE ", null);

		for (Map<String, Object> where : this.in) {
			this.checkMap(where, this.pojoMeta);
			ItemJoiner andItem = new ItemJoiner(" AND ", " ( ", " ) ");
			for (ColumnMeta fm : fms) {
				Object value = null;
				if (where.containsKey(fm.getFieldName())) {
					value = where.get(fm.getFieldName());
					andItem.item().append(fm.dbColumn).append("=? ");
					ms.addParam(value);
				}
			}
			orItem.item().append(andItem.toCharSequence());
		}
		CharSequence whereStr = orItem.toCharSequence(true);
		if (whereStr == null || whereStr.length() == 0) {
			SumkException.throwException(345445, "where cannot be null");
		}
		sb.append(whereStr);
		ms.sql = sb.toString();
		UpdateEvent event = new UpdateEvent(pojoMeta.getTableName(), to, this.incrMap, this.in, this.withnull,
				this._updateDBID);
		ms.event = event;
		return ms;
	}

	private MapedSql toMapedSqlWithoutWhere() throws Exception {
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(pojoMeta.getTableName());
		ColumnMeta[] fms = pojoMeta.fieldMetas;
		ItemJoiner whereItem = new ItemJoiner(" AND ", " WHERE ", null);
		ColumnType byType = byType();
		boolean notFirst = false;
		List<Object> whereParams = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		Map<String, Object> to = new HashMap<>(this.updateTo);
		for (ColumnMeta fm : fms) {
			String fieldName = fm.getFieldName();
			Object value = null;
			if (fm.accept(byType)) {
				value = fm.value(this.updateTo);
				if (value == null) {
					SumkException.throwException(234, fieldName + " cannot be null");
				}
				whereItem.item().append(fm.dbColumn).append("=? ");
				whereParams.add(value);
				paramMap.put(fieldName, value);
				continue;
			}

			if (this.incrMap != null && this.incrMap.containsKey(fieldName)) {
				to.remove(fieldName);
				sb.append(notFirst ? " , " : " SET ").append(fm.dbColumn).append('=').append(fm.dbColumn)
						.append(" +? ");
				notFirst = true;
				ms.addParam(this.incrMap.get(fieldName));
				continue;
			}
			value = fm.value(this.updateTo);
			if (value == null && !withnull) {
				continue;
			}
			if (fm.accept(ColumnType.ID_DB) && !this._updateDBID) {
				continue;
			}
			sb.append(notFirst ? " , " : " SET ");
			sb.append(fm.dbColumn).append("=? ");
			notFirst = true;
			ms.addParam(value);
		}
		CharSequence whereStr = whereItem.toCharSequence(true);
		if (whereStr == null || whereStr.length() == 0) {
			SumkException.throwException(-345445, "where cannot be null");
		}
		sb.append(whereStr);
		ms.addParams(whereParams);
		ms.sql = sb.toString();
		UpdateEvent event = new UpdateEvent(pojoMeta.getTableName(), to, this.incrMap,
				Collections.singletonList(paramMap), this.withnull, this._updateDBID);
		ms.event = event;
		return ms;
	}

	/**
	 * 增加或减少表中数字类型字段的值
	 * 
	 * @param fieldName
	 *            需要增长或减少的字段的名字,不能是redis主键
	 * @param v
	 *            如果是减少，直接用负数就行了
	 * @return 当前对象
	 */
	public Update incrNum(String fieldName, Number v) {
		if (v == null) {
			throw new SumkException(5349238, "cannot incr " + fieldName + "(java) by null");
		}
		PojoMeta pm = this.parsePojoMeta(true);
		ColumnMeta columnMeta = pm.getByFieldName(fieldName);
		if (columnMeta == null) {
			throw new SumkException(5912239, "cannot found java field " + fieldName + " in " + pm.pojoClz);
		}
		v = NumUtil.toType(v, columnMeta.field.getType(), true);
		if (this.incrMap == null) {
			this.incrMap = new HashMap<>();
		}
		this.incrMap.put(fieldName, v);
		return this;
	}

}
