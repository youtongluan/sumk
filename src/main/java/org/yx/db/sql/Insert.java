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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.common.ItemJoiner;
import org.yx.db.event.InsertEvent;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.util.SeqUtil;

public class Insert extends AbstractSqlBuilder<Integer> {

	private List<Object> src = new ArrayList<>();

	public Insert(SumkDbVisitor<Integer> visitor) {
		super(visitor);
	}

	public int execute() {
		return this.accept(visitor);
	}

	/**
	 * 分表的情况下，设置分区名。这个方法只能调用一次
	 * 
	 * @param sub
	 *            分区名
	 * @return 当前对象
	 */
	public Insert partition(String sub) {
		sub(sub);
		return this;
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

	public MapedSql toMapedSql() throws Exception {
		this.checkIn();
		this.pojoMeta = this.parsePojoMeta(true);
		return batchInsert();
	}

	protected MapedSql batchInsert() throws Exception {
		MapedSql ms = new MapedSql();
		ItemJoiner columns = ItemJoiner.create(",", " ( ", " ) ");
		ItemJoiner placeholder = ItemJoiner.create(",", " ( ", " ) ");
		List<ColumnMeta> fms = pojoMeta.fieldMetas;
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
			this.fillSpecialColumns(pojoMap, src.get(i));

			for (ColumnMeta fm : fms) {
				Object value = fm.value(pojoMap);
				ms.addParam(value);
				map.put(fm.getFieldName(), value);
			}
			if (map.isEmpty()) {
				continue;
			}
			cacheList.add(map);
			if (pojoMeta.isSoftDelete()) {
				ms.addParam(pojoMeta.softDelete.validValue);
			}
		}

		ms.event = new InsertEvent(pojoMeta.getTableName(), cacheList);
		return ms;
	}

	protected void fillSpecialColumns(Map<String, Object> pojoMap, Object srcObject) throws Exception {
		List<ColumnMeta> idColumns = pojoMeta.getDatabaseIds();
		if (idColumns.size() != 1) {
			return;
		}
		ColumnMeta idColumn = idColumns.get(0);
		if (idColumn.field.getType() != Long.class) {
			return;
		}
		Long autoId = (Long) idColumn.value(pojoMap);
		if (autoId == null) {

			autoId = SeqUtil.next(pojoMeta.getTableName());
			idColumn.setValue(srcObject, autoId);
			idColumn.setValue(pojoMap, autoId);
		}
		List<ColumnMeta> createTimes = pojoMeta.createColumns();
		if (createTimes.size() > 0) {
			Date now = new Date(SeqUtil.getTimeMillis(autoId) / 1000 * 1000);
			for (ColumnMeta cTime : createTimes) {
				cTime.setValue(srcObject, now);
				Object time2 = cTime.value(srcObject);
				cTime.setValue(pojoMap, time2);
			}
		}
	}

}
