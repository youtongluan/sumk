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
package org.yx.db.mapper;

import java.util.Map;

import org.yx.db.sql.MapedSql;
import org.yx.db.sql.MapedSqlBuilder;

public class PureStringParser implements SqlParser {

	private final String sql;

	public static PureStringParser create(String sql) {
		if (sql == null || sql.isEmpty()) {
			return null;
		}
		return new PureStringParser(sql);
	}

	private PureStringParser(String sql) {
		this.sql = sql.trim();
	}

	@Override
	public MapedSql toMapedSql(Map<String, Object> map) throws Exception {
		return new MapedSqlBuilder(sql, map).toMapedSql();
	}

	@Override
	public String toString() {
		return sql;
	}

}
