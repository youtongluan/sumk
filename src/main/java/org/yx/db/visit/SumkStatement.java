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
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.MapedSql.JsonWriterVisitor;
import org.yx.exception.SumkException;
import org.yx.log.CodeLineMarker;
import org.yx.log.Log;

public class SumkStatement {
	private static final Logger LOG = Log.get("sumk.sql.plain");
	private static SqlLog sqlLog = new SqlLogImpl();
	private static CodeLineMarker marker = new CodeLineMarker("org.yx.db.");

	public static CodeLineMarker getMarker() {
		return marker;
	}

	public static void setMarker(CodeLineMarker marker) {
		SumkStatement.marker = Objects.requireNonNull(marker);
	}

	public static void setSqlLog(SqlLog sqlLog) {
		SumkStatement.sqlLog = Objects.requireNonNull(sqlLog);
	}

	private final AtomicReference<PreparedStatement> statement;
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
		this.statement = new AtomicReference<>(statement);
		this.maped = maped;
		beginTime = System.currentTimeMillis();
		attachParams();
	}

	public int executeUpdate() throws SQLException {
		PreparedStatement statement = this.statement.get();
		if (statement == null) {
			SumkException.throwException(234132, "连接已关闭");
		}
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
		PreparedStatement statement = this.statement.get();
		if (statement == null) {
			SumkException.throwException(234132, "连接已关闭");
		}
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
		PreparedStatement statement = this.statement.getAndSet(null);
		if (statement == null) {
			return;
		}
		try {
			sqlLog.log(maped, writer -> {
				writer.name("sqlTime").value(sqlTime);
				writer.name("error").value(e.getMessage());
			});
			statement.close();
		} catch (SQLException e1) {
			Log.get("sumk.db").error(e1.getMessage(), e1);
		}
	}

	public synchronized void close() throws SQLException {
		PreparedStatement statement = this.statement.getAndSet(null);
		if (statement == null) {
			return;
		}
		int totalTime = (int) (System.currentTimeMillis() - beginTime);
		try {
			sqlLog.log(maped, writer -> {
				writer.name("sqlTime").value(sqlTime);
				writer.name("totalTime").value(totalTime);
				if (modifyCount > -1) {
					writer.name("modifyCount").value(modifyCount);
				}
			});
			statement.close();
		} catch (SQLException e1) {
			Log.get("sumk.db").error(e1.getMessage(), e1);
		}
	}

	private void attachParams() throws Exception {
		PreparedStatement statement = this.statement.get();
		if (statement == null) {
			SumkException.throwException(234132, "连接已关闭");
		}
		List<Object> params = maped.getParamters();
		int size = params.size();
		if (params != null && size > 0) {
			for (int i = 0; i < size; i++) {
				Object parameterObj = params.get(i);
				if (parameterObj == null) {
					statement.setNull(i + 1, java.sql.Types.OTHER);
					continue;
				}
				if (parameterObj.getClass() == java.util.Date.class) {
					statement.setTimestamp(i + 1, new Timestamp(((java.util.Date) parameterObj).getTime()));
					continue;
				}
				statement.setObject(i + 1, parameterObj);
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
		PreparedStatement statement = this.statement.get();
		if (statement == null) {
			SumkException.throwException(234132, "连接已关闭");
		}
		return statement.getGeneratedKeys();
	}

	private static class SqlLogImpl implements SqlLog {

		private static final Logger LOG_SQL_JSON = Log.get("sumk.sql.json");

		@Override
		public void log(MapedSql maped, JsonWriterVisitor visitor) {
			if (LOG_SQL_JSON.isTraceEnabled()) {
				try {
					LOG_SQL_JSON.trace(marker, maped.toJson(visitor));
				} catch (Exception e2) {
					Log.get("sumk.db.error").error(e2.getMessage(), e2);
				}
			}
		}

	}
}
