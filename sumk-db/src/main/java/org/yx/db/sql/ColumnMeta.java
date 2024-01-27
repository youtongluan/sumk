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

import java.lang.reflect.Field;
import java.util.Map;

import org.yx.annotation.doc.Comment;
import org.yx.common.util.kit.TypeConverter;
import org.yx.conf.Const;
import org.yx.db.enums.ColumnType;
import org.yx.db.spec.ColumnSpec;
import org.yx.util.StringUtil;

public final class ColumnMeta implements Comparable<ColumnMeta> {

	final Field field;

	final ColumnType meta;
	final byte columnOrder;

	final String dbColumn;

	public ColumnMeta(Field field, ColumnSpec c) {
		this.field = field;
		this.meta = c == null ? ColumnType.NORMAL : c.type();
		if (c == null) {
			this.columnOrder = Const.DEFAULT_ORDER;
		} else {
			this.columnOrder = c.order();
		}
		this.dbColumn = (c == null || StringUtil.isEmpty(c.value()))
				? DBNameResolvers.getColumnNameResolver().apply(field.getName())
				: c.value();
	}

	public boolean isDBID() {
		return this.meta.isDbID();
	}

	public boolean isCacheID() {
		return this.meta.isCacheID();
	}

	public Object value(Object owner) throws IllegalArgumentException, IllegalAccessException {
		if (owner instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) owner;
			return map.get(field.getName());
		}
		return field.get(owner);
	}

	public void setValue(Object owner, final Object value) throws Exception {
		if (owner instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) owner;
			map.put(field.getName(), value);
			return;
		}
		field.set(owner, TypeConverter.convert(value, field.getType()));
	}

	public String getFieldName() {
		return field.getName();
	}

	@Override
	public int compareTo(ColumnMeta o) {
		if (this.columnOrder == o.columnOrder) {
			return Integer.compare(this.meta.order(), o.meta.order());
		}
		return this.columnOrder > o.columnOrder ? 1 : -1;
	}

	@Override
	public String toString() {
		return "ColumnMeta [field=" + field.getName() + ", meta=" + meta + ", columnOrder=" + columnOrder
				+ ", dbColumn=" + dbColumn + "]";
	}

	public Field getField() {
		return field;
	}

	public ColumnType getMeta() {
		return meta;
	}

	public int getColumnOrder() {
		return columnOrder;
	}

	public String getDbColumn() {
		return dbColumn;
	}

	public String getComment() {
		Comment c = this.field.getAnnotation(Comment.class);
		return c == null ? "" : c.value();
	}

}
