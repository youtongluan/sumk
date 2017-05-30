/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
