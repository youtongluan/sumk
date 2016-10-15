package org.yx.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Log {
	public static final byte OFF = 1;
	public static final byte ERROR = 3;
	public static final byte INFO = 5;
	public static final byte DEBUG = 7;
	public static final byte TRACE = 9;
	public static final byte ON = 100;
	private static final String LEVEL_ERROR = "ERROR";
	private static final String LEVEL_INFO = "INFO";
	private static final String LEVEL_DEBUG = "DEBUG";
	private static final String LEVEL_TRACE = "TRACE";

	private static byte DEFAULT_LEVEL = INFO;
	private byte _level = -1;
	private static Map<String, Log> map = new ConcurrentHashMap<>();
	private static Log root = new Log("ROOT");

	public static boolean isTraceEnable(String module) {
		byte le = getLevel(module);
		return le >= Log.TRACE;
	}

	private static byte getLevel(String logName) {
		int index = logName.lastIndexOf(".");
		while (index > 0) {
			logName = logName.substring(0, index);
			Log log = map.get(logName);
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

	private Log(String module) {
		name = module;
	}

	public static Log get(Class<?> clz) {
		return get(clz.getName());
	}

	public static Log get(Class<?> clz, Object id) {
		return get(clz.getName() + "." + String.valueOf(id));
	}

	public Log sub(Object id) {
		return get(this.name + "." + String.valueOf(id));
	}

	public static Log get(String module) {
		if (module == null) {
			return root;
		}
		module = module.trim();
		Log log = map.get(module);
		if (log != null) {
			return log;
		}
		if (module.isEmpty() || module.equals(root.name)) {
			return root;
		}
		log = new Log(module);
		map.putIfAbsent(module, log);
		return map.get(module);
	}

	private void show(String level, String msg, Object... args) {
		for (Object arg : args) {
			msg = msg.replaceFirst("\\{\\}", arg == null ? "null" : arg.toString());
		}
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

	public void debug(Object msg) {
		if (getLevel() < DEBUG) {
			return;
		}
		show(LEVEL_DEBUG, String.valueOf(msg));
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

	public boolean isEnable(byte level) {
		return level <= getLevel();
	}

	public void setDebug() {
		this.setLevel(DEBUG);
	}

	public void info(Object msg) {
		if (getLevel() < INFO) {
			return;
		}
		show(LEVEL_INFO, String.valueOf(msg));
	}

	public void error(String msg) {
		if (getLevel() < ERROR) {
			return;
		}
		show(LEVEL_ERROR, msg);
	}

	public void error(String msg, Throwable e) {
		if (getLevel() < ERROR) {
			return;
		}
		show(LEVEL_ERROR, msg);
		printStack(e);

	}

	public static void printStack(Throwable e) {
		if (DEFAULT_LEVEL < ERROR) {
			return;
		}
		e.printStackTrace();
	}

}

class CodeInfo {
	String clz;
	String method;
	int line;

}
