package org.yx.db;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceWraper extends BasicDataSource {

	public DataSourceWraper(String name, String type) {
		this.name = name;
		this.type = DBType.parse(type);
		if (!this.type.isWritable()) {
			this.setDefaultReadOnly(true);
		}
	}

	private String name;
	private DBType type;

	public String getName() {
		return name;
	}

	public DBType getType() {
		return type;
	}

}
