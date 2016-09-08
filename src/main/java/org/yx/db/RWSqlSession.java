package org.yx.db;

import org.apache.ibatis.session.SqlSession;

public interface RWSqlSession extends SqlSession{
	/**
	 * 是否支持当前dbName的写操作
	 * @param dbName
	 */
	void acceptWrite(String dbName);
	/**
	 * 是否支持当前dbName的读操作
	 * @param dbName
	 */
	void acceptRead(String dbName);
	
	boolean isReadOnly();
	
	String getDbName();
}
