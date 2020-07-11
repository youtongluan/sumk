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
import java.sql.Blob;
import java.sql.Clob;
import java.util.Map;

import org.yx.annotation.db.Column;
import org.yx.annotation.doc.Comment;
import org.yx.common.date.TimeUtil;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.db.enums.ColumnType;
import org.yx.db.kit.NumUtil;
import org.yx.exception.SumkException;
import org.yx.util.StreamUtil;
import org.yx.util.StringUtil;

public final class ColumnMeta implements Comparable<ColumnMeta> {

	final Field field;

	final ColumnType meta;
	final byte columnOrder;

	final String dbColumn;

	final boolean isNumber;

	final boolean isDate;

	public ColumnMeta(Field field, Column c) {
		super();
		this.field = field;
		this.meta = c == null ? ColumnType.NORMAL : c.type();
		if (c == null) {
			this.columnOrder = Const.DEFAULT_ORDER;
		} else {
			this.columnOrder = c.order();
		}
		this.dbColumn = (c == null || StringUtil.isEmpty(c.value()))
				? DBNameResolvers.getResolver().resolveColumnName(field.getName()) : c.value();
		this.isNumber = Number.class.isAssignableFrom(field.getType());
		this.isDate = TimeUtil.isGenericDate(field.getType());
	}

	public boolean isDBID() {
		return ColumnType.ID_DB.accept(meta);
	}

	public boolean isRedisID() {
		return ColumnType.ID_CACHE.accept(meta);
	}

	public boolean accept(ColumnType type) {
		return meta.accept(type);
	}

	public Object value(Object owner) throws IllegalArgumentException, IllegalAccessException {
		if (Map.class.isInstance(owner)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) owner;
			return map.get(field.getName());
		}
		return field.get(owner);
	}

	void setValue(Object owner, final Object value) throws Exception {
		if (Map.class.isInstance(owner)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) owner;
			map.put(field.getName(), value);
			return;
		}
		if (value == null) {
			field.set(owner, null);
			return;
		}
		final Class<?> fieldType = this.field.getType();
		if (this.isDate) {
			Object v = TimeUtil.toType(value, fieldType, false);
			field.set(owner, v);
			return;
		}

		if (fieldType.isInstance(value)) {
			field.set(owner, value);
			return;
		}
		if (this.isNumber && Number.class.isInstance(value)) {
			Number v = NumUtil.toType((Number) value, fieldType, false);
			field.set(owner, v);
			return;
		}
		if (fieldType == byte[].class && Blob.class.isInstance(value)) {
			Blob v = (Blob) value;
			byte[] bs = StreamUtil.readAllBytes(v.getBinaryStream(), true);
			field.set(owner, bs);
			return;
		}
		if (fieldType == String.class && Clob.class.isInstance(value)) {
			Clob v = (Clob) value;
			String s = StreamUtil.readAll(v.getCharacterStream(), true);
			field.set(owner, s);
			return;
		}
		if (AppInfo.getBoolean("sumk.db.fail.miss.type", true)) {
			SumkException.throwException(345234543,
					"字段类型是" + fieldType.getName() + ",参数对象的实际类型是:" + value.getClass().getName());
		}
	}

	public String getFieldName() {
		return field.getName();
	}

	@Override
	public int compareTo(ColumnMeta o) {
		if (this.columnOrder == o.columnOrder) {
			return this.meta.order - o.meta.order;
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

	public byte getColumnOrder() {
		return columnOrder;
	}

	public String getDbColumn() {
		return dbColumn;
	}

	public boolean isNumber() {
		return isNumber;
	}

	public boolean isDate() {
		return isDate;
	}

	public String getComment() {
		Comment c = this.field.getAnnotation(Comment.class);
		return c == null ? "" : c.value();
	}

}
