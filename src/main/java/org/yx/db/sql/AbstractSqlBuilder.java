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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.Assert;
import org.yx.util.CollectionUtils;

public abstract class AbstractSqlBuilder<T> implements SqlBuilder {
	static {
		OrmSettings.register();
	}

	protected boolean withnull;

	protected SumkDbVisitor<T> visitor;

	protected Class<?> tableClass;

	protected PojoMeta pojoMeta;

	protected List<Map<String, Object>> in;

	/**
	 * 检查map中的key是不是都是有效的字段.默认为true
	 */
	protected boolean failIfPropertyNotMapped;

	protected void checkMap(Map<String, Object> map, PojoMeta pm) {
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
			SumkException.throwException(743257, key + " is not a field in " + pm.pojoClz);
		}
	}

	protected void checkIn() {
		Assert.isTrue(CollectionUtils.isNotEmpty(this.in), "no conditions");
	}

	/**
	 * 添加参数，会自动判断类型，设置pojoMeta<BR>
	 * src如果是pojo对象，是否将值为null的字段加入到in中，由withnull属性决定
	 * 
	 * @param src
	 *            Map或pojo类型
	 */
	protected void _addIn(Object src) {
		this._addIn(src, withnull);
	}

	@SuppressWarnings("unchecked")
	protected void _addIn(Object src, boolean addNullFieldToIn) {
		if (src == null) {
			return;
		}
		if (this.in == null) {
			this.in = new ArrayList<>();
		}
		if (Map.class.isInstance(src)) {
			Map<String, Object> map = (Map<String, Object>) src;
			if (!map.isEmpty()) {
				this.in.add(map);
			}
			return;
		}

		if (this.pojoMeta == null) {
			this.pojoMeta = PojoMetaHolder.getPojoMeta(src.getClass());
			if (this.pojoMeta == null) {
				SumkException.throwException(3654, src.getClass() + " does not config as a table");
			}
		}
		try {
			this.in.add(this.pojoMeta.populate(src, addNullFieldToIn));
		} catch (Exception e) {
			SumkException.throwException(534254, e.getMessage(), e);
		}
	}

	public PojoMeta getPojoMeta() {
		if (this.tableClass != null) {
			return PojoMetaHolder.getPojoMeta(tableClass);
		}

		return pojoMeta;
	}

	public AbstractSqlBuilder(SumkDbVisitor<T> visitor) {
		this.visitor = visitor;
		this.failIfPropertyNotMapped = OrmSettings.FAIL_IF_PROPERTY_NOT_MAPPED;
	}

	protected T accept(SumkDbVisitor<T> visitor) {
		try {
			return visitor.visit(this);
		} catch (Exception e) {
			if (SumkException.class.isInstance(e)) {
				throw (SumkException) e;
			}
			throw new SumkException(435, e.getMessage(), e);
		}
	}

}
