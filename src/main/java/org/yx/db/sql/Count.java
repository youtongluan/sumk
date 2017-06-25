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

import org.yx.db.RawDB;
import org.yx.util.Assert;
import org.yx.util.StringUtils;

/**
 * 直接调用select.count()就可以了，一般不需要显式使用这个类
 * 
 * @see select
 */
public class Count implements Executable {
	protected final SelectBuilder select;

	public Count(SelectBuilder select) {
		this.select = select;
	}

	@Override
	public int execute() {
		List<Object> paramters = new ArrayList<>(8);
		select.pojoMeta = select.parsePojoMeta(true);
		Assert.notNull(select.pojoMeta, "pojo meta cannot be null");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM ").append(select.pojoMeta.getTableName());
		CharSequence where = select.buildWhere(paramters);
		if (StringUtils.isNotEmpty(where)) {
			sql.append(" WHERE ").append(where);
		}
		return RawDB.count(sql.toString(), paramters);
	}

}
