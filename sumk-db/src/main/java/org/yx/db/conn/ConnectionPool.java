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
import java.util.Objects;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.yx.base.context.ActionContext;
import org.yx.common.util.kit.Asserts;
import org.yx.db.enums.DBType;
import org.yx.db.enums.TxHook;
import org.yx.db.event.DBEvent;
import org.yx.db.event.EventLane;
import org.yx.db.sql.PojoMeta;
import org.yx.exception.SimpleSumkException;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public final class ConnectionPool implements AutoCloseable {

	private static ThreadLocal<List<ConnectionPool>> connectionHolder = new ThreadLocal<List<ConnectionPool>>() {

		@Override
		protected List<ConnectionPool> initialValue() {
			return new ArrayList<>(2);
		}

	};
	private static final Logger LOGGER = Log.get("sumk.conn");

	private final String dbName;

	private final DBType dbType;

	private final boolean autoCommit;

	private SumkConnection readConn;
	private SumkConnection writeConn;
	private List<SqlSessionHook> hooks;
	private final EventLane eventLane;

	public void addHook(TxHook type, Consumer<HookContext> r) {
		SqlSessionHook act = new SqlSessionHook(type, r);
		if (hooks == null) {
			hooks = new ArrayList<>();
		}
		if (hooks.contains(act)) {
			return;
		}
		hooks.add(act);
	}

	public static ConnectionPool create(String dbName, DBType dbType, boolean autoCommit) {
		if (ActionContext.current().isTest() && !autoCommit) {
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
		if (list.size() > 0 && list.get(0).getDbName().equals(dbName)) {

			return null;
		}
		return create(dbName, dbType, false);
	}

	private ConnectionPool(String dbName, DBType dbType, boolean autoCommit) {
		this.dbName = Objects.requireNonNull(dbName);
		this.dbType = Objects.requireNonNull(dbType);
		this.autoCommit = autoCommit;
		this.eventLane = new EventLane();
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
			LOGGER.error("### connection leak:" + list.size());
			while (list.size() > 0) {
				list.get(0).close();
			}
			connectionHolder.remove();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public SumkConnection connection(DBType userType) throws SQLException {
		if (ActionContext.current().isTest()) {
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
			return this.connectionByCommand(userType);
		}
	}

	private SumkConnection connectionByCommand(DBType type) throws SQLException {
		if (type == DBType.WRITE) {
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
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("{}写锁分身出读锁", this.dbName);
			}
			this.readConn = this.writeConn.copy();
			return this.readConn;
		}
		this.readConn = dataSource.getConnection();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("open read connection:{}", readConn);
		}
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
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("{}读锁升级出写锁", this.dbName);
			}
		} else {
			this.writeConn = dataSource.getConnection();
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("open write connection:{}", writeConn);
			}
		}
		this.writeConn.setAutoCommit(this.autoCommit);
		return writeConn;
	}

	private SumkDataSource getDataSource(SumkConnection c) {
		if (c == null) {
			return null;
		}
		return c.dataSource;
	}

	private void runHook(TxHook type, Throwable exception) {
		HookContext c = null;
		for (SqlSessionHook act : this.hooks) {
			if (act.getType() == type) {
				if (c == null) {
					c = new HookContext(getDataSource(this.writeConn), getDataSource(this.readConn), exception);
				}
				act.getAction().accept(c);
			}
		}
	}

	public void commit() throws SQLException {
		if (this.hooks != null) {
			runHook(TxHook.ON_COMMIT, null);
		}
		if (this.writeConn != null) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("commit {}", this.dbName);
			}
			this.eventLane.commit(this.writeConn);
		}
		if (this.hooks != null) {
			runHook(TxHook.COMMITED, null);
		}
	}

	public void rollback(Throwable e) throws SQLException {
		eventLane.clear();
		if (this.writeConn != null) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("rollback {}", this.dbName);
			}
			this.writeConn.rollback();
		}
		if (this.hooks != null) {
			runHook(TxHook.ROLLBACK, e);
		}
	}

	@Override
	public void close() throws Exception {
		eventLane.clear();
		if (this.writeConn != null) {
			try {
				this.writeConn.close();
			} catch (Exception e) {
				Log.printStack("sumk.sql.error", e);
			}
		}
		if (this.readConn != null) {
			try {
				this.readConn.close();
			} catch (Exception e) {
				Log.printStack("sumk.sql.error", e);
			}
		}
		if (this.hooks != null) {
			runHook(TxHook.CLOSED, null);
		}
		this.writeConn = null;
		this.readConn = null;
		List<ConnectionPool> list = connectionHolder.get();
		int size = list.size();
		list.remove(this);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Close connection context [{}], from size {} to {}", this.dbName, size, list.size());
		}
	}

	public String getDbName() {
		return dbName;
	}

	public DBType getDbType() {
		return dbType;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void pubuishModify(DBEvent event) {
		if (this.writeConn != null) {
			this.eventLane.pubuishModify(this.writeConn, event);
		}
	}

	public boolean existModifyEvent(PojoMeta pm) {
		return this.eventLane.existModifyEvent(pm.getTableName());
	}
}
