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
package org.yx.db.sql;

public final class SoftDeleteMeta {
	final String columnName;
	final Object validValue;
	final Object inValidValue;
	final Class<?> columnType;

	final boolean equalValid;

	public SoftDeleteMeta(String columnName, Object validValue, Object inValidValue, Class<?> columnType,
			boolean equal) {
		this.columnName = columnName;
		this.validValue = validValue;
		this.inValidValue = inValidValue;
		this.columnType = columnType;
		this.equalValid = equal;
	}

	public String getColumnName() {
		return columnName;
	}

	public Object getValidValue() {
		return validValue;
	}

	public Object getInValidValue() {
		return inValidValue;
	}

	public Class<?> getColumnType() {
		return columnType;
	}

	public boolean isEqualValid() {
		return equalValid;
	}
}
