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
package org.yx.db.visit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.yx.db.DBType;
import org.yx.db.conn.ConnectionPool;
import org.yx.db.conn.EventLane;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.SelectBuilder;
import org.yx.db.sql.SqlBuilder;
import org.yx.log.Log;

public final class Visitors {
	private static Logger log = Log.get("sumk.sql");

	private static interface Transform<T> {
		T transFrom(ResultSet ret) throws Exception;
	}

	private static class QueryVisitor<T> implements SumkDbVisitor<T> {
		private final Transform<T> transform;

		private QueryVisitor(Transform<T> transform) {
			this.transform = transform;
		}

		@Override
		public T visit(SqlBuilder builder) throws Exception {
			MapedSql maped = builder.toMapedSql();
			Connection conn = ConnectionPool.get().connection(DBType.ANY);
			PreparedStatement statement = createStatement(conn, maped);
			ResultSet ret = statement.executeQuery();
			T list = this.transform.transFrom(ret);
			statement.close();
			return list;
		}

	}

	private static String getSql(PreparedStatement statement) {
		String sql = statement.toString();
		int index = sql.indexOf(": ");
		if (index < 10) {
			return sql;
		}
		return sql.substring(index + 2);
	}

	private static PreparedStatement createStatement(Connection conn, MapedSql maped) throws Exception {

		if (Log.isON(log)) {
			log.trace(String.valueOf(maped));
		}
		PreparedStatement statement = conn.prepareStatement(maped.getSql());
		List<Object> params = maped.getParamters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
		}
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			log.debug(sb.append("<=> ").append(getSql(statement)).toString());
		}
		return statement;
	}

	public static final SumkDbVisitor<Integer> modifyVisitor = builder -> {
		Connection conn = ConnectionPool.get().connection(DBType.WRITE);
		MapedSql maped = builder.toMapedSql();
		PreparedStatement statement = createStatement(conn, maped);
		int ret = statement.executeUpdate();
		statement.close();
		EventLane.pubuish(conn, maped.getEvent());
		return ret;
	};

	public static final SumkDbVisitor<List<Map<String, Object>>> queryVisitorForORM = builder -> {
		MapedSql maped = builder.toMapedSql();
		Connection conn = ConnectionPool.get().connection(DBType.ANY);
		PreparedStatement statement = createStatement(conn, maped);
		ResultSet ret = statement.executeQuery();
		PojoMeta pm = ((SelectBuilder) builder).parsePojoMeta(true);
		List<Map<String, Object>> list = ResultSetUtils.toMapList(ret, pm);
		statement.close();
		return list;
	};

	public static final SumkDbVisitor<List<Map<String, Object>>> queryVisitor = new QueryVisitor<>(
			ResultSetUtils::toMapList);

	public static final SumkDbVisitor<List<?>> singleListQueryVisitor = new QueryVisitor<>(ResultSetUtils::toList);

	public static final SumkDbVisitor<List<Object[]>> arrayListQueryVisitor = new QueryVisitor<>(
			ResultSetUtils::toObjectArrayList);
}
