package org.yx.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.yx.db.event.DBEvent;

public class MapedSql {

	public MapedSql() {
		super();
		this.paramters = new ArrayList<>();
	}

	String sql;
	private List<Object> paramters;
	DBEvent event;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParamters() {
		return paramters;
	}

	public void addParam(Object p) {
		this.paramters.add(p);
	}

	public void addParams(List<Object> ps) {
		if (ps == null || ps.isEmpty()) {
			return;
		}
		this.paramters.addAll(ps);
	}

	/**
	 * 这个值有可能为null，这是为了性能考虑
	 * 
	 * @return
	 */
	public DBEvent getEvent() {
		return event;
	}

	public void setEvent(DBEvent event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return sql + " -- " + paramters;
	}

}
