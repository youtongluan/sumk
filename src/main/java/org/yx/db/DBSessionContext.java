package org.yx.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.util.Assert;
import org.yx.log.Log;

public final class DBSessionContext implements AutoCloseable {

	private static ThreadLocal<List<DBSessionContext>> sessionHolder = new ThreadLocal<List<DBSessionContext>>() {

		@Override
		protected List<DBSessionContext> initialValue() {
			return new ArrayList<>(2);
		}

	};
	private String dbName;
	private DBType dbType;

	private SqlSession readSession;
	private SqlSession writeSession;

	/**
	 * 创建新的DBSessionContext
	 * 
	 * @param dbName
	 *            数据库名称
	 * @param dbType
	 *            读或者写.any会被当成读
	 */
	public static DBSessionContext create(String dbName, DBType dbType) {
		List<DBSessionContext> list = sessionHolder.get();
		DBSessionContext context = new DBSessionContext();
		context.dbName = dbName;
		context.dbType = dbType;
		list.add(0, context);
		return context;
	}

	public static DBSessionContext createIfAbsent(String dbName, DBType dbType) {
		List<DBSessionContext> list = sessionHolder.get();
		if (list.size() > 0) {
			return null;
		}
		return create(dbName, dbType);
	}

	private DBSessionContext() {
	}

	public static DBSessionContext get() {
		List<DBSessionContext> list = sessionHolder.get();
		Assert.isTrue(list.size() > 0, "DBSessionContext.create() must be call before get()");
		DBSessionContext context = list.get(0);
		return context;
	}

	public static void clossLeakSession() {
		List<DBSessionContext> list = sessionHolder.get();
		if (list.isEmpty()) {
			sessionHolder.remove();
			return;
		}
		try {
			Log.get("DBSessionContext").error("###session leak:" + list.size());
			while (list.size() > 0) {
				list.get(0).close();
			}
			sessionHolder.remove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取其它类型的session
	 * 
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public SqlSession session(DBType type) {
		if (this.dbType == DBType.WRITE) {
			return this.getWriteSession();
		}
		if (this.dbType == DBType.READONLY) {
			return this.getReadSession();
		}
		return this._session(type);
	}

	private SqlSession _session(DBType type) {
		if (type == DBType.ANY) {
			if (this.readSession != null) {
				return this.readSession;
			}
			if (this.writeSession != null) {
				return this.writeSession;
			}
		}
		if (type == DBType.WRITE) {
			return this.getWriteSession();
		}
		return this.getReadSession();
	}

	private SqlSession getReadSession() {
		if (this.readSession != null) {
			return this.readSession;
		}
		SqlSessionFactory factory = SqlSessionFactory.get(dbName);
		SqlSession session = factory.getSqlSession(DBType.READONLY);
		this.readSession = session;
		return session;
	}

	private SqlSession getWriteSession() {
		if (this.writeSession != null) {
			return this.writeSession;
		}
		SqlSessionFactory factory = SqlSessionFactory.get(dbName);
		SqlSession session = factory.getSqlSession(DBType.WRITE);
		this.writeSession = session;
		return session;
	}

	/**
	 * 返回默认类型的session
	 * 
	 * @return
	 * @throws SQLException
	 */
	public SqlSession session() {
		return this._session(this.dbType);
	}

	public void commit() {
		if (this.writeSession != null) {
			this.writeSession.commit();
		}
	}

	public void rollback() {
		if (this.writeSession != null) {
			this.writeSession.rollback();
		}
	}

	@Override
	public void close() throws Exception {
		if (this.writeSession != null) {
			try {
				this.writeSession.close();
			} catch (Exception e) {
				Log.printStack(e);
			}
			this.writeSession = null;
		}
		if (this.readSession != null) {
			try {
				this.readSession.close();
			} catch (Exception e) {
				Log.printStack(e);
			}
			this.readSession = null;
		}
		List<DBSessionContext> list = sessionHolder.get();
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
