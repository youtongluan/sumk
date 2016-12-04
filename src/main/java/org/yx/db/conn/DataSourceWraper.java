package org.yx.db.conn;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.yx.db.DBType;
import org.yx.log.Log;

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

	@Override
	public Connection getConnection() throws SQLException {
		return new ConnectionWrapper(super.getConnection(), this);
	}

	public Connection readConnection(Connection write) throws SQLException {
		if (write != null && ConnectionWrapper.class.isInstance(write)
				&& ((ConnectionWrapper) write).getDataSource() == this) {
			Connection c = SlaveConnectionWrapper.create((ConnectionWrapper) write);
			if (c != null) {
				Log.get("db").trace("use write connection for read");
				return c;
			}
		}
		return new ConnectionWrapper(super.getConnection(), this);
	}

}
