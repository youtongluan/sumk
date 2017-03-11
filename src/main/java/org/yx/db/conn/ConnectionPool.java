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
import java.util.ArrayList;
import java.util.List;

import org.yx.db.DBType;
import org.yx.log.Log;
import org.yx.util.Assert;

public final class ConnectionPool implements AutoCloseable {

	private static ThreadLocal<List<ConnectionPool>> connectionHolder = new ThreadLocal<List<ConnectionPool>>() {

		@Override
		protected List<ConnectionPool> initialValue() {
			return new ArrayList<>(2);
		}

	};
	private String dbName;
	private DBType dbType;

	private Connection readConn;
	private Connection writeConn;

	public static ConnectionPool create(String dbName, DBType dbType) {
		List<ConnectionPool> list = connectionHolder.get();
		ConnectionPool context = new ConnectionPool();
		context.dbName = dbName;
		context.dbType = dbType;
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

	private ConnectionPool() {
	}

	public static ConnectionPool get() {
		List<ConnectionPool> list = connectionHolder.get();
		Assert.isTrue(list.size() > 0, "must open transaction in box or other containers");
		ConnectionPool context = list.get(0);
		return context;
	}

	public static void clossLeakConnection() {
		List<ConnectionPool> list = connectionHolder.get();
		if (list.isEmpty()) {
			connectionHolder.remove();
			return;
		}
		try {
			Log.get("DBConnectionContext").error("###connection leak:" + list.size());
			while (list.size() > 0) {
				list.get(0).close();
			}
			connectionHolder.remove();
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventLane.removeALL();
	}

	/**
	 * 获取其它类型的session<BR>
	 * 如果已经存在同一数据源的写连接，就返回那个写连接
	 * 
	 * @param type
	 *            建议类型，与真正的连接类型未必会一致
	 * @return
	 * @throws SQLException
	 */
	public Connection connection(DBType type) {
		if (this.dbType == DBType.WRITE) {
			return this.getWriteConnection();
		}
		if (this.dbType == DBType.READ) {
			if (type == DBType.WRITE) {
				return this.getWriteConnection();

			}
			return this.getReadConnection();
		}
		return this.connectionByUser(type);
	}

	private Connection connectionByUser(DBType type) {
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
		ConnectionFactory factory = ConnectionFactory.get(dbName);
		Connection conn = factory.getConnection(DBType.READ, this.writeConn);
		this.readConn = conn;
		Log.get("sumk.conn.open").trace("open read connection:{}", conn);
		return conn;
	}

	private Connection getWriteConnection() {
		if (this.writeConn != null) {
			return this.writeConn;
		}
		ConnectionFactory factory = ConnectionFactory.get(dbName);
		Connection conn = factory.getConnection(DBType.WRITE, null);
		this.writeConn = conn;
		Log.get("sumk.conn.open").trace("open write connection:{}", conn);
		return conn;
	}

	/**
	 * 返回默认类型的session
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getDefaultconnection() {
		return this.connectionByUser(this.dbType);
	}

	public void commit() throws SQLException {
		if (this.writeConn != null) {
			Log.get("sumk.conn").trace("commit {}", this.dbName);
			this.writeConn.commit();
		}
	}

	public void rollback() throws SQLException {
		if (this.writeConn != null) {
			Log.get("sumk.conn").trace("rollback {}", this.dbName);
			this.writeConn.rollback();
		}
	}

	@Override
	public void close() throws Exception {
		if (this.writeConn != null) {
			try {
				Log.get("sumk.conn.close").trace("close write connection:{}", this.writeConn);
				this.writeConn.close();
			} catch (Exception e) {
				Log.printStack(e);
			}
			this.writeConn = null;
		}
		if (this.readConn != null) {
			try {
				Log.get("sumk.conn.close").trace("close read connection:{}", this.readConn);
				this.readConn.close();
			} catch (Exception e) {
				Log.printStack(e);
			}
			this.readConn = null;
		}
		List<ConnectionPool> list = connectionHolder.get();
		int size = list.size();
		list.remove(this);
		Log.get("sumk.conn").trace("Close connection context [{}], from size {} to {}", this.dbName, size, list.size());
	}

	public String getDbName() {
		return dbName;
	}

	public DBType getDbType() {
		return dbType;
	}

}
