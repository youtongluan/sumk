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
package org.yx.common;

import java.sql.SQLException;

import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.exception.BizException;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class DBTransaction {

	private ConnectionPool dbCtx = null;
	private boolean embed;
	private String dbName;
	private DBType dbType;

	public DBTransaction(String dbName, DBType dbType, boolean embed) {
		super();
		this.embed = embed;
		this.dbName = dbName;
		this.dbType = dbType;
	}

	public void begin() {
		Log.get("sumk.db").trace("begin with embed:{}", embed);

		dbCtx = embed ? ConnectionPool.create(dbName, dbType) : ConnectionPool.createIfAbsent(dbName, dbType);
	}

	public void rollback(Throwable e) {
		if (BizException.class.isInstance(e)) {
			Log.get("sumk.error").warn(e.toString());
		} else {
			Log.printStack(SumkLogs.SQL_ERROR, e);
		}
		if (dbCtx != null) {
			try {
				dbCtx.rollback();
			} catch (SQLException e1) {
				Log.printStack(SumkLogs.SQL_ERROR, e1);
			}
		}
		if (RuntimeException.class.isInstance(e)) {
			throw (RuntimeException) e;
		}
		throw new SumkException(1076971, "业务执行出错", e);
	}

	public void commit() {
		if (dbCtx == null) {
			return;
		}
		try {
			this.dbCtx.commit();
		} catch (SQLException e) {
			Log.printStack(SumkLogs.SQL_ERROR, e);
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
