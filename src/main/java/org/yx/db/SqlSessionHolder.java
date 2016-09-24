package org.yx.db;

import org.apache.ibatis.session.SqlSession;

public abstract class SqlSessionHolder {

	private static SqlSessionPool pool = new DefaultSqlSessionPool();

	public static SqlSessionPool getPool() {
		return pool;
	}

	public static void setPool(SqlSessionPool pool) {
		SqlSessionHolder.pool = pool;
	}

	public static void commit(String module) {
		pool.commit(module);
	}

	public static void rollback(String module) {
		pool.rollback(module);
	}

	public static SqlSession writeSession(String module) {
		return pool.writeSession(module);
	}

	public static SqlSession readSession(String module) {
		return pool.readSession(module);
	}

}
