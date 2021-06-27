package org.yx.db.sql;

import java.util.Objects;
import java.util.function.UnaryOperator;

import org.yx.conf.AppInfo;
import org.yx.util.StringUtil;

public class DBNameResolvers {

	private static final UnaryOperator<String> DEFAULT_RESOLVER = s -> {
		if (AppInfo.getBoolean("sumk.db.name.lowercase", false)) {
			return StringUtil.camelToUnderline(s).toLowerCase();
		}
		return StringUtil.camelToUnderline(s);
	};

	private static UnaryOperator<String> columnNameResolver = DEFAULT_RESOLVER;

	private static UnaryOperator<String> tableNameResolver = DEFAULT_RESOLVER;
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
