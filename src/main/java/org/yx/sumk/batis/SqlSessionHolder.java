package org.yx.sumk.batis;

import org.apache.ibatis.session.SqlSession;

public abstract class SqlSessionHolder {

	public static SqlSession session() {
		return new ProxySession();
	}

}
