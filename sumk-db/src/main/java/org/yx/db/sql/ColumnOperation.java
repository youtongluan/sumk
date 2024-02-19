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

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.yx.exception.SumkException;

public class ColumnOperation implements CompareOperation {
	private final String name;
	private final Operation type;
	private final Object value;

	public ColumnOperation(String name, Operation type, Object value) {
		this.name = Objects.requireNonNull(name);
		this.type = Objects.requireNonNull(type);
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Operation getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name + type.op + "?";
	}

	public boolean isSameOperation(ColumnOperation b) {
		return this.name.equals(b.name) && this.type == b.type;
	}

	@Override
	public CharSequence buildSql(SelectBuilder select, List<Object> paramters) {
		ColumnMeta cm = select.pojoMeta.getByFieldName(name);
		if (cm == null) {
			if (select.isOn(DBFlag.FAIL_IF_PROPERTY_NOT_MAPPED)) {
				throw new SumkException(54675234, name + "这个字段没有在" + select.pojoMeta.pojoClz.getName() + "中定义");
			}
			return "";
		}
		StringBuilder sb = new StringBuilder().append(cm.dbColumn);
		if (value == null) {
			if (type == Operation.EQUAL) {
				return sb.append(" IS NULL");
			}
			if (type == Operation.NOT) {
				return sb.append(" IS NOT NULL");
			}
			if (!select.isOn(DBFlag.SELECT_COMPARE_ALLOW_NULL)) {
				throw new SumkException(2342423, name + "的值为null");
			}
			if (select.isOn(DBFlag.SELECT_COMPARE_IGNORE_NULL)) {
				return "";
			}
		}
		sb.append(type.op);
		if (type == Operation.IN || type == Operation.NOT_IN) {
			sb.append("(");
			boolean first = true;
			for (Object obj : (Collection<?>) value) {
				paramters.add(obj);
				if (first) {
					sb.append('?');
					first = false;
				} else {
					sb.append(",?");
				}
			}
			return sb.append(")");
		}

		paramters.add(value);
		return sb.append('?');
	}
}
