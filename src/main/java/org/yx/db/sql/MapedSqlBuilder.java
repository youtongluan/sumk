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

import java.util.Map;

import org.yx.db.sql.token.MapedSqlTokenParser;
import org.yx.db.sql.token.ReplaceTokenHandler;
import org.yx.db.sql.token.StringTokenParser;
import org.yx.db.sql.token.VariableTokenHandler;

public class MapedSqlBuilder implements SqlBuilder {
	private final String _sql;
	private final Map<String, Object> map;

	/**
	 * 
	 * @param sql
	 * @param bean
	 *            pojo或者map
	 */
	public MapedSqlBuilder(String sql, Map<String, Object> map) {
		this._sql = sql;
		this.map = map;
	}

	@Override
	public MapedSql toMapedSql() throws Exception {
		String sql = _sql;
		if (sql.contains("${")) {
			sql = new StringTokenParser("${", "}", new ReplaceTokenHandler(map)).parse(sql);
		}
		return new MapedSqlTokenParser("#{", "}", new VariableTokenHandler(map)).parse(sql);
	}

}
