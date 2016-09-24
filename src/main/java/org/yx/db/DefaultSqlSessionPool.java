package org.yx.db;

import org.apache.ibatis.session.SqlSession;

public class DefaultSqlSessionPool implements SqlSessionPool {

	@Override
	public SqlSession writeSession(String module) {

		return DBSessionContext.get().session(DBType.WRITE);
	}

	@Override
	public SqlSession readSession(String module) {
		return DBSessionContext.get().session(DBType.READONLY);
	}

	@Override
	public void commit(String module) {
		DBSessionContext.get().commit();
	}

	@Override
	public void rollback(String module) {
		DBSessionContext.get().rollback();
	}

}
