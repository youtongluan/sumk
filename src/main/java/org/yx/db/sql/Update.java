package org.yx.db.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.conf.AppInfo;
import org.yx.db.dao.ColumnType;
import org.yx.db.event.UpdateEvent;
import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;

public class Update extends AbstractSqlBuilder<Integer> {

	ColumnType _byType;

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

	public Update byCacheID() {
		this._byType = ColumnType.ID_CACHE;
		return this;
	}

	/**
	 * 设置对象类型的参数 <B>目前不支持批量<B>
	 * 
	 * @param pojo
	 *            Pojo或Map类型.如果是Map类型，要设置tableClass
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
		String where = "";
		ColumnType byType = byType();
		boolean notFirst = false;
		List<Object> whereParams = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		for (ColumnMeta fm : fms) {
			Object value = fm.value(pojo);
			if (fm.accept(byType)) {
				if (value == null) {
					SumkException.throwException(234, fm.getFieldName() + " cannot be null");
				}
				if (where.length() > 0) {
					where += " AND ";
				}
				where += fm.getDbColumn() + " =? ";
				whereParams.add(value);
				map.put(fm.getFieldName(), value);
				continue;
			}
			if (value == null && !withnull) {
				continue;
			}
			sb.append(notFirst ? " , " : " SET ");
			notFirst = true;
			sb.append(fm.getDbColumn()).append(" =? ");
			ms.addParam(value);
			map.put(fm.getFieldName(), value);
		}
		if (where.isEmpty()) {
			SumkException.throwException(345445, "where cannot be null");
		}
		sb.append(" WHERE ").append(where);
		ms.addParams(whereParams);
		ms.sql = sb.toString();
		UpdateEvent event = new UpdateEvent(pojoMeta.getTableName(), map, this.withnull);
		ms.event = event;
		return ms;
	}
}
