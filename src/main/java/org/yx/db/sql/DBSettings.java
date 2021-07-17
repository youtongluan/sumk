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

import java.util.Objects;
import java.util.function.IntFunction;

import org.yx.conf.AppInfo;
import org.yx.db.enums.DBType;
import org.yx.log.Logs;

public final class DBSettings {
	private static boolean FAIL_IF_PROPERTY_NOT_MAPPED;
	private static boolean FROM_CACHE;
	private static boolean TO_CACHE;
	private static int MAX_LOG_PARAM_LENGTH;

	private static int LIMIT_AS_NO_LIMIT;

	private static int UNION_LOG_TIME;
	private static boolean UNION_LOG_ENABLE;
	private static int DEBUG_LOG_SPEND_TIME;
	private static byte[] PASSWORD_KEY = new byte[] { 121, 111, 117, 116, 111, 110, 103, 108, 117, 97, 110, 64, 115,
			117, 109, 107 };

	private static DBType readType;

	private static int MAX_QUERY_CACHE_SIZE;

	private static SoftDeleteParser softDeleteParser = new SoftDeleteParserImpl();

	private static IntFunction<VisitCounter> visitCounterFactory;

	public static IntFunction<VisitCounter> visitCounterFactory() {
		return visitCounterFactory;
	}

	public static void setVisitCounterFactory(IntFunction<VisitCounter> factory) {
		DBSettings.visitCounterFactory = factory;
	}

	public static SoftDeleteParser softDeleteParser() {
		return softDeleteParser;
	}

	public static void setSoftDeleteParser(SoftDeleteParser parser) {
		DBSettings.softDeleteParser = parser;
	}

	public static int maxQueryCacheSize() {
		return MAX_QUERY_CACHE_SIZE;
	}

	public static DBType readType() {
		return readType;
	}

	public static byte[] getPasswordKey() {
		return PASSWORD_KEY;
	}

	public static void setPasswordKey(byte[] passwordKey) {
		PASSWORD_KEY = Objects.requireNonNull(passwordKey);
	}

	public static int debugLogSpendTime() {
		return DEBUG_LOG_SPEND_TIME;
	}

	public static int unionLogTime() {
		return UNION_LOG_TIME;
	}

	public static boolean isUnionLogEnable() {
		return UNION_LOG_ENABLE;
	}

	public static boolean failIfPropertyNotMapped() {
		return FAIL_IF_PROPERTY_NOT_MAPPED;
	}

	public static boolean fromCache() {
		return FROM_CACHE;
	}

	public static boolean toCache() {
		return TO_CACHE;
	}

	public static int asNoLimit() {
		return LIMIT_AS_NO_LIMIT;
	}

	public static int maxSqlParamLength() {
		return MAX_LOG_PARAM_LENGTH;
	}

	public static synchronized void init() {
		if (DBSettings.readType != null || LIMIT_AS_NO_LIMIT > 0) {
			return;
		}
		AppInfo.addObserver(info -> {
			try {
				FAIL_IF_PROPERTY_NOT_MAPPED = AppInfo.getBoolean("sumk.db.failIfPropertyNotMapped", true);
				FROM_CACHE = AppInfo.getBoolean("sumk.db.fromCache", true);
				TO_CACHE = AppInfo.getBoolean("sumk.db.toCache", true);
				LIMIT_AS_NO_LIMIT = AppInfo.getInt("sumk.db.asnolimit", 5000);
				MAX_LOG_PARAM_LENGTH = AppInfo.getInt("sumk.sql.param.maxlength", 5000);
				UNION_LOG_TIME = AppInfo.getInt("sumk.unionlog.sql.time", 0);
				UNION_LOG_ENABLE = AppInfo.getBoolean("sumk.unionlog.sql.enable", true);
				DEBUG_LOG_SPEND_TIME = AppInfo.getInt("sumk.sql.debug.spendTime", 100);
				MAX_QUERY_CACHE_SIZE = AppInfo.getInt("sumk.select.query.maxsize", 1000);
			} catch (Exception e) {
				Logs.db().info(e.getMessage(), e);
			}
			String read = AppInfo.get("sumk.db.readtype", null);
			if (read != null) {
				try {
					DBSettings.readType = DBType.valueOf(read.toUpperCase());
				} catch (Exception e) {
					Logs.db().info("sumk.db.readtype配置不正确," + e.getMessage(), e);
				}
			} else {
				DBSettings.readType = DBType.ANY;
			}
		});
	}
}
