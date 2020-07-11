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
package org.yx.db.exec;

import java.sql.SQLException;

import org.yx.db.DBType;
import org.yx.db.TransactionType;
import org.yx.db.conn.ConnectionPool;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public final class DBTransaction implements AutoCloseable {

	private ConnectionPool dbCtx = null;
	private final TransactionType transaction;
	private final String dbName;
	private final DBType dbType;

	public DBTransaction(String dbName, DBType dbType, TransactionType transaction) {
		this.transaction = transaction;
		this.dbName = dbName;
		this.dbType = dbType;
	}

	public void begin() {
		switch (transaction) {
		case AUTO_COMMIT:
			dbCtx = ConnectionPool.create(dbName, dbType, true);
			return;
		case REQUIRES_NEW:
			dbCtx = ConnectionPool.create(dbName, dbType, false);
			return;
		case REQUIRED:

			dbCtx = ConnectionPool.createIfAbsent(dbName, dbType);
			return;
		default:
			return;
		}
	}

	public boolean isAutoCommit() {
		return this.transaction == TransactionType.AUTO_COMMIT;
	}

	public void rollback(Throwable e) {
		if (dbCtx != null && !this.isAutoCommit()) {
			try {
				dbCtx.rollback();
			} catch (SQLException e1) {
				Log.printStack("sumk.sql.error", e1);
			}
		}
	}

	public void commit() {
		if (dbCtx == null || this.isAutoCommit()) {
			return;
		}
		try {
			this.dbCtx.commit();
		} catch (SQLException e) {
			Log.printStack("sumk.sql.error", e);
		}
	}

	@Override
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

	public ConnectionPool getConnectionPool() {
		return dbCtx;
	}

}
