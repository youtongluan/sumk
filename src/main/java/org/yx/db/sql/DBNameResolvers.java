package org.yx.db.sql;

import java.util.Objects;

import org.yx.util.StringUtil;

public class DBNameResolvers {

	private static DBNameResolver nameResolver = new DBColumnNameResolverImpl();

	public static DBNameResolver getResolver() {
		return nameResolver;
	}

	public static void setResolver(DBNameResolver resolver) {
		DBNameResolvers.nameResolver = Objects.requireNonNull(resolver, "resolver cannot be null");
	}

	private static class DBColumnNameResolverImpl implements DBNameResolver {

		@Override
		public String resolveColumnName(String javaFieldName) {
			return StringUtil.camelToUnderline(javaFieldName);
		}

		@Override
		public String resolveTableName(String simpleJavaName) {
			return StringUtil.camelToUnderline(simpleJavaName);
		}

	}
}
