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

public final class Log {
	private static volatile LogType logType = LogType.slf4j;

	private Log() {
	}

	static {
		try {
			String type = AppInfo.get("sumk.log.type", null);
			if (type != null && type.length() > 0) {
				type = type.trim().toLowerCase();
				Log.setLogType(LogType.valueOf(type));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setLogType(LogType type) {
		logType = type;
		ConsoleLog.get("sumk.log").info("set logtype to {}", logType);
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
		if (logType == LogType.slf4j) {
			return LoggerFactory.getLogger(module);
		}
		return ConsoleLog.getLogger(module);
	}

	/**
	 * 这个方法是框架专用，开发人员不要调用
	 * 
	 * @param e
	 *            异常
	 */
	public static void printStack(Throwable e) {
		get("sumk.error").error(e.toString(), e);
	}

	public static void printStack(String module, Throwable e) {
		get(module).error(e.toString(), e);
	}

	public static boolean isON(Logger log) {
		return SumkLogger.class.isInstance(log) && SumkLogger.class.cast(log).isON();
	}

}
