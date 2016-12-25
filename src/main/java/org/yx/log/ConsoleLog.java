package org.yx.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.helpers.MarkerIgnoringBase;

public class ConsoleLog extends MarkerIgnoringBase {

	private static final long serialVersionUID = -3870429620672951725L;

	public static final byte OFF = 1;
	public static final byte ERROR = 3;
	public static final byte WARN = 4;
	public static final byte INFO = 5;
	public static final byte DEBUG = 7;
	public static final byte TRACE = 9;
	public static final byte ON = 100;
	private static final String LEVEL_ERROR = "ERROR";
	private static final String LEVEL_WARN = "WARN";
	private static final String LEVEL_INFO = "INFO";
	private static final String LEVEL_DEBUG = "DEBUG";
	private static final String LEVEL_TRACE = "TRACE";

	private static byte DEFAULT_LEVEL = INFO;
	private byte _level = -1;
	private static Map<String, ConsoleLog> map = new ConcurrentHashMap<>();
	private static ConsoleLog root = new ConsoleLog("sumk");

	public static ConsoleLog get(String module) {
		if (module == null) {
			return root;
		}
		module = module.trim();
		ConsoleLog log = map.get(module);
		if (log != null) {
			return log;
		}
		if (module.isEmpty() || module.equals(root.name)) {
			return root;
		}
		log = new ConsoleLog(module);
		map.putIfAbsent(module, log);
		return map.get(module);
	}

	private static byte getLevel(String logName) {
		int index = logName.lastIndexOf(".");
		while (index > 0) {
			logName = logName.substring(0, index);
			ConsoleLog log = map.get(logName);
			if (log != null && log._level > 0) {
				return log._level;
			}
			index = logName.lastIndexOf(".");
		}
		return DEFAULT_LEVEL;
	}

	private byte getLevel() {
		if (this._level > 0) {
			return this._level;
		}
		if (this.name == null) {
			return DEFAULT_LEVEL;
		}
		String logName = this.name;
		return getLevel(logName);
	}

	public static void setDefaultLevel(byte level) {
		if (level > -1) {
			DEFAULT_LEVEL = level;
		}
	}

	private final String name;

	private ConsoleLog(String module) {
		name = module;
	}

	protected String buildMessage(String msg, Object... args) {
		if (msg == null) {
			return msg;
		}
		String[] tmps = msg.split("\\{\\}", -1);
		if (tmps.length < 2) {
			return msg;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tmps.length - 1; i++) {
			sb.append(tmps[i]);
			sb.append(args.length > i ? String.valueOf(args[i]) : "{}");
		}
		sb.append(tmps[tmps.length - 1]);
		return sb.toString();
	}

	protected void show(String level, String msg, Object... args) {
		msg = this.buildMessage(msg, args);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println(format.format(new Date()) + " [" + Thread.currentThread().getName() + "] " + level + " "
				+ shorter(name) + " - " + msg);
	}

	private String shorter(String name) {
		if (name == null || name.length() < 40) {
			return name;
		}
		return "..." + name.substring(name.length() - 35);
	}

	public void setLevel(byte level) {
		this._level = level;
	}

	public void debug(String msg, Object... args) {
		if (getLevel() < DEBUG) {
			return;
		}
		show(LEVEL_DEBUG, msg, args);
	}

	public void trace(String msg, Object... args) {
		if (getLevel() < TRACE) {
			return;
		}
		show(LEVEL_TRACE, msg, args);
	}

	public void info(String msg, Object... args) {
		if (getLevel() < INFO) {
			return;
		}
		show(LEVEL_INFO, msg, args);
	}

	public void trace(Object msg) {
		if (getLevel() < TRACE) {
			return;
		}
		show(LEVEL_TRACE, String.valueOf(msg));
	}

	@Override
	public boolean isTraceEnabled() {
		return this.getLevel() >= TRACE;
	}

	@Override
	public void trace(String msg) {
		if (getLevel() < TRACE) {
			return;
		}
		show(LEVEL_TRACE, msg);
	}

	@Override
	public void trace(String format, Object arg) {
		if (getLevel() < TRACE) {
			return;
		}
		show(LEVEL_TRACE, format, arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		if (getLevel() < TRACE) {
			return;
		}
		show(LEVEL_TRACE, format, arg1, arg2);
	}

	@Override
	public void trace(String msg, Throwable t) {
		if (getLevel() < TRACE) {
			return;
		}
		t.printStackTrace();
	}

	@Override
	public boolean isDebugEnabled() {
		return this.getLevel() >= DEBUG;
	}

	@Override
	public void debug(String msg) {
		if (getLevel() < DEBUG) {
			return;
		}
		show(LEVEL_DEBUG, msg);
	}

	@Override
	public void debug(String format, Object arg) {
		if (getLevel() < DEBUG) {
			return;
		}
		show(LEVEL_DEBUG, format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		if (getLevel() < DEBUG) {
			return;
		}
		show(LEVEL_DEBUG, format, arg1, arg2);
	}

	@Override
	public void debug(String msg, Throwable t) {
		if (getLevel() < DEBUG) {
			return;
		}
		t.printStackTrace();
	}

	@Override
	public boolean isInfoEnabled() {
		return this.getLevel() >= INFO;
	}

	@Override
	public void info(String msg) {
		if (getLevel() < INFO) {
			return;
		}
		show(LEVEL_INFO, msg);
	}

	@Override
	public void info(String format, Object arg) {
		if (getLevel() < INFO) {
			return;
		}
		show(LEVEL_INFO, format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		if (getLevel() < INFO) {
			return;
		}
		show(LEVEL_INFO, format, arg1, arg2);
	}

	@Override
	public void info(String msg, Throwable t) {
		if (getLevel() < INFO) {
			return;
		}
		t.printStackTrace();
	}

	@Override
	public boolean isWarnEnabled() {
		return this.getLevel() >= WARN;
	}

	@Override
	public void warn(String msg) {
		if (getLevel() < WARN) {
			return;
		}
		show(LEVEL_WARN, msg);
	}

	@Override
	public void warn(String format, Object arg) {
		if (getLevel() < WARN) {
			return;
		}
		show(LEVEL_WARN, format, arg);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		if (getLevel() < WARN) {
			return;
		}
		show(LEVEL_WARN, format, arg1, arg2);
	}

	@Override
	public void warn(String msg, Throwable t) {
		if (getLevel() < WARN) {
			return;
		}
		t.printStackTrace();
	}

	@Override
	public void warn(String format, Object... arguments) {
		if (getLevel() < WARN) {
			return;
		}
		show(LEVEL_WARN, format, arguments);
	}

	@Override
	public boolean isErrorEnabled() {
		return this.getLevel() >= ERROR;
	}

	@Override
	public void error(String msg) {
		if (getLevel() < ERROR) {
			return;
		}
		show(LEVEL_ERROR, msg);
	}

	@Override
	public void error(String format, Object arg) {
		if (getLevel() < ERROR) {
			return;
		}
		show(LEVEL_ERROR, format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		if (getLevel() < ERROR) {
			return;
		}
		show(LEVEL_ERROR, format, arg1, arg2);
	}

	@Override
	public void error(String msg, Throwable t) {
		if (getLevel() < ERROR) {
			return;
		}
		t.printStackTrace();
	}

	@Override
	public void error(String format, Object... arguments) {
		if (getLevel() < ERROR) {
			return;
		}
		show(LEVEL_ERROR, format, arguments);
	}

	public static boolean isEnable(byte level) {
		return DEFAULT_LEVEL >= level;
	}
}

class CodeInfo {
	String clz;
	String method;
	int line;

}
