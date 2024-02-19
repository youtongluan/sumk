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
package org.yx.log.impl;

import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;
import org.yx.exception.SumkException;
import org.yx.log.CodeLineMarker;
import org.yx.log.LogKits;
import org.yx.log.LogLevel;
import org.yx.log.LogSettings;
import org.yx.log.SumkLogger;

public class SumkLoggerImpl extends SumkLogger implements LocationAwareLogger {

	public SumkLoggerImpl(String module) {
		super(module);
	}

	private LogLevel fromLocationAwareLoggerInt(int logger_int) {
		switch (logger_int) {
		case LocationAwareLogger.TRACE_INT:
			return LogLevel.TRACE;
		case LocationAwareLogger.DEBUG_INT:
			return LogLevel.DEBUG;
		case LocationAwareLogger.INFO_INT:
			return LogLevel.INFO;
		case LocationAwareLogger.WARN_INT:
			return LogLevel.WARN;
		case LocationAwareLogger.ERROR_INT:
			return LogLevel.ERROR;
		default:
			throw new SumkException(34523521, logger_int + "不是有效的日志级别");
		}
	}

	@Override
	protected void output(Marker marker, LogLevel methodLevel, String format, Object... arguments) {
		try {
			String msg = LogKits.buildMessage(format, arguments);
			LogObject logObject = LogObject.create(marker, methodLevel, msg, null, this);

			if (!LogAppenders.offer(logObject) || LogSettings.consoleEnable()) {
				System.out.print(LogHelper.plainMessage(logObject, LogSettings.showAttach()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void output(Marker marker, LogLevel methodLevel, String msg, Throwable e) {
		try {
			LogObject logObject = LogObject.create(marker, methodLevel, msg, e, this);
			if (!LogAppenders.offer(logObject) || LogSettings.consoleEnable()) {
				System.err.print(LogHelper.plainMessage(logObject, LogSettings.showAttach()));
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public void log(Marker marker, String fqcn, int level, String message, Object[] argArray, Throwable t) {
		try {
			LogLevel methodLevel = this.fromLocationAwareLoggerInt(level);
			if (!this.isLogable(methodLevel)) {
				return;
			}
			String msg = LogKits.buildMessage(message, argArray);
			LogObject logObject = LogObject.create(new CodeLineMarker(fqcn), methodLevel, msg, t, this);

			if (!LogAppenders.offer(logObject) || LogSettings.consoleEnable()) {
				System.out.print(LogHelper.plainMessage(logObject, LogSettings.showAttach()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
