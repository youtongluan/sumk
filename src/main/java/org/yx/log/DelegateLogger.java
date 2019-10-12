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
import org.slf4j.Marker;
import org.yx.main.SumkServer;

public final class DelegateLogger implements Logger {

	private volatile Logger delegate;

	private DelegateLogger(String name) {
		this.delegate = ConsoleLog.get(name);
	}

	public static DelegateLogger get(String name) {
		return new DelegateLogger(name);
	}

	public Logger delegate() {
		if (delegate.getClass() != ConsoleLog.class) {
			return delegate;
		}
		if (SumkServer.isStarted() && Log.logType() == LogType.slf4j) {
			delegate = Log.get(delegate.getName());
			if (delegate.isDebugEnabled()) {
				Log.get("sumk.log").debug("{} change to {}", delegate.getName(), delegate.getClass().getSimpleName());
			}
		}
		return delegate;
	}

	public String getName() {
		return delegate.getName();
	}

	public boolean isTraceEnabled() {
		return delegate().isTraceEnabled();
	}

	public void trace(String msg) {
		delegate().trace(msg);
	}

	public void trace(String format, Object arg) {
		delegate().trace(format, arg);
	}

	public void trace(String format, Object arg1, Object arg2) {
		delegate().trace(format, arg1, arg2);
	}

	public void trace(String format, Object... arguments) {
		delegate().trace(format, arguments);
	}

	public void trace(String msg, Throwable t) {
		delegate().trace(msg, t);
	}

	public boolean isTraceEnabled(Marker marker) {
		return delegate().isTraceEnabled(marker);
	}

	public void trace(Marker marker, String msg) {
		delegate().trace(marker, msg);
	}

	public void trace(Marker marker, String format, Object arg) {
		delegate().trace(marker, format, arg);
	}

	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		delegate().trace(marker, format, arg1, arg2);
	}

	public void trace(Marker marker, String format, Object... arguments) {
		delegate().trace(marker, format, arguments);
	}

	public void trace(Marker marker, String msg, Throwable t) {
		delegate().trace(marker, msg, t);
	}

	public boolean isDebugEnabled() {
		return delegate().isDebugEnabled();
	}

	public void debug(String msg) {
		delegate().debug(msg);
	}

	public void debug(String format, Object arg) {
		delegate().debug(format, arg);
	}

	public void debug(String format, Object arg1, Object arg2) {
		delegate().debug(format, arg1, arg2);
	}

	public void debug(String format, Object... arguments) {
		delegate().debug(format, arguments);
	}

	public void debug(String msg, Throwable t) {
		delegate().debug(msg, t);
	}

	public boolean isDebugEnabled(Marker marker) {
		return delegate().isDebugEnabled(marker);
	}

	public void debug(Marker marker, String msg) {
		delegate().debug(marker, msg);
	}

	public void debug(Marker marker, String format, Object arg) {
		delegate().debug(marker, format, arg);
	}

	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		delegate().debug(marker, format, arg1, arg2);
	}

	public void debug(Marker marker, String format, Object... arguments) {
		delegate().debug(marker, format, arguments);
	}

	public void debug(Marker marker, String msg, Throwable t) {
		delegate().debug(marker, msg, t);
	}

	public boolean isInfoEnabled() {
		return delegate().isInfoEnabled();
	}

	public void info(String msg) {
		delegate().info(msg);
	}

	public void info(String format, Object arg) {
		delegate().info(format, arg);
	}

	public void info(String format, Object arg1, Object arg2) {
		delegate().info(format, arg1, arg2);
	}

	public void info(String format, Object... arguments) {
		delegate().info(format, arguments);
	}

	public void info(String msg, Throwable t) {
		delegate().info(msg, t);
	}

	public boolean isInfoEnabled(Marker marker) {
		return delegate().isInfoEnabled(marker);
	}

	public void info(Marker marker, String msg) {
		delegate().info(marker, msg);
	}

	public void info(Marker marker, String format, Object arg) {
		delegate().info(marker, format, arg);
	}

	public void info(Marker marker, String format, Object arg1, Object arg2) {
		delegate().info(marker, format, arg1, arg2);
	}

	public void info(Marker marker, String format, Object... arguments) {
		delegate().info(marker, format, arguments);
	}

	public void info(Marker marker, String msg, Throwable t) {
		delegate().info(marker, msg, t);
	}

	public boolean isWarnEnabled() {
		return delegate().isWarnEnabled();
	}

	public void warn(String msg) {
		delegate().warn(msg);
	}

	public void warn(String format, Object arg) {
		delegate().warn(format, arg);
	}

	public void warn(String format, Object arg1, Object arg2) {
		delegate().warn(format, arg1, arg2);
	}

	public void warn(String format, Object... arguments) {
		delegate().warn(format, arguments);
	}

	public void warn(String msg, Throwable t) {
		delegate().warn(msg, t);
	}

	public boolean isWarnEnabled(Marker marker) {
		return delegate().isWarnEnabled(marker);
	}

	public void warn(Marker marker, String msg) {
		delegate().warn(marker, msg);
	}

	public void warn(Marker marker, String format, Object arg) {
		delegate().warn(marker, format, arg);
	}

	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		delegate().warn(marker, format, arg1, arg2);
	}

	public void warn(Marker marker, String format, Object... arguments) {
		delegate().warn(marker, format, arguments);
	}

	public void warn(Marker marker, String msg, Throwable t) {
		delegate().warn(marker, msg, t);
	}

	public boolean isErrorEnabled() {
		return delegate().isErrorEnabled();
	}

	public void error(String msg) {
		delegate().error(msg);
	}

	public void error(String format, Object arg) {
		delegate().error(format, arg);
	}

	public void error(String format, Object arg1, Object arg2) {
		delegate().error(format, arg1, arg2);
	}

	public void error(String format, Object... arguments) {
		delegate().error(format, arguments);
	}

	public void error(String msg, Throwable t) {
		delegate().error(msg, t);
	}

	public boolean isErrorEnabled(Marker marker) {
		return delegate().isErrorEnabled(marker);
	}

	public void error(Marker marker, String msg) {
		delegate().error(marker, msg);
	}

	public void error(Marker marker, String format, Object arg) {
		delegate().error(marker, format, arg);
	}

	public void error(Marker marker, String format, Object arg1, Object arg2) {
		delegate().error(marker, format, arg1, arg2);
	}

	public void error(Marker marker, String format, Object... arguments) {
		delegate().error(marker, format, arguments);
	}

	public void error(Marker marker, String msg, Throwable t) {
		delegate().error(marker, msg, t);
	}
}
