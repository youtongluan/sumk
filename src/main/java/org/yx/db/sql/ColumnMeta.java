/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import org.yx.db.annotation.Column;
import org.yx.db.annotation.ColumnType;
import org.yx.db.kit.NumUtil;
import org.yx.util.StringUtils;

public class ColumnMeta implements Comparable<ColumnMeta> {

	public final Field field;

	public final ColumnType meta;
	final byte columnOrder;

	public final String dbColumn;
//	public final UpdateType updateType;

	public final boolean isNumber;

	ColumnMeta(Field field, Column c) {
		super();
		this.field = field;
		this.meta = c == null ? ColumnType.NORMAL : c.columnType();
		if (c == null) {
			this.columnOrder = 64;
//			this.updateType = UpdateType.CUSTOM;
		} else {
			this.columnOrder = 1;
//			this.updateType = c.updateType();
		}
		this.dbColumn = (c == null || StringUtils.isEmpty(c.value())) ? field.getName().toLowerCase() : c.value();
		this.isNumber = Number.class.isAssignableFrom(field.getType());
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

	/**
	 * 
	 * @param owner
	 *            pojo类或者Map<String,Object>
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object value(Object owner) throws IllegalArgumentException, IllegalAccessException {
		if (Map.class.isInstance(owner)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) owner;
			return map.get(field.getName());
		}
		return field.get(owner);
	}

	void setValue(Object owner, Object value) throws IllegalArgumentException, IllegalAccessException {
		if (Map.class.isInstance(owner)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) owner;
			map.put(field.getName(), value);
			return;
		}
		if (this.isNumber && Number.class.isInstance(value)) {
			value = NumUtil.toType((Number) value, this.field.getType(), false);
		}
		field.set(owner, value);
	}

	public String getFieldName() {
		return field.getName();
	}

	@Override
	public int compareTo(ColumnMeta o) {
		if (this.columnOrder == o.columnOrder) {
			return this.dbColumn.compareTo(o.dbColumn);
		}
		return this.columnOrder > o.columnOrder ? 1 : -1;
	}

	@Override
	public String toString() {
		return "ColumnMeta [field=" + field.getName() + ", meta=" + meta + ", columnOrder=" + columnOrder
				+ ", dbColumn=" + dbColumn + "]";
	}

}
