package org.yx.db.conn;

import java.util.Set;

public interface DataSourceManagerSelector {

	DataSourceManager select(String dbName);

	/**
	 * 已经加载的数据源名称
	 * 
	 * @return 数据源名称列表，不为null
	 */
	Set<String> dbNames();
}
