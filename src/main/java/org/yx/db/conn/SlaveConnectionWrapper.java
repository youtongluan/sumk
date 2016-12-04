package org.yx.db.conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import javax.sql.DataSource;

import org.yx.conf.AppInfo;
import org.yx.util.StringUtils;

public class SlaveConnectionWrapper extends ConnectionWrapper {

	public static SlaveConnectionWrapper create(ConnectionWrapper write) throws SQLException {
		if (write.isClosed()) {
			return null;
		}
		return new SlaveConnectionWrapper(write, write.getDataSource());
	}

	public SlaveConnectionWrapper(Connection write, DataSource ds) {
		super(write, ds);
	}

	@Override
	public void commit() throws SQLException {
		if (StringUtils.isEmpty(AppInfo.get("sumk.db"))) {
			throw new SQLException("readonly,cannot commit");
		}
	}

	@Override
	public void rollback() throws SQLException {
	}

	@Override
	public void close() throws SQLException {

		super.close();
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
	}

}
