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
package org.yx.db.log;

import java.util.Objects;

import org.yx.base.context.ActionContext;
import org.yx.base.sumk.UnsafeStringWriter;
import org.yx.db.DBJson;
import org.yx.db.sql.DBSettings;
import org.yx.db.sql.MapedSql;
import org.yx.db.sql.SqlLog;
import org.yx.db.visit.SumkStatement;
import org.yx.log.Log;
import org.yx.log.LogKits;
import org.yx.log.LogLevel;
import org.yx.log.Logs;
import org.yx.log.impl.CodeLine;
import org.yx.log.impl.CodeLineKit;
import org.yx.log.impl.LogObject;
import org.yx.log.impl.UnionLog;
import org.yx.log.impl.UnionLogs;
import org.yx.util.SumkDate;

import com.google.gson.stream.JsonWriter;

public class SimpleSqlLogImpl implements SqlLog {
	private static String LOG_NAME_SQL = "sumk.unionlog.sql";

	public SimpleSqlLogImpl() {
		UnionLog union = UnionLogs.getUnionLog();
		Logs.db().debug("sqlLog初始化时，UnionLog的状态为:{}", union.isStarted());
	}

	public static String getSqlLogName() {
		return LOG_NAME_SQL;
	}

	public static void setSqlLogName(String sqlLogName) {
		sqlLogName = Objects.requireNonNull(sqlLogName).trim();
		if (sqlLogName.length() > 0) {
			LOG_NAME_SQL = sqlLogName;
		}
	}

	@Override
	public void log(SumkStatement state, int totalTime, Throwable ex) {
		UnionLog union = UnionLogs.getUnionLog();
		if (!union.isStarted()) {
			return;
		}
		try {
			MapedSql msql = state.getMaped();
			UnsafeStringWriter stringWriter = new UnsafeStringWriter(64);
			JsonWriter writer = new JsonWriter(stringWriter);
			writer.setSerializeNulls(true);
			writer.beginObject();
			writer.name("sql").value(msql.getSql());
			writer.name("hash").value(msql.getSql().hashCode() & Integer.MAX_VALUE);
			String params = DBJson.operator().toJson(msql.getParamters());
			params = LogKits.shorterSubfix(params, DBSettings.maxSqlParamLength());
			writer.name("paramters").value(params);
			writer.name("sqlTime").value(state.getSqlTime());
			if (state.getModifyCount() > -1) {
				writer.name("modifyCount").value(state.getModifyCount());
			}
			if (totalTime > -1 && totalTime != state.getSqlTime()) {
				writer.name("totalTime").value(totalTime);
			}
			writer.endObject();
			writer.flush();
			writer.close();
			CodeLine codeLine = CodeLineKit.parse(SumkStatement.getMarker(), LOG_NAME_SQL);
			LogLevel methodLevel = ex == null ? LogLevel.INFO : LogLevel.ERROR;
			LogObject logObj = new LogObject(LOG_NAME_SQL, SumkDate.now(), methodLevel, stringWriter.toString(), ex,
					Thread.currentThread().getName(), ActionContext.current().logContext(), codeLine);
			union.directOffer(logObj);
		} catch (Exception e) {
			Log.get("sumk.log").error(e.getMessage(), e);
		}
	}

}
