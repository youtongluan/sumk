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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;

public abstract class AbstractSqlBuilder<T> implements SqlBuilder {

	protected SumkDbVisitor<T> visitor;

	protected Class<?> tableClass;

	protected PojoMeta pojoMeta;

	protected List<Map<String, Object>> in;

	protected boolean failIfPropertyNotMapped;

	protected String sub;

	protected void sub(String sub) {
		this.sub = sub;
		if (this.pojoMeta != null) {
			this.pojoMeta = PojoMetaHolder.getPojoMeta(this.pojoMeta, sub);
		}
	}

	protected void checkMap(Map<String, ?> map, PojoMeta pm) {
		if (!this.failIfPropertyNotMapped) {
			return;
		}
		Set<String> keys = map.keySet();
		MAPKEY: for (String key : keys) {
			for (ColumnMeta p : pm.fieldMetas) {
				if (p.getFieldName().equals(key)) {
					continue MAPKEY;
				}
			}
			throw new SumkException(-743257, key + " is not a field in " + pm.pojoClz);
		}
	}

	protected void checkIn() {
		if (CollectionUtil.isEmpty(this.in)) {
			throw new SumkException(-7345245, "no conditions");
		}
	}

	protected void _addIn(Object src) {
		this._addInByMap(this.populate(src, false));
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> populate(Object src, boolean keepNull) {
		if (src == null) {
			return Collections.emptyMap();
		}
		if (src instanceof Map) {
			return new HashMap<>((Map<String, Object>) src);
		}
		if (this.tableClass == null) {
			this.tableClass = src.getClass();
		}
		try {
			return this.makeSurePojoMeta().populate(src, keepNull);
		} catch (Exception e) {
			throw new SumkException(-42534254, e.getMessage(), e);
		}
	}

	protected void _addInByMap(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		if (this.in == null) {
			this.in = new ArrayList<>();
		}
		this.in.add(map);
	}

	public PojoMeta makeSurePojoMeta() {
		if (this.pojoMeta != null) {
			return this.pojoMeta;
		}
		if (this.tableClass != null) {
			this.pojoMeta = PojoMetaHolder.getPojoMeta(tableClass, sub);
		}
		if (this.pojoMeta == null) {
			throw new SumkException(7325435, "please call tableClass(XX.class) first");
		}
		return this.pojoMeta;
	}

	public AbstractSqlBuilder(SumkDbVisitor<T> visitor) {
		this.visitor = visitor;
		this.failIfPropertyNotMapped = DBSettings.failIfPropertyNotMapped();
	}

	protected T accept(SumkDbVisitor<T> visitor) {
		try {
			return visitor.visit(this);
		} catch (Exception e) {
			if (e instanceof SumkException) {
				throw (SumkException) e;
			}
			throw new SumkException(-53172189, e.getMessage(), e);
		}
	}

}
