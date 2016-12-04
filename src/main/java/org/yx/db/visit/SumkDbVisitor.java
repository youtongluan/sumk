package org.yx.db.visit;

import org.yx.db.sql.SqlBuilder;

public interface SumkDbVisitor<T> {

	T visit(SqlBuilder builder) throws Exception;
}
