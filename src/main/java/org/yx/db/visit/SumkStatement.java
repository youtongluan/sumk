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
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.yx.db.sql.DBSettings;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.SqlLog;
import org.yx.exception.SumkException;
import org.yx.exception.SumkExceptionCode;
import org.yx.log.CodeLineMarker;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.util.SumkDate;

public class SumkStatement {
	private static final Logger LOG = Log.get("sumk.sql.plain");
	private static SqlLog sqlLog = (a, b, c) -> {
	};
	private static CodeLineMarker marker = new CodeLineMarker("org.yx.db.");
	private static BiConsumer<PreparedStatement, List<Object>> statementParamAttacher = (ps, params) -> {
		int size = params.size();
		if (size == 0) {
			return;
		}
		try {
			for (int i = 0; i < size; i++) {
				Object parameterObj = params.get(i);
				if (parameterObj == null) {
					ps.setNull(i + 1, java.sql.Types.OTHER);
					continue;
				}
				if (parameterObj.getClass() == java.util.Date.class) {
					ps.setTimestamp(i + 1, new Timestamp(((java.util.Date) parameterObj).getTime()));
					continue;
				}
				if (parameterObj.getClass() == SumkDate.class) {
					ps.setTimestamp(i + 1, SumkDate.class.cast(parameterObj).toTimestamp());
					continue;
				}
				ps.setObject(i + 1, parameterObj);
			}
		} catch (Exception e) {
			throw new SumkException(-3643654, "设置PreparedStatement的参数失败", e);
		}
	};

	public static BiConsumer<PreparedStatement, List<Object>> getStatementParamAttacher() {
		return statementParamAttacher;
	}

	public static void setStatementParamAttacher(BiConsumer<PreparedStatement, List<Object>> statementParamAttacher) {
		SumkStatement.statementParamAttacher = Objects.requireNonNull(statementParamAttacher);
	}

	private final AtomicReference<PreparedStatement> statement;
	private final MapedSql maped;
	private final long beginTime;
	private int sqlTime;
	private int modifyCount = -1;

	/**
	 * 非空
	 * 
	 * @return marker实例
	 */
	public static CodeLineMarker getMarker() {
		return marker;
	}

	public static void setMarker(CodeLineMarker marker) {
		SumkStatement.marker = Objects.requireNonNull(marker);
	}

	public static void setSqlLog(SqlLog sqlLog) {
		SumkStatement.sqlLog = Objects.requireNonNull(sqlLog);
	}

	static SumkStatement create(Connection conn, MapedSql maped) throws Exception {
		return new SumkStatement(conn.prepareStatement(maped.getSql()), maped);
	}

	static SumkStatement createAutoGenerateKeyStatement(Connection conn, MapedSql maped) throws Exception {
		return new SumkStatement(conn.prepareStatement(maped.getSql(), Statement.RETURN_GENERATED_KEYS), maped);
	}

	private SumkStatement(PreparedStatement statement, MapedSql maped) {
		this.statement = new AtomicReference<>(statement);
		this.maped = maped;
		beginTime = System.currentTimeMillis();
		attachParams();
	}

	public int executeUpdate() throws SQLException {
		PreparedStatement statement = this.statement.get();
		if (statement == null) {
			throw new SumkException(SumkExceptionCode.DB_CONNECTION_CLOSED, "连接已关闭");
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
			throw new SumkException(SumkExceptionCode.DB_CONNECTION_CLOSED, "连接已关闭");
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
			statement.close();
			if (DBSettings.isUnionLogEnable()) {
				sqlLog.log(this, -1, e);
			}
		} catch (SQLException e1) {
			Logs.db().error(e1.getMessage(), e1);
		}
	}

	public synchronized void close() throws SQLException {
		PreparedStatement statement = this.statement.getAndSet(null);
		if (statement == null) {
			return;
		}
		try {
			statement.close();
			int totalTime = (int) (System.currentTimeMillis() - beginTime);
			if (DBSettings.isUnionLogEnable() && totalTime >= DBSettings.unionLogTime()) {
				sqlLog.log(this, totalTime, null);
			}
			this.logSpendTime();
		} catch (SQLException e1) {
			Logs.db().error(e1.getMessage(), e1);
		}
	}

	private void logSpendTime() {
		if (LOG.isDebugEnabled()) {
			if (this.sqlTime > DBSettings.debugLogSpendTime()) {
				LOG.debug(marker, this.buildTimeLog());
			} else if (LOG.isTraceEnabled()) {
				LOG.trace(marker, this.buildTimeLog());
			}
		}
	}

	private String buildTimeLog() {
		StringBuilder sb = new StringBuilder();
		sb.append("$耗时(ms):").append(this.sqlTime);
		if (this.modifyCount >= 0) {
			sb.append(",修改的记录数:").append(this.modifyCount);
		}
		return sb.toString();
	}

	private void attachParams() {
		PreparedStatement statement = this.statement.get();
		if (statement == null) {
			throw new SumkException(SumkExceptionCode.DB_CONNECTION_CLOSED, "连接已关闭了");
		}
		statementParamAttacher.accept(statement, maped.getParamters());
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
			throw new SumkException(SumkExceptionCode.DB_CONNECTION_CLOSED, "连接已关闭");
		}
		return statement.getGeneratedKeys();
	}

	public int getSqlTime() {
		return sqlTime;
	}

	public int getModifyCount() {
		return modifyCount;
	}

	public MapedSql getMaped() {
		return maped;
	}
}
