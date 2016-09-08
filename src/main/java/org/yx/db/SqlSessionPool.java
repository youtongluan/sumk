package org.yx.db;

import org.apache.ibatis.session.SqlSession;

/**
 * 线程安全
 */
public interface SqlSessionPool {
	/**
	 * 获取该module所对应的写session
	 * @param db
	 * @return
	 */
	
	SqlSession writeSession(String db);
	
	void commit(String db);
	
	void rollback(String db);
	
	
	/**
	 * 获取该module的读session，它有可能是DBType.READONLY，也有可能是DBType.ANY。
	 * 关键是看实现类
	 * @param db
	 * @return
	 */
	SqlSession readSession(String db);
	
}
