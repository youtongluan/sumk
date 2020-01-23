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
import org.yx.common.context.ActionContext;
import org.yx.db.DBType;
import org.yx.db.event.EventLane;
import org.yx.exception.SimpleSumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.util.Asserts;

public final class ConnectionPool implements AutoCloseable {

	private static ThreadLocal<List<ConnectionPool>> connectionHolder = new ThreadLocal<List<ConnectionPool>>() {

		@Override
		protected List<ConnectionPool> initialValue() {
			return new ArrayList<>(2);
		}

	};
	private static final Logger LOG_CONN_OPEN = Log.get("sumk.conn.open");
	public static final Logger LOG_CONN = Log.get("sumk.conn");

	private final String dbName;

	private final DBType dbType;

	private SumkConnection readConn;
	private SumkConnection writeConn;

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
		Asserts.isTrue(list.size() > 0, "must open transaction in box or other containers");
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
			LOG_CONN.error("### connection leak:" + list.size());
			while (list.size() > 0) {
				list.get(0).close();
			}
			connectionHolder.remove();
		} catch (Exception e) {
			LOG_CONN.error(e.getMessage(), e);
		}
		EventLane.removeALL();
	}

	public Connection connection(DBType type) throws SQLException {
		if (ActionContext.get().isTest()) {
			return this.getWriteConnection();
		}
		switch (this.dbType) {
		case WRITE:
			return this.getWriteConnection();
		case READONLY:
			if (type == DBType.WRITE) {
				throw new SimpleSumkException(5639234, "can not open write connection in readonly context");
			}
			return this.getReadConnection();
		case READ:
			if (type == DBType.WRITE) {
				return this.getWriteConnection();
			}
			return this.getReadConnection();
		default:
			return this.connectionByRequireType(type);
		}
	}

	private Connection connectionByRequireType(DBType type) throws SQLException {
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

	private Connection getReadConnection() throws SQLException {
		if (this.readConn != null) {
			return this.readConn;
		}
		SumkDataSource dataSource = DataSources.readDataSource(dbName);
		if (this.writeConn != null && this.writeConn.dataSource == dataSource) {
			if (LOG_CONN.isTraceEnabled()) {
				LOG_CONN.trace("{}写锁分身出读锁", this.dbName);
			}
			this.readConn = this.writeConn.copy();
			return this.readConn;
		}
		this.readConn = dataSource.getConnection();
		LOG_CONN_OPEN.trace("open read connection:{}", readConn);
		return readConn;
	}

	private Connection getWriteConnection() throws SQLException {
		if (this.writeConn != null) {
			return this.writeConn;
		}
		SumkDataSource dataSource = DataSources.writeDataSource(dbName);
		if (this.readConn != null && this.readConn.dataSource == dataSource) {
			this.writeConn = this.readConn.copy();
			if (LOG_CONN.isTraceEnabled()) {
				LOG_CONN.trace("{}读锁升级出写锁", this.dbName);
			}
		} else {
			this.writeConn = dataSource.getConnection();
			LOG_CONN_OPEN.trace("open write connection:{}", writeConn);
		}
		this.writeConn.disableAutoCommit();
		return writeConn;
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
				this.writeConn.close();
			} catch (Exception e) {
				Logs.printStack(e);
			}
		}
		if (this.readConn != null && !this.readConn.isSameInnerConnection(this.writeConn)) {
			try {
				this.readConn.close();
			} catch (Exception e) {
				Logs.printStack(e);
			}
		}
		this.writeConn = null;
		this.readConn = null;
		List<ConnectionPool> list = connectionHolder.get();
		int size = list.size();
		list.remove(this);
		if (LOG_CONN.isTraceEnabled()) {
			LOG_CONN.trace("Close connection context [{}], from size {} to {}", this.dbName, size, list.size());
		}
	}

	public String getDbName() {
		return dbName;
	}

	public DBType getDbType() {
		return dbType;
	}

}
