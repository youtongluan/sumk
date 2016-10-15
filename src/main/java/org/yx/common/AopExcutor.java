package org.yx.common;

import org.yx.db.DBSessionContext;
import org.yx.db.DBType;
import org.yx.log.Log;

/**
 * 用于解析反射的上下文，比如数据库连接的创建与销毁.
 * 它封装的是与数据源（包括db、redis、mongo等）的连接关系，并不关心是SOA方式，还是http方式
 * 
 * @author youtl
 *
 */
public class AopExcutor {

	private DBSessionContext dbCtx = null;
	private boolean embed;

	public AopExcutor(boolean embed) {
		super();
		this.embed = embed;
	}

	public void begin(String dbName, DBType dbType) {
		Log.get(AopExcutor.class).trace("begin with embed:{}", embed);

		dbCtx = embed ? DBSessionContext.createIfAbsent(dbName, dbType) : DBSessionContext.create(dbName, dbType);
	}

	public void rollback(Throwable e) {
		Log.printStack(e);
		if (dbCtx != null) {
			Log.get(AopExcutor.class).trace("rollback {}", dbCtx.getDbName());
			dbCtx.rollback();
		}
		if (RuntimeException.class.isInstance(e)) {
			throw (RuntimeException) e;
		}
		throw new RuntimeException(e);
	}

	public void commit() {
		if (dbCtx == null) {
			return;
		}
		Log.get(AopExcutor.class).trace("commit {}", dbCtx.getDbName());
		this.dbCtx.commit();
	}

	public void close() {

		if (dbCtx == null) {
			return;
		}
		Log.get(AopExcutor.class).trace("close {}", dbCtx.getDbName());
		try {
			dbCtx.close();
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
