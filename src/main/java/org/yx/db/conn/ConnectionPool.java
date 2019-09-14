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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.yx.common.SumkLogs;
import org.yx.common.context.ActionContext;
import org.yx.db.DBType;
import org.yx.db.event.EventLane;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;

public final class ConnectionPool implements AutoCloseable {

	private static ThreadLocal<List<ConnectionPool>> connectionHolder = new ThreadLocal<List<ConnectionPool>>() {

		@Override
		protected List<ConnectionPool> initialValue() {
			return new ArrayList<>(2);
		}

	};
	private static final Logger LOG_CONN_OPEN = Log.get("sumk.conn.open");
	private static final Logger LOG_CONN_CLOSE = Log.get("sumk.conn.close");
	public static final Logger LOG_CONN = Log.get("sumk.conn");

	private final String dbName;

	private final DBType dbType;

	private Connection readConn;
	private Connection writeConn;

	public static ConnectionPool create(String dbName, DBType dbType) {
		if (ActionContext.get().isTest()) {
			if (connectionHolder.get().size() > 0) {
				return null;
			}
			dbType = DBType.WRITE;
		}
		List<ConnectionPool> list = connectionHolder.get();
		ConnectionPool context = new ConnectionPool(dbName, dbType);
		list.add(0, context);
		return context;
	}

	public static ConnectionPool createIfAbsent(String dbName, DBType dbType) {
		List<ConnectionPool> list = connectionHolder.get();
		if (list.size() > 0) {

			return null;
		}
		return create(dbName, dbType);
	}

	private ConnectionPool(String dbName, DBType dbType) {
		this.dbName = dbName;
		this.dbType = dbType;
	}

	public static ConnectionPool get() {
		List<ConnectionPool> list = connectionHolder.get();
		Assert.isTrue(list.size() > 0, "must open transaction in box or other containers");
		ConnectionPool context = list.get(0);
		return context;
	}

	public static int localPoolSize() {
		List<ConnectionPool> list = connectionHolder.get();
		return list.size();
	}

	public static void clossLeakConnection() {
		List<ConnectionPool> list = connectionHolder.get();
		if (list.isEmpty()) {
			connectionHolder.remove();
			return;
		}
		try {
			LOG_CONN.error("###connection leak:" + list.size());
			while (list.size() > 0) {
				list.get(0).close();
			}
			connectionHolder.remove();
		} catch (Exception e) {
			LOG_CONN.error(e.getMessage(), e);
		}
		EventLane.removeALL();
	}

	public Connection connection(DBType type) {
		if (ActionContext.get().isTest()) {
			return this.getWriteConnection();
		}
		switch (this.dbType) {
		case WRITE:
			return this.getWriteConnection();
		case READONLY:
			if (type == DBType.WRITE) {
				SumkException.throwException(5639234, "can not open write connection in readonly context");
			}
			return this.getReadConnection();
		case READ:
			if (type == DBType.WRITE) {
				return this.getWriteConnection();
			}
			return this.getReadConnection();
		default:
			return this.connectionByUser(type);
		}
	}

	private Connection connectionByUser(DBType type) {
		if (ActionContext.get().isTest()) {
			return this.getWriteConnection();
		}
		if (type == DBType.ANY) {
			if (this.readConn != null) {
				return this.readConn;
			}
			if (this.writeConn != null) {
				return this.writeConn;
			}
		}
		if (type == DBType.WRITE) {
			return this.getWriteConnection();
		}
		return this.getReadConnection();
	}

	private Connection getReadConnection() {
		if (this.readConn != null) {
			return this.readConn;
		}
		ConnectionFactory factory = ConnectionFactorys.get(dbName);
		Connection conn = factory.getConnection(DBType.READ, this.writeConn);
		this.readConn = conn;
		LOG_CONN_OPEN.trace("open read connection:{}", conn);
		return conn;
	}

	private Connection getWriteConnection() {
		if (this.writeConn != null) {
			return this.writeConn;
		}
		ConnectionFactory factory = ConnectionFactorys.get(dbName);
		Connection conn = factory.getConnection(DBType.WRITE, null);
		this.writeConn = conn;
		LOG_CONN_OPEN.trace("open write connection:{}", conn);
		return conn;
	}

	public Connection getDefaultConnection() {
		return this.connectionByUser(this.dbType);
	}

	public void commit() throws SQLException {
		if (this.writeConn != null) {
			LOG_CONN.trace("commit {}", this.dbName);
			this.writeConn.commit();
		}
	}

	public void rollback() throws SQLException {
		if (this.writeConn != null) {
			LOG_CONN.trace("rollback {}", this.dbName);
			this.writeConn.rollback();
		}
	}

	@Override
	public void close() throws Exception {
		if (this.writeConn != null) {
			try {
				LOG_CONN_CLOSE.trace("close write connection:{}", this.writeConn);
				this.writeConn.close();
			} catch (Exception e) {
				Log.printStack(SumkLogs.SQL_ERROR, e);
			}
			this.writeConn = null;
		}
		if (this.readConn != null) {
			try {
				LOG_CONN_CLOSE.trace("close read connection:{}", this.readConn);
				this.readConn.close();
			} catch (Exception e) {
				Log.printStack(SumkLogs.SQL_ERROR, e);
			}
			this.readConn = null;
		}
		List<ConnectionPool> list = connectionHolder.get();
		int size = list.size();
		list.remove(this);
		LOG_CONN.trace("Close connection context [{}], from size {} to {}", this.dbName, size, list.size());
	}

	public String getDbName() {
		return dbName;
	}

	public DBType getDbType() {
		return dbType;
	}

}
