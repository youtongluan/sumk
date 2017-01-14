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
package org.yx.db.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.db.sql.ColumnMeta;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.SelectBuilder;
import org.yx.db.sql.SqlBuilder;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;
import org.yx.util.Assert;

public class QueryVisitor implements SumkDbVisitor<List<Map<String, Object>>> {

	public static QueryVisitor visitor = new QueryVisitor();

	@Override
	public List<Map<String, Object>> visit(SqlBuilder builder) throws Exception {
		MapedSql maped = builder.toMapedSql();
		if (ConsoleLog.isEnable(ConsoleLog.ON)) {
			Log.get("sumk.SQL.visitor").trace(String.valueOf(maped));
		}
		Connection conn = ConnectionPool.get().connection(DBType.ANY);
		PreparedStatement statement = conn.prepareStatement(maped.getSql());
		List<Object> params = maped.getParamters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
		}
		Log.get("sumk.SQL").debug(" ==> {}", statement);
		ResultSet ret = statement.executeQuery();
		PojoMeta pm = null;
		if (SelectBuilder.class.isInstance(builder)) {
			pm = ((SelectBuilder) builder).getPojoMeta();
		}
		List<Map<String, Object>> list = toMapList(ret, pm);
		statement.close();
		return list;
	}

	public static List<Map<String, Object>> toMapList(ResultSet rs, PojoMeta pm) throws java.sql.SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		if (rs == null) {
			return list;
		}
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		Map<String, Object> rowData = new HashMap<>();
		while (rs.next()) {
			rowData = new HashMap<>(columnCount * 2);
			for (int i = 1; i <= columnCount; i++) {
				if (pm == null) {
					rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
					continue;
				}
				ColumnMeta cm = pm.getByColumnDBName(md.getColumnName(i));
				Assert.notNull(cm, md.getColumnName(i) + " has no mapper");
				rowData.put(cm.getFieldName(), rs.getObject(i));
			}
			list.add(rowData);
		}
		rs.close();
		return list;
	}
}
