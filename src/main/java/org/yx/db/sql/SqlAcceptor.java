package org.yx.db.sql;

import org.yx.db.visit.SumkDbVisitor;

/**
 * T是返回值的类型
 */
public interface SqlAcceptor<T> {

	T accept(SumkDbVisitor<T> visitor);
}
