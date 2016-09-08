package org.yx.db;

import org.apache.ibatis.session.SqlSession;
import org.springframework.util.Assert;
import org.yx.log.Log;



public final class DBSessionContext implements AutoCloseable{
	
	private static ThreadLocal<DBSessionContext> sessionHolder=new ThreadLocal<DBSessionContext>();
	private String dbName;
	private DBType dbType;
	
	private SqlSession readSession;
	private SqlSession writeSession;
	
	
	public static DBSessionContext create(String dbName,DBType dbType){
		DBSessionContext context= sessionHolder.get();
		if(context!=null){
			return context;
		}
		context=new DBSessionContext();
		context.dbName=dbName;
		context.dbType=dbType;
		sessionHolder.set(context);
		return context;
	}
	
	private DBSessionContext(){
	}
	
	public static DBSessionContext get(){
		DBSessionContext context= sessionHolder.get();
		Assert.notNull(context,"DBSessionContext.create() must be call before get()");
		return context;
	}
	
	public SqlSession session(DBType type) {
		if(this.dbType==DBType.WRITE){
			return this.getWriteSession();
		}
		if(this.dbType==DBType.READONLY){
			return this.getReadSession();
		}
		return this._session(type);
	}
	
	private SqlSession _session(DBType type) {
		if(type==DBType.ANY){
			if(this.readSession!=null){
				return this.readSession;
			}
			if(this.writeSession!=null){
				return this.writeSession;
			}
		}
		if(type==DBType.WRITE ){
			return this.getWriteSession();
		}
		return this.getReadSession();
	}
	
	
	private SqlSession getReadSession()  {
		if(this.readSession!=null){
			return this.readSession;
		}
		SqlSessionFactory factory=SqlSessionFactory.get(dbName);
		SqlSession session = factory.getSqlSession(DBType.READONLY);
		this.readSession=session;
		return session;
	}
	
	private SqlSession getWriteSession() {
		if(this.writeSession!=null){
			return this.writeSession;
		}
		SqlSessionFactory factory=SqlSessionFactory.get(dbName);
		SqlSession session = factory.getSqlSession(DBType.WRITE);
		this.writeSession=session;
		return session;
	}
	
	public SqlSession session() {
		return this._session(this.dbType);
	}
	
	public void commit(){
		if(this.writeSession!=null){
			this.writeSession.commit();
		}
	}
	public void rollback(){
		if(this.writeSession!=null){
			this.writeSession.rollback();
		}
	}
	@Override
	public void close() throws Exception {
		if(this.writeSession!=null){
			try{
				this.writeSession.close();
			}catch(Exception e){
				Log.get("DBSessionContext.close.2").error(e);
			}
			this.writeSession=null;
		}
		if(this.readSession!=null){
			try{
				this.readSession.close();
			}catch(Exception e){
				Log.get("DBSessionContext.close.10").error(e);
			}
			this.readSession=null;
		}
		sessionHolder.remove();
	}
}
