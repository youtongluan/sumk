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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.common.ItemJoiner;
import org.yx.db.event.UpdateEvent;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;
import org.yx.util.kit.TypeConverter;

public class Update extends ModifySqlBuilder {

	private Map<String, Object> updateTo;

	private Map<String, Number> incrMap;

	/**
	 * @param update
	 *            如果为false，则数据库主键不会被更新。默认为false。
	 * @return 当前对象
	 */
	public Update updateDBID(boolean update) {
		this.setOnOff(DBFlag.UPDATE_UPDATE_DBID, update);
		return this;
	}

	/**
	 * 
	 * @param onOff
	 *            如果为true，会验证map参数中，是否存在无效的key，预防开发人员将key写错。默认为true
	 * @return 当前对象
	 */
	public Update failIfPropertyNotMapped(boolean onOff) {
		this.setOnOff(DBFlag.FAIL_IF_PROPERTY_NOT_MAPPED, onOff);
		return this;
	}

	/**
	 * 设置where条件，如果没有设置该条件。就用pojo的数据库主键或者redis主键
	 * <UL>
	 * <LI>本方法可以被多次调用，多次调用之间是OR关系</LI>
	 * <LI><B>注意：如果本表使用了缓存，本参数必须包含所有redis主键</B></LI>
	 * <LI><B>注意：如果pojo是map类型，那么它的null值是有效条件</B></LI>
	 * </UL>
	 * 
	 * @param pojo
	 *            bean类型或Map.如果是pojo对象，其中的null字段会被忽略掉
	 * @return 当前对象
	 */

	public Update addWhere(Object pojo) {
		this._addIn(pojo);
		return this;
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
		this.setOnOff(DBFlag.UPDATE_FULL_UPDATE, fullUpdate);
		return this;
	}

	/**
	 * 分表的情况下，设置分区名。这个方法只能调用一次
	 * 
	 * @param sub
	 *            分区名
	 * @return 当前对象
	 */
	public Update partition(String sub) {
		sub(sub);
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
		if (pojo instanceof Map) {
			this.updateTo = new HashMap<>((Map<String, Object>) pojo);
			return this;
		}
		if (this.tableClass == null) {
			this.tableClass = pojo.getClass();
		}
		try {
			this.updateTo = this.makeSurePojoMeta().populate(pojo, false);
		} catch (Exception e) {
			throw new SumkException(-345461, e.getMessage(), e);
		}
		return this;
	}

	public Update tableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
		return this;
	}

	public MapedSql toMapedSql() throws Exception {
		if (this.updateTo == null) {
			this.updateTo = Collections.emptyMap();
		}
		if (this.updateTo.isEmpty() && CollectionUtil.isEmpty(this.incrMap)) {
			throw new SumkException(-3464601, "updateTo is null or empty");
		}

		this.checkMap(this.updateTo, this.pojoMeta);
		if (CollectionUtil.isEmpty(this.in)) {
			this.addDBIDs2Where();
		}
		return _toMapedSql();
	}

	protected MapedSql _toMapedSql() throws Exception {
		PojoMeta pojoMeta = this.pojoMeta;
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder(32);
		List<ColumnMeta> fms = pojoMeta.fieldMetas;
		SoftDeleteMeta softDelete = pojoMeta.softDelete;
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

			if (!fm.containsKey(this.updateTo) && !this.isOn(DBFlag.UPDATE_FULL_UPDATE)) {
				continue;
			}
			if (fm.isDBID() && !this.isOn(DBFlag.UPDATE_UPDATE_DBID)) {
				continue;
			}
			sb.append(notFirst ? " , " : " SET ");
			sb.append(fm.dbColumn).append("=? ");
			notFirst = true;
			ms.addParam(fm.value(this.updateTo));
		}

		ItemJoiner orItem = new ItemJoiner(" OR ", " WHERE ", null);
		boolean isSingle = in.size() == 1;
		for (Map<String, Object> where : this.in) {
			if (where.isEmpty()) {
				continue;
			}
			this.checkMap(where, pojoMeta);
			ItemJoiner andItem = isSingle ? new ItemJoiner(" AND ", null, null) : new ItemJoiner(" AND ", " ( ", " ) ");
			for (ColumnMeta fm : fms) {
				Object value = null;
				if (!where.containsKey(fm.getFieldName())) {
					continue;
				}
				value = where.get(fm.getFieldName());
				if (value == null) {
					andItem.item().append(fm.dbColumn).append(" IS NULL ");
					continue;
				}
				andItem.item().append(fm.dbColumn).append("=? ");
				ms.addParam(value);
			}
			if (andItem.isEmpty()) {
				continue;
			}
			if (softDelete != null) {
				if (softDelete.equalValid) {
					andItem.item().append(softDelete.columnName).append("=? ");
					ms.addParam(softDelete.validValue);
				} else {
					andItem.item().append(softDelete.columnName).append(" != ? ");
					ms.addParam(softDelete.inValidValue);
				}
			}
			CharSequence one = andItem.toCharSequence();
			orItem.item().append(one);
		}
		CharSequence whereStr = orItem.toCharSequence(true);
		if (whereStr == null || whereStr.length() == 0) {
			throw new SumkException(-7345445, "where cannot be null");
		}
		sb.append(whereStr);
		ms.sql = sb.toString();
		ms.event = new UpdateEvent(pojoMeta.getTableName(), to, this.incrMap, this.in,
				this.isOn(DBFlag.UPDATE_FULL_UPDATE), this.isOn(DBFlag.UPDATE_UPDATE_DBID));
		return ms;
	}

	protected void addDBIDs2Where() throws Exception {
		List<ColumnMeta> whereColumns = this.pojoMeta.getDatabaseIds();
		Map<String, Object> paramMap = new HashMap<>();
		for (ColumnMeta fm : whereColumns) {
			String fieldName = fm.getFieldName();
			paramMap.put(fieldName, fm.value(this.updateTo));
		}
		this._addInByMap(paramMap);
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
		PojoMeta pm = makeSurePojoMeta();
		ColumnMeta columnMeta = pm.getByFieldName(fieldName);
		if (columnMeta == null) {
			throw new SumkException(5912239, "cannot found java field " + fieldName + " in " + pm.pojoClz);
		}
		v = TypeConverter.toType(v, columnMeta.field.getType(), true);
		if (this.incrMap == null) {
			this.incrMap = new HashMap<>();
		}
		this.incrMap.put(fieldName, v);
		return this;
	}

}
