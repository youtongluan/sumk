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
		if (this.dbType == DBType.READONLY) {
			return this.getReadConnection();
		}
		return this._connection(type);
	}

	private Connection _connection(DBType type) {
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
		Connection conn = factory.getConnection(DBType.READONLY, this.writeConn);
		this.readConn = conn;
		return conn;
	}

	private Connection getWriteConnection() {
		if (this.writeConn != null) {
			return this.writeConn;
		}
		ConnectionFactory factory = ConnectionFactory.get(dbName);
		Connection session = factory.getConnection(DBType.WRITE, null);
		this.writeConn = session;
		return session;
	}

	/**
	 * 返回默认类型的session
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getDefaultconnection() {
		return this._connection(this.dbType);
	}

	public void commit() throws SQLException {
		if (this.writeConn != null) {
			this.writeConn.commit();
		}
	}

	public void rollback() throws SQLException {
		if (this.writeConn != null) {
			this.writeConn.rollback();
		}
	}

	@Override
	public void close() throws Exception {
		if (this.writeConn != null) {
			try {
				this.writeConn.close();
			} catch (Exception e) {
				Log.printStack(e);
			}
			this.writeConn = null;
		}
		if (this.readConn != null) {
			try {
				this.readConn.close();
			} catch (Exception e) {
				Log.printStack(e);
			}
			this.readConn = null;
		}
		List<ConnectionPool> list = connectionHolder.get();
		int size = list.size();
		list.remove(this);
		Log.get(this.getClass()).trace("Close session context, from size {} to {}", size, list.size());
	}

	public String getDbName() {
		return dbName;
	}

	public DBType getDbType() {
		return dbType;
	}

}
