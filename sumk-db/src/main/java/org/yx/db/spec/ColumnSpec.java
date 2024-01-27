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
package org.yx.db.spec;

import java.util.Objects;

import org.yx.db.enums.ColumnType;

public class ColumnSpec {
	private final String value;
	private final ColumnType type;
	private final byte order;

	public ColumnSpec(String value, ColumnType type, byte order) {
		this.value = value;
		this.type = Objects.requireNonNull(type);
		this.order = order;
	}

	/**
	 * @return 数据库字段的名字，不填的话，就是属性名(小写)
	 */
	public String value() {
		return this.value;
	}

	public ColumnType type() {
		return this.type;
	}

	public byte order() {
		return this.order;
	}
}
