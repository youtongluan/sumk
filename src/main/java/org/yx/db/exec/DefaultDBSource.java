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

import java.util.Objects;

import org.yx.db.enums.DBType;
import org.yx.db.enums.TransactionType;

public class DefaultDBSource implements DBSource {
	private final String dbName;
	private final DBType type;
	private final TransactionType transactionType;

	public DefaultDBSource(String dbName, DBType type, TransactionType transactionType) {
		this.dbName = Objects.requireNonNull(dbName);
		this.type = Objects.requireNonNull(type);
		this.transactionType = Objects.requireNonNull(transactionType);
	}

	public static DefaultDBSource create(String dbName, DBType type, TransactionType transactionType) {
		return new DefaultDBSource(dbName, type, transactionType);
	}

	public String dbName() {
		return dbName;
	}

	public DBType dbType() {
		return type;
	}

	@Override
	public TransactionType transactionType() {
		return this.transactionType;
	}

	@Override
	public String toString() {
		return "DefaultDatabase [dbName=" + dbName + ", type=" + type + ", transactionType=" + transactionType + "]";
	}

}
