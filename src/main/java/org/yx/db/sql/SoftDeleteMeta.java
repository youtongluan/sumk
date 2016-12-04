package org.yx.db.sql;

public class SoftDeleteMeta {
	final String columnName;
	final Object validValue;
	final Object inValidValue;
	final Class<?> columnType;

	public SoftDeleteMeta(String columnName, Object validValue, Object inValidValue, Class<?> columnType) {
		super();
		this.columnName = columnName;
		this.validValue = validValue;
		this.inValidValue = inValidValue;
		this.columnType = columnType;
	}

}
