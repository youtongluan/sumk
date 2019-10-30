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
package org.yx.log;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {
	private static LogType logType = LogType.slf4j;

	private Log() {
	}

	public static LogType logType() {
		return logType;
	}

	public static void setLogType(LogType type) {
		logType = Objects.requireNonNull(type);
		ConsoleLog.get("sumk.log").info("set logtype to {}", logType);
	}

	public static boolean isTraceEnable(String module) {
		return get(module).isTraceEnabled();
	}

	public static Logger get(Class<?> clz) {
		return get(clz.getName());
	}

	public static Logger get(String module) {
		if (module == null) {
			module = "";
		}
		if (logType == LogType.slf4j) {
			return LoggerFactory.getLogger(module);
		}
		return DelegateLogger.get(module);
	}

	public static void printStack(String module, Throwable e) {
		get(module).error(e.toString(), e);
	}

	public static boolean isON(Logger log) {
		return SumkLogger.class.isInstance(log) && SumkLogger.class.cast(log).isON();
	}

}
