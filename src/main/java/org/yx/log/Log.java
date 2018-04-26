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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yx.conf.AppInfo;

public abstract class Log {
	static {
		try {
			String logType = AppInfo.get("sumk.log.type", LogType.console.name());
			setLogType(LogType.valueOf(logType.trim().toLowerCase()));
		} catch (Exception e) {
			ConsoleLog.get("sumk.log").error(e.getMessage(), e);
		}
	}

	private static LogType logType;

	public static void setLogType(LogType type) {
		Log.logType = type;
	}

	public static boolean isTraceEnable(String module) {
		return get(module).isTraceEnabled();
	}

	public static Logger get(Class<?> clz) {
		String name = clz.getName();
		if (name.startsWith("org.yx.")) {
			name = "sumk" + name.substring(6);
		}
		return get(name);
	}

	public static Logger get(Class<?> clz, Object id) {
		String name = clz.getName();
		if (name.startsWith("org.yx.")) {
			name = "sumk" + name.substring(6);
		}
		return get(name + "." + String.valueOf(id));
	}

	public static Logger get(String module) {
		if (module == null || module.isEmpty()) {
			module = "root";
		}

		if (logType == null || logType == LogType.console) {
			return ConsoleLog.getLogger(module);
		}
		return LoggerFactory.getLogger(module);
	}

	/**
	 * 这个方法是框架专用，开发人员不要调用
	 * 
	 * @param e
	 *            异常
	 */
	public static void printStack(Throwable e) {
		get("sumk.error").error(e.getMessage(), e);
	}

	public static void printStack(String module, Throwable e) {
		get(module).error(e.getMessage(), e);
	}

	public static boolean isON(Logger log) {
		return SumkLogger.class.isInstance(log) && SumkLogger.class.cast(log).isON();
	}

}
