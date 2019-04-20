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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.yx.db.sql.MapedSql;
import org.yx.log.CodeLineMarker;
import org.yx.log.Log;

public class SumkStatement {
	private static final Logger LOG = Log.get("sumk.sql.plain");
	private static final Logger LOG_SQL_JSON = Log.get("sumk.sql.json");
	private static CodeLineMarker marker = new CodeLineMarker("org.yx.db.");

	public static CodeLineMarker getMarker() {
		return marker;
	}

	public static void setMarker(CodeLineMarker marker) {
		SumkStatement.marker = Objects.requireNonNull(marker);
	}

	private PreparedStatement statement;
	private final MapedSql maped;
	private final long beginTime;
	private int sqlTime;
	private int modifyCount = -1;

	static SumkStatement create(Connection conn, MapedSql maped) throws Exception {
		return new SumkStatement(conn.prepareStatement(maped.getSql()), maped);
	}

	static SumkStatement createAutoGenerateKeyStatement(Connection conn, MapedSql maped) throws Exception {
		return new SumkStatement(conn.prepareStatement(maped.getSql(), Statement.RETURN_GENERATED_KEYS), maped);
	}

	private SumkStatement(PreparedStatement statement, MapedSql maped) throws Exception {
		this.statement = statement;
		this.maped = maped;
		beginTime = System.currentTimeMillis();
		attachParams();
	}

	public int executeUpdate() throws SQLException {
		modifyCount = 0;
		try {
			modifyCount = statement.executeUpdate();
		} catch (Exception e) {
			sqlTime = (int) (System.currentTimeMillis() - beginTime);
			close(e);
			throw e;
		}
		sqlTime = (int) (System.currentTimeMillis() - beginTime);
		return modifyCount;
	}

	public ResultSet executeQuery() throws Exception {
		ResultSet ret = null;
		try {
			ret = statement.executeQuery();
		} catch (Exception e) {
			sqlTime = (int) (System.currentTimeMillis() - beginTime);
			close(e);
			throw e;
		}
		sqlTime = (int) (System.currentTimeMillis() - beginTime);
		return ret;
	}

	private void close(Exception e) {
		PreparedStatement statement = null;
		synchronized (this) {
			statement = this.statement;
			if (statement == null) {
				return;
			}
			this.statement = null;
		}
		if (Log.isON(LOG_SQL_JSON)) {
			try {
				LOG_SQL_JSON.trace(marker, maped.toJson(writer -> {
					writer.name("sqlTime").value(sqlTime);
					writer.name("error").value(e.getMessage());
				}));
			} catch (Exception e2) {
				Log.get("sumk.db.error").error(e.getMessage(), e);
			}
		}
		try {
			statement.close();
		} catch (SQLException e1) {
			Log.get("sumk.db").error(e1.getMessage(), e1);
		}
	}

	public synchronized void close() throws SQLException {
		PreparedStatement statement = null;
		synchronized (this) {
			statement = this.statement;
			if (statement == null) {
				return;
			}
			this.statement = null;
		}
		if (Log.isON(LOG_SQL_JSON)) {
			int totalTime = (int) (System.currentTimeMillis() - beginTime);
			try {
				LOG_SQL_JSON.trace(marker, maped.toJson(writer -> {
					writer.name("sqlTime").value(sqlTime);
					writer.name("totalTime").value(totalTime);
					if (modifyCount > -1) {
						writer.name("modifyCount").value(modifyCount);
					}
				}));
			} catch (Exception e) {
				Log.get("sumk.db.error").error(e.getMessage(), e);
			}
		}
		try {
			statement.close();
		} catch (SQLException e1) {
			Log.get("sumk.db").error(e1.getMessage(), e1);
		}
	}

	private void attachParams() throws Exception {
		List<Object> params = maped.getParamters();
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
		}
		if (LOG.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			LOG.debug(marker, sb.append("<=> ").append(getSql()).toString());
		}
	}

	private String getSql() {
		String sql = statement.toString();
		int index = sql.indexOf(": ");
		if (index < 10 || index + 10 > sql.length()) {
			return sql;
		}
		return sql.substring(index + 2);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return statement.getGeneratedKeys();
	}
}
