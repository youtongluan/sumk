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
package org.yx.annotation.spec;

import java.util.Objects;

import org.yx.db.enums.DBType;
import org.yx.db.enums.TransactionType;

public class BoxSpec {
	private final String value;
	private final DBType dbType;
	private final TransactionType transaction;

	public BoxSpec(String value, DBType dbType, TransactionType transaction) {
		this.value = Objects.requireNonNull(value);
		this.dbType = Objects.requireNonNull(dbType);
		this.transaction = Objects.requireNonNull(transaction);
	}

	/**
	 * @return 数据库字段的名字，不填的话，就是属性名(小写)
	 */
	public String value() {
		return this.value;
	}

	public DBType dbType() {
		return this.dbType;
	}

	public TransactionType transaction() {
		return this.transaction;
	}
}
