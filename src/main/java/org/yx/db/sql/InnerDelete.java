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

import java.util.Map;

import org.yx.common.ItemJoiner;
import org.yx.db.event.DeleteEvent;

abstract class InnerDelete {
	protected Delete delete;

	public InnerDelete(Delete delete) {
		this.delete = delete;
	}

	protected MapedSql toMapedSql(StringBuilder sb, MapedSql ms) throws InstantiationException, IllegalAccessException {
		PojoMeta pojoMeta = delete.parsePojoMeta(true);
		ItemJoiner orItem = new ItemJoiner(" OR ", " WHERE ", null);
		for (Map<String, Object> oneWhere : delete.in) {
			delete.checkMap(oneWhere, pojoMeta);
			ItemJoiner andItem = new ItemJoiner(" AND ", " ( ", " ) ");
			for (ColumnMeta fm : pojoMeta.fieldMetas) {
				Object value = fm.value(oneWhere);
				if (value == null) {
					continue;
				}
				andItem.item().append(fm.dbColumn).append("=?");
				ms.addParam(value);
			}
			orItem.item().append(andItem.toCharSequence());
		}

		sb.append(orItem.toCharSequence(true));
		ms.sql = sb.toString();
		ms.event = new DeleteEvent(pojoMeta.getTableName(), delete.in);
		return ms;
	}
}

class HardDelete extends InnerDelete implements SqlBuilder {

	public HardDelete(Delete delete) {
		super(delete);
	}

	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(delete.parsePojoMeta(true).getTableName());
		return this.toMapedSql(sb, new MapedSql());
	}

}

class SoftDelete extends InnerDelete implements SqlBuilder {

	public SoftDelete(Delete delete) {
		super(delete);
	}

	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		StringBuilder sb = new StringBuilder();
		PojoMeta pojoMeta = delete.parsePojoMeta(true);
		SoftDeleteMeta sm = pojoMeta.softDelete;
		sb.append("UPDATE ").append(pojoMeta.getTableName()).append(" SET ").append(sm.columnName).append(" =? ");
		MapedSql ms = new MapedSql();
		ms.addParam(sm.inValidValue);
		return this.toMapedSql(sb, ms);
	}

}
