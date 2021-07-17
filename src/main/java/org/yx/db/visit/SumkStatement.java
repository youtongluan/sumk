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
import org.yx.db.kit.DBKits;
import org.yx.db.sql.DBSettings;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.SqlLog;
import org.yx.exception.SumkException;
import org.yx.exception.SumkExceptionCode;
import org.yx.log.CodeLineMarker;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.util.SumkDate;

public class SumkStatement implements AutoCloseable {
	private static final Logger LOG = Log.get("sumk.sql.plain");
	private static long executeCount;
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
					ps.setTimestamp(i + 1, ((SumkDate) parameterObj).toTimestamp());
					continue;
				}
				ps.setObject(i + 1, parameterObj);
			}
		} catch (Exception e) {
			throw new SumkException(-3643654,
					"设置PreparedStatement的参数失败，sql语句: " + DBKits.getSqlOfStatement(ps) + " ,参数: " + params, e);
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
	private Throwable exception;

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

	private PreparedStatement checkStatement() throws SQLException {
		PreparedStatement statement = this.statement.get();
		if (statement == null) {
			throw new SumkException(SumkExceptionCode.DB_CONNECTION_CLOSED, "连接已关闭");
		}
		executeCount++;
		return statement;
	}

	public int executeUpdate() throws SQLException {
		PreparedStatement statement = checkStatement();
		modifyCount = 0;
		try {
			modifyCount = statement.executeUpdate();
		} catch (Exception e) {
			this.exception = e;
			sqlTime = (int) (System.currentTimeMillis() - beginTime);
			throw e;
		}
		sqlTime = (int) (System.currentTimeMillis() - beginTime);
		return modifyCount;
	}

	public ResultSet executeQuery() throws Exception {
		PreparedStatement statement = checkStatement();
		ResultSet ret = null;
		try {
			ret = statement.executeQuery();
		} catch (Exception e) {
			this.exception = e;
			sqlTime = (int) (System.currentTimeMillis() - beginTime);
			throw e;
		}
		sqlTime = (int) (System.currentTimeMillis() - beginTime);
		return ret;
	}

	public void close() throws SQLException {
		PreparedStatement statement = this.statement.getAndSet(null);
		if (statement == null) {
			return;
		}
		try {
			if (this.exception != null) {
				LOG.error(marker, DBKits.getSqlOfStatement(statement) + ", 发生异常", this.exception);
			}
			statement.close();
			int totalTime = (int) (System.currentTimeMillis() - beginTime);
			if (DBSettings.isUnionLogEnable() && (this.exception != null || totalTime >= DBSettings.unionLogTime())) {
				sqlLog.log(this, totalTime, this.exception);
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
			LOG.debug(marker, sb.append("<=> ").append(DBKits.getSqlOfStatement(this.statement.get())).toString());
		}
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

	public static long getExecuteCount() {
		return executeCount;
	}
}
