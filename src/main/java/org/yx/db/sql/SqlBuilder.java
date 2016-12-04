package org.yx.db.sql;

/**
 * T是返回值的类型
 */
public interface SqlBuilder {

	/**
	 * 有些数据库的驱动，PreparedStatement.setObject()，如果值为null，会抛异常。要用setNULL(index,type
	 * )来设置null<BR>
	 * mysql和oarcle驱动没有这个问题
	 */
	MapedSql toMapedSql() throws Exception;

}
