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

import org.slf4j.helpers.MarkerIgnoringBase;

public abstract class SumkLogger extends MarkerIgnoringBase {

	private static final long serialVersionUID = 1;

	protected abstract Loggers loggers();

	protected SumkLogger(String module) {
		this.name = module;
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

	protected static String shorter(String name) {
		if (name == null || name.length() < 40) {
			return name;
		}
		return "..." + name.substring(name.length() - 35);
	}

	protected boolean isLogable(LogLevel methodLevel) {
		return methodLevel.ordinal() >= loggers().getLevel(this).ordinal();
	}

	protected abstract void output(LogLevel methodLevel, String msg);

	protected abstract void output(LogLevel methodLevel, String format, Object... arguments);

	protected abstract void output(LogLevel methodLevel, String msg, Throwable e);

	private void log(LogLevel methodLevel, String msg) {
		if (this.isLogable(methodLevel)) {
			this.output(methodLevel, msg);
		}
	}

	private void log(LogLevel methodLevel, String format, Object... arguments) {
		if (this.isLogable(methodLevel)) {
			this.output(methodLevel, format, arguments);
		}
	}

	private void log(LogLevel methodLevel, String msg, Throwable t) {
		if (this.isLogable(methodLevel)) {
			this.output(methodLevel, msg, t);
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

	protected static class CodeInfo {
		String clz;
		String method;
		int line;

	}

	public boolean isON() {
		return loggers().getLevel(this) == LogLevel.ON;
	}

}