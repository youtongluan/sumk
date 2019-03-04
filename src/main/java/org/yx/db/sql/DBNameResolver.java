package org.yx.db.sql;

public interface DBNameResolver {
	/**
	 * 根据java字段名，返回数据库的列名
	 * 
	 * @param javaFieldName
	 *            java的字段名，大小写敏感
	 * @return 数据库列名，一般为小写格式
	 */
	String resolveColumnName(String javaFieldName);

	/**
	 * 根据java类名，返回数据库的表名
	 * 
	 * @param simpleJavaName
	 *            java的类名，大小写敏感
	 * @return 数据库表名，一般为小写格式
	 */
	String resolveTableName(String simpleJavaName);
}
