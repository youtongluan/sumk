/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.db.conn;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.yx.db.DBType;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class SumkDataSource implements DataSource {
	private final String name;
	private final DBType type;
	private final DataSource proxy;

	public SumkDataSource(String name, DBType type, DataSource ds) {
		this.name = Objects.requireNonNull(name);
		this.type = Objects.requireNonNull(type);
		this.proxy = Objects.requireNonNull(ds);
	}

	public String getName() {
		return name;
	}

	public DBType getType() {
		return type;
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection conn = new ConnectionWrapper(proxy.getConnection(), this);
		if (!type.isWritable()) {
			conn.setReadOnly(true);
		}
		return conn;
	}

	public Connection readConnection(Connection write) throws SQLException {
		if (write != null && ConnectionWrapper.class.isInstance(write)
				&& ((ConnectionWrapper) write).getDataSource() == this) {
			Connection c = SlaveConnectionWrapper.create((ConnectionWrapper) write);
			if (c != null) {
				Log.get("sumk.db.ds").trace("use write connection for read");
				return c;
			}
		}
		return new ConnectionWrapper(proxy.getConnection(), this);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return proxy.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		proxy.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		proxy.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return proxy.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return proxy.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface == this.getClass()) {
			return iface.cast(this);
		}
		if (iface.isInstance(proxy)) {
			return iface.cast(proxy);
		}
		throw new SumkException(-234345, this.getClass().getSimpleName() + " does not wrap " + iface.getName());
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface == this.getClass() || iface.isInstance(proxy);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		Connection conn = proxy.getConnection(username, password);
		if (!type.isWritable()) {
			conn.setReadOnly(true);
		}
		return conn;
	}

}
