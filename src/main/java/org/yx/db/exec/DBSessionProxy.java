package org.yx.db.exec;

import java.io.IOException;
import java.sql.SQLException;

import org.yx.db.DBAction;
import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;

public class DBSessionProxy implements DBAction {
	private DBType dbType;
	private ResultContainer container;
	private ConnectionPool dbCtx = null;

	public static DBSessionProxy create(ResultContainer container, DBType dbType) {
		return new DBSessionProxy(dbType, container);
	}

	public DBSessionProxy(DBType dbType, ResultContainer container) {
		super();
		this.dbType = dbType;
		this.container = container;
	}

	public void exec(DBExecutor executor) throws Exception {

		try {
			ExeContext context = new ExeContext();
			context.param = container.getParam();
			context.action = this;
			dbCtx = ConnectionPool.create(container.getDb(), dbType);
			executor.exec(context);
			dbCtx.commit();
			this.container.result = context.getResult();
		} catch (Exception e) {
			if (dbCtx != null) {
				dbCtx.rollback();
			}
			throw e;
		} finally {
			if (dbCtx != null) {
				dbCtx.close();
			}
		}
	}

	@Override
	public void commit() throws IOException, SQLException {
		dbCtx.commit();

	}

	@Override
	public void rollback() throws IOException, SQLException {
		dbCtx.rollback();
	}

}
