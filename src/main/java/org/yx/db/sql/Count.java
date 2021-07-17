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
import java.util.List;
import java.util.Objects;

import org.yx.db.mapper.RawExecutor;
import org.yx.util.StringUtil;

/**
 * 直接调用select.count()就可以了，一般不需要显式使用这个类
 * 
 * @see select
 */
public class Count {
	protected final SelectBuilder select;

	public Count(SelectBuilder select) {
		this.select = select;
	}

	public long execute() {
		List<Object> paramters = new ArrayList<>(8);
		PojoMeta pojoMeta = select.makeSurePojoMeta();
		pojoMeta.getCounter().incrQueryCount();

		Objects.requireNonNull(pojoMeta, "pojo meta cannot be null");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM ").append(pojoMeta.getTableName());
		CharSequence where = select.buildWhere(paramters);
		if (StringUtil.isNotEmpty(where)) {
			sql.append(" WHERE ").append(where);
		}
		return RawExecutor.count(sql.toString(), paramters);
	}

}
