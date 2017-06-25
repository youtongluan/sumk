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
import java.util.List;
import java.util.Map;

import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.db.conn.EventLane;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.SelectBuilder;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;

public class Visitors {

	public static final SumkDbVisitor<Integer> modifyVisitor = builder -> {
		MapedSql maped = builder.toMapedSql();
		if (ConsoleLog.isEnable(ConsoleLog.ON)) {
			Log.get("sumk.SQL.visitor").trace(String.valueOf(maped));
		}
		Connection conn = ConnectionPool.get().connection(DBType.WRITE);
		PreparedStatement statement = conn.prepareStatement(maped.getSql());
		List<Object> params = maped.getParamters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
		}
		Log.get("sumk.SQL").debug(" <== {}", statement);
		int ret = statement.executeUpdate();
		statement.close();
		EventLane.pubuish(conn, maped.getEvent());
		return ret;
	};

	public static final SumkDbVisitor<List<Map<String, Object>>> queryVisitor = builder -> {
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
			pm = ((SelectBuilder) builder).parsePojoMeta(true);
		}
		List<Map<String, Object>> list = ResultSetUtils.toMapList(ret, pm);
		statement.close();
		return list;
	};

	public static final SumkDbVisitor<List<?>> singleListQueryVisitor = builder -> {
		MapedSql maped = builder.toMapedSql();
		if (ConsoleLog.isEnable(ConsoleLog.ON)) {
			Log.get("sumk.SQL.single.visitor").trace(String.valueOf(maped));
		}
		Connection conn = ConnectionPool.get().connection(DBType.ANY);
		PreparedStatement statement = conn.prepareStatement(maped.getSql());
		List<Object> params = maped.getParamters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
		}
		Log.get("sumk.SQL.single").debug(" ==> {}", statement);
		ResultSet ret = statement.executeQuery();
		List<?> list = ResultSetUtils.toList(ret);
		statement.close();
		return list;
	};
}
