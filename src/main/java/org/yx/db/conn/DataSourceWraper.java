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

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.yx.db.DBType;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class DataSourceWraper extends BasicDataSource {

	public DataSourceWraper(String name, String type) {
		this.name = name;
		this.type = parseFromConfigFile(type);
		if (!this.type.isWritable()) {
			this.setDefaultReadOnly(true);
		}
	}

	private static DBType parseFromConfigFile(String type) {
		String type2 = type.toLowerCase();
		switch (type2) {
		case "w":
		case "write":
			return DBType.WRITE;
		case "r":
		case "read":
		case "readonly":
			return DBType.READ;
		case "wr":
		case "rw":
		case "any":
			return DBType.ANY;
		default:
			throw new SumkException(2342312, type + " is not correct db type");
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
				Log.get("sumk.db.ds").trace("use write connection for read");
				return c;
			}
		}
		return new ConnectionWrapper(super.getConnection(), this);
	}

}
