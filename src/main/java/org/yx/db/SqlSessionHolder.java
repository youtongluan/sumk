package org.yx.db;

import org.apache.ibatis.session.SqlSession;
import org.yx.sumk.batis.ProxySession;

public abstract class SqlSessionHolder {

	public static SqlSession writeSession(String module) {
		return session();
	}

	public static SqlSession readSession(String module) {
		return session();
	}

	public static SqlSession session() {
		return new ProxySession();
	}

}
