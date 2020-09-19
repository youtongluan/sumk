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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.yx.common.context.ActionContext;
import org.yx.db.enums.DBType;
import org.yx.db.event.EventLane;
import org.yx.exception.SimpleSumkException;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.kit.Asserts;

public final class ConnectionPool implements AutoCloseable {

	private static ThreadLocal<List<ConnectionPool>> connectionHolder = new ThreadLocal<List<ConnectionPool>>() {

		@Override
		protected List<ConnectionPool> initialValue() {
			return new ArrayList<>(2);
		}

	};
	private final Logger LOG_CONN_OPEN = Log.get("sumk.conn.open");
	private final Logger LOG_CONN = Log.get("sumk.conn");

	private final String dbName;

	private final DBType dbType;

	private final boolean autoCommit;

	private SumkConnection readConn;
	private SumkConnection writeConn;

	public static ConnectionPool create(String dbName, DBType dbType, boolean autoCommit) {
		if (ActionContext.get().isTest() && !autoCommit) {
			if (connectionHolder.get().size() > 0) {
				return null;
			}
			dbType = DBType.WRITE;
		}
		List<ConnectionPool> list = connectionHolder.get();
		ConnectionPool context = new ConnectionPool(dbName, dbType, autoCommit);
		list.add(0, context);
		return context;
	}

	public static ConnectionPool createIfAbsent(String dbName, DBType dbType) {
		List<ConnectionPool> list = connectionHolder.get();
		if (list.size() > 0) {

			return null;
		}
		return create(dbName, dbType, false);
	}

	private ConnectionPool(String dbName, DBType dbType, boolean autoCommit) {
		this.dbName = dbName;
		this.dbType = dbType;
		this.autoCommit = autoCommit;
	}

	public static ConnectionPool get() {
		List<ConnectionPool> list = connectionHolder.get();
		Asserts.requireTrue(list.size() > 0, "must open transaction in box or other containers");
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
			Log.get("sumk.conn").error("### connection leak:" + list.size());
			while (list.size() > 0) {
				list.get(0).close();
			}
			connectionHolder.remove();
		} catch (Exception e) {
			Log.get("sumk.conn").error(e.getMessage(), e);
		}
		EventLane.removeALL();
	}

	public SumkConnection connection(DBType userType) throws SQLException {
		if (ActionContext.get().isTest()) {
			return this.getWriteConnection();
		}
		switch (this.dbType) {
		case WRITE:
			if (userType == DBType.READONLY) {
				throw new SimpleSumkException(5639234, "can not open readOnly connection in write context");
			}
			return this.getWriteConnection();
		case READONLY:
			if (userType == DBType.WRITE) {
				throw new SimpleSumkException(5639234, "can not open write connection in readonly context");
			}
			return this.getReadConnection();
		case READ_PREFER:
			if (userType == DBType.WRITE) {
				return this.getWriteConnection();
			}
			return this.getReadConnection();
		default:
			return this.connectionByUserType(userType);
		}
	}

	private SumkConnection connectionByUserType(DBType type) throws SQLException {
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

	private SumkConnection getReadConnection() throws SQLException {
		if (this.readConn != null) {
			return this.readConn;
		}
		SumkDataSource dataSource = DataSources.readDataSource(dbName);
		if (dataSource == null) {
			throw new SumkException(124234154, dbName + "没有可用的读数据源");
		}
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

	private SumkConnection getWriteConnection() throws SQLException {
		if (this.writeConn != null) {
			return this.writeConn;
		}
		SumkDataSource dataSource = DataSources.writeDataSource(dbName);
		if (dataSource == null) {
			throw new SumkException(124234153, dbName + "没有可用的写数据源");
		}
		if (this.readConn != null && this.readConn.dataSource == dataSource) {
			this.writeConn = this.readConn.copy();
			if (LOG_CONN.isTraceEnabled()) {
				LOG_CONN.trace("{}读锁升级出写锁", this.dbName);
			}
		} else {
			this.writeConn = dataSource.getConnection();
			LOG_CONN_OPEN.trace("open write connection:{}", writeConn);
		}
		this.writeConn.setAutoCommit(this.autoCommit);
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
				Log.printStack("sumk.sql.error", e);
			}
		}
		if (this.readConn != null && !this.readConn.isSameInnerConnection(this.writeConn)) {
			try {
				this.readConn.close();
			} catch (Exception e) {
				Log.printStack("sumk.sql.error", e);
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
