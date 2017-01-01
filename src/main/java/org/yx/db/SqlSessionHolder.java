package org.yx.db;

import org.apache.ibatis.session.SqlSession;

public abstract class SqlSessionHolder {

	public static SqlSession writeSession(String module) {
		return org.yx.sumk.batis.SqlSessionHolder.session();
	}

	public static SqlSession readSession(String module) {
		return org.yx.sumk.batis.SqlSessionHolder.session();
	}

}
