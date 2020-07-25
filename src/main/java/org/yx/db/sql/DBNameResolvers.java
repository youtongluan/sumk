package org.yx.db.sql;

import java.util.Objects;
import java.util.function.UnaryOperator;

import org.yx.util.StringUtil;

public class DBNameResolvers {

	private static UnaryOperator<String> columnNameResolver = StringUtil::camelToUnderline;

	private static UnaryOperator<String> tableNameResolver = columnNameResolver;
	private static UnaryOperator<String> cachePrefixResolver = tableName -> "{" + tableName + "}";

	public static UnaryOperator<String> getColumnNameResolver() {
		return columnNameResolver;
	}

	public static void setColumnNameResolver(UnaryOperator<String> columnNameResolver) {
		DBNameResolvers.columnNameResolver = Objects.requireNonNull(columnNameResolver);
	}

	public static UnaryOperator<String> getTableNameResolver() {
		return tableNameResolver;
	}

	public static void setTableNameResolver(UnaryOperator<String> tableNameResolver) {
		DBNameResolvers.tableNameResolver = Objects.requireNonNull(tableNameResolver);
	}

	public static UnaryOperator<String> getCachePrefixResolver() {
		return cachePrefixResolver;
	}

	public static void setCachePrefixResolver(UnaryOperator<String> cachePrefixResolver) {
		DBNameResolvers.cachePrefixResolver = Objects.requireNonNull(cachePrefixResolver);
	}

}
