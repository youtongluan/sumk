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
package org.yx.common;

import java.sql.SQLException;

import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.exception.SumkException;
import org.yx.log.Log;

/**
 * 用于解析反射的上下文，比如数据库连接的创建与销毁.
 * 它封装的是与数据源（包括db、redis、mongo等）的连接关系，并不关心是SOA方式，还是http方式
 * 
 * @author 游夏
 *
 */
public class AopExcutor {

	private ConnectionPool dbCtx = null;
	private boolean embed;

	public AopExcutor(boolean embed) {
		super();
		this.embed = embed;
	}

	public void begin(String dbName, DBType dbType) {
		Log.get(AopExcutor.class).trace("begin with embed:{}", embed);

		dbCtx = embed ? ConnectionPool.createIfAbsent(dbName, dbType) : ConnectionPool.create(dbName, dbType);
	}

	public void rollback(Throwable e) {
		Log.printStack(e);
		if (dbCtx != null) {
			try {
				dbCtx.rollback();
			} catch (SQLException e1) {
				Log.printStack(e1);
			}
		}
		if (RuntimeException.class.isInstance(e)) {
			throw (RuntimeException) e;
		}
		throw SumkException.create(e);
	}

	public void commit() {
		if (dbCtx == null) {
			return;
		}
		try {
			this.dbCtx.commit();
		} catch (SQLException e) {
			Log.printStack(e);
		}
	}

	public void close() {

		if (dbCtx == null) {
			return;
		}
		try {
			dbCtx.close();
		} catch (Exception e) {
			SumkException.throwException(7820198, "error in commit," + e.getMessage(), e);
		}
	}

}
