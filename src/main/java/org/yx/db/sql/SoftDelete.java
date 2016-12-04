package org.yx.db.sql;

import java.util.HashMap;
import java.util.Map;

import org.yx.db.event.DeleteEvent;
import org.yx.exception.SumkException;

class SoftDelete implements SqlBuilder {

	private Delete delete;

	public SoftDelete(Delete delete) {
		this.delete = delete;
	}

	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		Object pojo = delete.in.get(0);
		PojoMeta pm = delete.getPojoMeta();
		MapedSql ms = new MapedSql();
		StringBuilder sb = new StringBuilder();
		SoftDeleteMeta sm = pm.softDelete;
		if (sm == null) {
			SumkException.throwException(235435, pm.getTableName() + " can not be soft delete");
		}
		sb.append("UPDATE ").append(pm.getTableName()).append(" SET ").append(sm.columnName).append(" =? ");
		ms.addParam(pm.softDelete.inValidValue);
		StringBuilder where = new StringBuilder();
		for (ColumnMeta fm : pm.fieldMetas) {
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
		ms.event = new DeleteEvent(pm.getTableName(), map);
		return ms;
	}

}
