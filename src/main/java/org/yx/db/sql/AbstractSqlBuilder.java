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

import org.yx.db.visit.SumkDbVisitor;
import org.yx.exception.SumkException;
import org.yx.util.Assert;
import org.yx.util.CollectionUtils;

public abstract class AbstractSqlBuilder<T> implements SqlBuilder {
	protected boolean withnull;

	protected SumkDbVisitor<T> visitor;

	protected Class<?> tableClass;

	protected PojoMeta pojoMeta;

	protected List<Map<String, Object>> in;

	protected void checkIn() {
		Assert.isTrue(CollectionUtils.isNotEmpty(this.in), "no conditions");
	}

	/**
	 * 添加参数，会自动判断类型，设置pojoMeta
	 * 
	 * @param src
	 *            Map或pojo类型
	 */
	@SuppressWarnings("unchecked")
	protected void _addIn(Object src) {
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
		this.pojoMeta = PojoMetaHolder.getPojoMeta(src.getClass());
		if (this.pojoMeta == null) {
			SumkException.throwException(3654, src.getClass() + " does not config as a table");
		}
		try {
			this.in.add(this.pojoMeta.populate(src, withnull));
		} catch (Exception e) {
			SumkException.throwException(534254, e.getMessage(), e);
		}
	}

	/**
	 * 获取sql操作相关联的对象信息<BR>
	 * <B>这个是sumk框架的方法，不推荐在业务代码中使用</B>
	 */
	public PojoMeta getPojoMeta() {
		if (this.tableClass != null) {
			return PojoMetaHolder.getPojoMeta(tableClass);
		}

		return pojoMeta;
	}

	public AbstractSqlBuilder(SumkDbVisitor<T> visitor) {
		this.visitor = visitor;
	}

	protected T accept(SumkDbVisitor<T> visitor) {
		try {
			return visitor.visit(this);
		} catch (Exception e) {
			throw new SumkException(435, e.getMessage(), e);
		}
	}

}
