package org.yx.db.sql;

import java.util.HashMap;
import java.util.Map;

import org.yx.db.event.DeleteEvent;

class HardDelete implements SqlBuilder {

	private Delete delete;

	public HardDelete(Delete delete) {
		this.delete = delete;
	}

	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		Object pojo = delete.in.get(0);
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(delete.getPojoMeta().getTableName());
		StringBuilder where = new StringBuilder();
		for (ColumnMeta fm : delete.getPojoMeta().fieldMetas) {
			Object value = fm.value(pojo);
			if (value == null) {
				continue;
			}
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append(fm.getDbColumn()).append("=?");
			ms.addParam(value);
			map.put(fm.getFieldName(), value);
		}
		sb.append(" WHERE ").append(where.toString());
		ms.sql = sb.toString();
		ms.event = new DeleteEvent(delete.getPojoMeta().getTableName(), map);
		return ms;
	}

}
