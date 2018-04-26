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
import java.util.Arrays;
import java.util.List;

public class RawSqlBuilder implements SqlBuilder {
	private String _sql;
	private List<Object> _params;

	@SuppressWarnings("unchecked")
	public RawSqlBuilder(String sql, Object[] params) {
		this._sql = sql;
		if (params == null || params.length == 0) {
			this._params = new ArrayList<>();
		} else if (params.length == 1 && List.class.isInstance(params[0])) {
			this._params = (List<Object>) params[0];
		} else {
			this._params = Arrays.asList(params);
		}
	}

	@Override
	public MapedSql toMapedSql() throws Exception {
		MapedSql ms = new MapedSql();
		ms.setSql(_sql);
		ms.addParams(_params);
		return ms;
	}

}
