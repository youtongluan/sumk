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

import static org.yx.log.LogLevel.DEBUG;
import static org.yx.log.LogLevel.ERROR;
import static org.yx.log.LogLevel.INFO;
import static org.yx.log.LogLevel.TRACE;
import static org.yx.log.LogLevel.WARN;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.yx.conf.AppInfo;

public abstract class SumkLogger implements Logger {

	protected final String name;

	/**
	 * 不为null，也不以.开头
	 */
	public String getName() {
		return name;
	}

	public int maxLogNameLength() {
		return AppInfo.getInt("sumk.log.maxLogNameLength", 32);
	}

	protected SumkLogger(String module) {
		this.name = parseName(module);
	}

	private String parseName(String module) {
		if (module == null) {
			return "";
		}
		while (module.startsWith(".")) {
			module = module.substring(1);
		}
		return module;
	}

	protected String buildMessage(String msg, Object... args) {
		if (msg == null || args == null) {
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

	protected boolean isLogable(LogLevel methodLevel) {
		return methodLevel.ordinal() >= Loggers.getLevel(this).ordinal();
	}

	protected abstract void output(Marker marker, LogLevel methodLevel, String format, Object... arguments);

	protected abstract void output(Marker marker, LogLevel methodLevel, String msg, Throwable e);

	private void log(LogLevel methodLevel, String msg) {
		if (this.isLogable(methodLevel)) {
			this.output(null, methodLevel, msg);
		}
	}

	private void log(LogLevel methodLevel, String format, Object... arguments) {
		if (this.isLogable(methodLevel)) {
			this.output(null, methodLevel, format, arguments);
		}
	}

	private void log(LogLevel methodLevel, String msg, Throwable t) {
		if (this.isLogable(methodLevel)) {
			this.output(null, methodLevel, msg, t);
		}
	}

	private void log(Marker marker, LogLevel methodLevel, String msg) {
		if (this.isLogable(methodLevel)) {
			this.output(marker, methodLevel, msg);
		}
	}

	private void log(Marker marker, LogLevel methodLevel, String format, Object... arguments) {
		if (this.isLogable(methodLevel)) {
			this.output(marker, methodLevel, format, arguments);
		}
	}

	private void log(Marker marker, LogLevel methodLevel, String msg, Throwable t) {
		if (this.isLogable(methodLevel)) {
			this.output(marker, methodLevel, msg, t);
		}
	}

	@Override
	public boolean isTraceEnabled() {
		return isLogable(TRACE);
	}

	@Override
	public void trace(String msg) {
		this.log(TRACE, msg);
	}

	@Override
	public void trace(String format, Object arg) {
		this.log(TRACE, format, arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		this.log(TRACE, format, arg1, arg2);
	}

	@Override
	public void trace(String format, Object... arguments) {
		this.log(TRACE, format, arguments);
	}

	@Override
	public void trace(String msg, Throwable t) {
		this.log(TRACE, msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return isLogable(DEBUG);
	}

	@Override
	public void debug(String msg) {
		this.log(DEBUG, msg);
	}

	@Override
	public void debug(String format, Object arg) {
		this.log(DEBUG, format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		this.log(DEBUG, format, arg1, arg2);
	}

	@Override
	public void debug(String format, Object... arguments) {
		this.log(DEBUG, format, arguments);
	}

	@Override
	public void debug(String msg, Throwable t) {
		this.log(DEBUG, msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return isLogable(INFO);
	}

	@Override
	public void info(String msg) {
		this.log(INFO, msg);
	}

	@Override
	public void info(String format, Object arg) {
		this.log(INFO, format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		this.log(INFO, format, arg1, arg2);
	}

	@Override
	public void info(String format, Object... arguments) {
		this.log(INFO, format, arguments);
	}

	@Override
	public void info(String msg, Throwable t) {
		this.log(INFO, msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return isLogable(WARN);
	}

	@Override
	public void warn(String msg) {
		this.log(WARN, msg);
	}

	@Override
	public void warn(String format, Object arg) {
		this.log(WARN, format, arg);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		this.log(WARN, format, arg1, arg2);
	}

	@Override
	public void warn(String format, Object... arguments) {
		this.log(WARN, format, arguments);
	}

	@Override
	public void warn(String msg, Throwable t) {
		this.log(WARN, msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return isLogable(ERROR);
	}

	@Override
	public void error(String msg) {
		this.log(ERROR, msg);
	}

	@Override
	public void error(String format, Object arg) {
		this.log(ERROR, format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		this.log(ERROR, format, arg1, arg2);
	}

	@Override
	public void error(String format, Object... arguments) {
		this.log(ERROR, format, arguments);
	}

	@Override
	public void error(String msg, Throwable t) {
		this.log(ERROR, msg, t);
	}

	public boolean isON() {
		return Loggers.getLevel(this) == LogLevel.ON;
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return this.isTraceEnabled();
	}

	@Override
	public void trace(Marker marker, String msg) {
		this.log(marker, TRACE, msg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		this.log(marker, TRACE, format, arg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		this.log(marker, TRACE, format, arg1, arg2);
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		this.log(marker, TRACE, format, argArray);
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		this.log(marker, TRACE, msg, t);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return this.isDebugEnabled();
	}

	@Override
	public void debug(Marker marker, String msg) {
		this.log(marker, DEBUG, msg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		this.log(marker, DEBUG, format, arg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		this.log(marker, DEBUG, format, arg1, arg2);
	}

	@Override
	public void debug(Marker marker, String format, Object... argArray) {
		this.log(marker, DEBUG, format, argArray);
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		this.log(marker, DEBUG, msg, t);
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return this.isInfoEnabled();
	}

	@Override
	public void info(Marker marker, String msg) {
		this.log(marker, INFO, msg);
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		this.log(marker, INFO, format, arg);
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		this.log(marker, INFO, format, arg1, arg2);
	}

	@Override
	public void info(Marker marker, String format, Object... argArray) {
		this.log(marker, INFO, format, argArray);
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		this.log(marker, INFO, msg, t);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return this.isWarnEnabled();
	}

	@Override
	public void warn(Marker marker, String msg) {
		this.log(marker, WARN, msg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		this.log(marker, WARN, format, arg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		this.log(marker, WARN, format, arg1, arg2);
	}

	@Override
	public void warn(Marker marker, String format, Object... argArray) {
		this.log(marker, WARN, format, argArray);
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		this.log(marker, WARN, msg, t);
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return this.isErrorEnabled();
	}

	@Override
	public void error(Marker marker, String msg) {
		this.log(marker, ERROR, msg);
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		this.log(marker, ERROR, format, arg);
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		this.log(marker, ERROR, format, arg1, arg2);
	}

	@Override
	public void error(Marker marker, String format, Object... argArray) {
		this.log(marker, ERROR, format, argArray);
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		this.log(marker, ERROR, msg, t);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + name + "]";
	}

}