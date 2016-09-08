package org.yx.biz;

import java.lang.reflect.Method;

import org.yx.db.DBSessionContext;
import org.yx.db.DBType;

/**
 * 用于解析反射的上下文，比如数据库连接的创建与销毁.
 * 它封装的是与数据源（包括db、redis、mongo等）的连接关系，并不关心是SOA方式，还是http方式
 * @author youtl
 *
 */
public class BizExcutor {
	
	private String dbName;
	private DBType dbType;
	
	public static BizExcutor create(String dbName, DBType dbType){
		return new BizExcutor(dbName,dbType);
	}

	public BizExcutor(String dbName, DBType dbType) {
		super();
		this.dbName = dbName;
		this.dbType = dbType;
	}

	public Object exec(Method m,Object obj,Object[] params) throws Exception{
		DBSessionContext dbCtx=null;
		try{
			dbCtx=createDBIfNeed(m);
			Object ret= m.invoke(obj, params);
			if(dbCtx!=null){
				dbCtx.commit();
			}
			return ret;
		}catch(Exception e){
			if(dbCtx!=null){
				dbCtx.rollback();
			}
			throw e;
		}finally{
			if(dbCtx!=null){
				dbCtx.close();
			}
		}
	}

	private DBSessionContext createDBIfNeed(Method m) {
		if(this.dbName==null || this.dbType==null){
			return null;
		}
		return DBSessionContext.create(dbName, dbType);
	}
}
