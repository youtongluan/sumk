package org.yx.db.sql;

public interface DBNameResolver {

	String resolveColumnName(String javaFieldName);

	String resolveTableName(String simpleJavaName);
}
