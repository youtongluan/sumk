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

import org.yx.conf.Const;
import org.yx.db.enums.DBType;
import org.yx.db.enums.TransactionType;

public final class DBSources {

	private static final DBSource WRITE = new DefaultDBSource(Const.DEFAULT_DB_NAME, DBType.WRITE,
			TransactionType.REQUIRED);
	private static final DBSource READONLY = new DefaultDBSource(Const.DEFAULT_DB_NAME, DBType.READONLY,
			TransactionType.REQUIRED);
	private static final DBSource ANY = new DefaultDBSource(Const.DEFAULT_DB_NAME, DBType.ANY,
			TransactionType.REQUIRED);

	public static DBSource write() {
		return WRITE;
	}

	public static DBSource readOnly() {
		return READONLY;
	}

	public static DBSource any() {
		return ANY;
	}

	public static DBSource write(String dbName) {
		return new DefaultDBSource(dbName, DBType.WRITE, TransactionType.REQUIRED);
	}

	public static DBSource readOnly(String dbName) {
		return new DefaultDBSource(dbName, DBType.READONLY, TransactionType.REQUIRED);
	}

	public static DBSource any(String dbName) {
		return new DefaultDBSource(dbName, DBType.ANY, TransactionType.REQUIRED);
	}

	public static DBSource writeAutoCommit(String dbName) {
		return new DefaultDBSource(dbName, DBType.WRITE, TransactionType.AUTO_COMMIT);
	}

	public static DBSource autoCommit(String dbName) {
		return new DefaultDBSource(dbName, DBType.ANY, TransactionType.AUTO_COMMIT);
	}
}
