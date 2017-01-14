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
	 * @param pojo
	 *            要插入的对象，该对象不会被DB包修改。如果是Map类型，要设置tableClass
	 *            但如果对象中主键为null。而且表只有一个主键，并且是Long类型的话，会自动生成一个id，并且赋值到pojo中<br>
	 *            <B>目前不支持批量<B>
	 * @return
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

	/**
	 * 生成插入的sql，如果没有配置ORM注解，就返回null. 如果该表示软删除，就自动插入标志字段的有效值
	 * 
	 * @param pojo
	 * @param withnull
	 *            为true的话，那些是null的字段，也会展示在sql中
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		this.checkIn();
		Map<String, Object> pojoMap = this.in.get(0);
		this.pojoMeta = this.getPojoMeta();
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(pojoMeta.getTableName());
		sb.append(" (");
		boolean notFirst = false;
		String values = "";
		ColumnMeta[] fms = pojoMeta.fieldMetas;
		Map<String, Object> map = new HashMap<>();

		for (ColumnMeta fm : fms) {
			String name = fm.getDbColumn();
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
				sb.append(",");
				values += ",";
			}
			notFirst = true;
			sb.append(name);
			ms.addParam(value);
			map.put(fm.getFieldName(), value);
			values += "?";
		}
		if (pojoMeta.isSoftDelete()) {
			String columnName = pojoMeta.softDelete.columnName;
			sb.append(",").append(columnName);
			values += ",?";
			ms.addParam(pojoMeta.softDelete.validValue);
		}
		sb.append(")");
		sb.append(" VALUES ");
		sb.append("(");
		sb.append(values);
		sb.append(")");
		ms.sql = sb.toString();
		InsertEvent event = new InsertEvent(pojoMeta.getTableName(), map);
		ms.event = event;
		return ms;
	}

	private boolean canUseAutoID(PojoMeta pm) {
		ColumnMeta[] ids = pm.getPrimaryIDs();
		if (ids.length != 1) {
			return false;
		}
		Class<?> f = ids[0].getField().getType();
		return Long.class == f;
	}

}
