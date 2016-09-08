package org.yx.db.exec;

import org.yx.db.DBType;

public class DBExec {
	
	private static void exec(ResultContainer container,DBExecutor executor,DBType dbType) throws Exception{
		
		DBSessionProxy.create(container,dbType).exec(executor);
	}
	/**
	 * 在写连接中进行
	 * @param db
	 * @param executor
	 * @throws Exception 
	 */
	public static void exec(ResultContainer container,DBExecutor executor) throws Exception{
		exec(container,executor,DBType.WRITE);
	}
	/**
	 * 读写分离，读用的是读库
	 * @param db
	 * @param executor
	 * @param container
	 * @throws Exception 
	 */
	public static void execRWSplit(ResultContainer container,DBExecutor executor) throws Exception{
		exec(container,executor,DBType.ANY);
	}
	
	/**
	 * 仅用于查询，在读连接上操作
	 * @param db
	 * @param executor
	 * @throws Exception 
	 */
	public static void query(ResultContainer container,DBExecutor executor) throws Exception{
		exec(container,executor,DBType.READONLY);
	}
}
