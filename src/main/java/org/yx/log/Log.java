package org.yx.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Log {

	private static LogType logType;

	public static void setLogType(LogType type) {
		Log.logType = type;
	}

	public static boolean isTraceEnable(String module) {
		return get(module).isTraceEnabled();
	}

	public static Logger get(Class<?> clz) {
		return get(clz.getSimpleName());
	}

	public static Logger get(Class<?> clz, Object id) {
		return get(clz.getSimpleName() + "." + String.valueOf(id));
	}

	public static Logger get(String module) {
		if (logType == null || logType == LogType.console) {
			return ConsoleLog.get(module);
		}
		if (module == null || (module = module.trim()).isEmpty()) {
			module = "sumk";
		}
		if (!module.startsWith("sumk")) {
			module = "sumk." + module;
		}
		return LoggerFactory.getLogger(module);
	}

	public static void printStack(Throwable e) {
		get("sumk.error").error(e.getMessage(), e);
	}

	public static void printStack(String module, Throwable e) {
		get(module).error(e.getMessage(), e);
	}
}
