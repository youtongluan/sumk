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

import org.yx.db.event.InsertEvent;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.util.SeqUtil;

public final class Insert extends AbstractSqlBuilder<Integer> {

	private List<Object> src = new ArrayList<>();

	public Insert(SumkDbVisitor<Integer> visitor) {
		super(visitor);
	}

	public int execute() {
		return this.accept(visitor);
	}

	/**
	 * @param pojo
	 *            要插入的对象，该对象不会被DB包修改。如果是Map类型，要设置tableClass<BR>
	 *            但如果对象中主键为null。而且表只有一个主键，并且是Long类型的话，会自动生成一个id，并且赋值到pojo中<br>
	 * @return 当前对象
	 */
	public Insert insert(Object pojo) {
		this.src.add(pojo);
		this._addIn(pojo);
		return this;
	}

	public Insert tableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
		return this;
	}

	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		this.checkIn();
		this.pojoMeta = this.parsePojoMeta(true);
		return this.in.size() == 1 ? singleInsert() : batchInsert();
	}

	private MapedSql singleInsert() throws InstantiationException, IllegalAccessException {
		Map<String, Object> pojoMap = this.in.get(0);
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(pojoMeta.getTableName());
		sb.append(" (");
		boolean notFirst = false;
		StringBuilder values = new StringBuilder();
		ColumnMeta[] fms = pojoMeta.fieldMetas;
		Map<String, Object> map = new HashMap<>();

		for (ColumnMeta fm : fms) {
			String name = fm.dbColumn;
			Object value = fm.value(pojoMap);
			if (value == null) {

				if (fm.isDBID() && canUseAutoID(pojoMeta)) {
					value = SeqUtil.next(pojoMeta.getTableName());
					fm.setValue(pojoMap, value);
					fm.setValue(src.get(0), value);
				} else {
					continue;
				}
			}
			if (notFirst) {
				sb.append(',');
				values.append(',');
			}
			notFirst = true;
			sb.append(name);
			ms.addParam(value);
			map.put(fm.getFieldName(), value);
			values.append('?');
		}
		if (pojoMeta.isSoftDelete()) {
			String columnName = pojoMeta.softDelete.columnName;
			sb.append(',').append(columnName);
			values.append(",?");
			ms.addParam(pojoMeta.softDelete.validValue);
		}
		sb.append(')');
		sb.append(" VALUES ");
		sb.append('(');
		sb.append(values);
		sb.append(')');
		ms.sql = sb.toString();
		InsertEvent event = new InsertEvent(pojoMeta.getTableName(), Collections.singletonList(map));
		ms.event = event;
		return ms;
	}

	private MapedSql batchInsert() throws InstantiationException, IllegalAccessException {
		MapedSql ms = new MapedSql();
		ItemJoiner columns = ItemJoiner.create(",", " ( ", " ) ");
		ItemJoiner placeholder = ItemJoiner.create(",", " ( ", " ) ");
		ColumnMeta[] fms = pojoMeta.fieldMetas;
		int recodeSize = in.size();
		for (ColumnMeta fm : fms) {
			String name = fm.dbColumn;
			columns.item().append(name);
			placeholder.item().append('?');
		}
		if (pojoMeta.isSoftDelete()) {
			String columnName = pojoMeta.softDelete.columnName;
			columns.item().append(columnName);
			placeholder.item().append('?');
		}
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(pojoMeta.getTableName()).append(columns.toCharSequence(true))
				.append(" VALUES ");
		CharSequence place = placeholder.toCharSequence(true);
		for (int i = 0; i < recodeSize; i++) {
			if (i > 0) {
				sql.append(',');
			}
			sql.append(place);
		}
		ms.sql = sql.toString();

		List<Map<String, Object>> cacheList = new ArrayList<>(recodeSize);
		for (int i = 0; i < recodeSize; i++) {
			Map<String, Object> pojoMap = this.in.get(i);
			Map<String, Object> map = new HashMap<>();
			cacheList.add(map);

			for (ColumnMeta fm : fms) {
				Object value = fm.value(pojoMap);
				if (value == null) {

					if (fm.isDBID() && canUseAutoID(pojoMeta)) {
						value = SeqUtil.next(pojoMeta.getTableName());
						fm.setValue(pojoMap, value);
						fm.setValue(src.get(i), value);
					}
				}
				ms.addParam(value);
				map.put(fm.getFieldName(), value);
			}
			if (pojoMeta.isSoftDelete()) {
				ms.addParam(pojoMeta.softDelete.validValue);
			}
		}

		InsertEvent event = new InsertEvent(pojoMeta.getTableName(), cacheList);
		ms.event = event;
		return ms;
	}

	private boolean canUseAutoID(PojoMeta pm) {
		ColumnMeta[] ids = pm.getPrimaryIDs();
		if (ids.length != 1) {
			return false;
		}
		Class<?> f = ids[0].field.getType();
		return Long.class == f;
	}

}
