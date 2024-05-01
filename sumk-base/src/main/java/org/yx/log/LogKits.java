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

import org.slf4j.spi.LocationAwareLogger;

public final class LogKits {
	private static final String DELIM_STR = "{}";

	public static String shorterPrefix(String name, int maxLogNameLength) {
		if (maxLogNameLength < 5 || name == null || name.length() <= maxLogNameLength) {
			return name;
		}
		return "..".concat(name.substring(name.length() - maxLogNameLength + 2));
	}

	public static String shorterSubfix(String text, int maxLogNameLength) {
		if (maxLogNameLength < 5 || text == null || text.length() <= maxLogNameLength) {
			return text;
		}
		return text.substring(0, maxLogNameLength - 2).concat("..");
	}

	public static String buildMessage(String msg, Object... args) {
		if (msg == null || args == null || args.length == 0 || !msg.contains(DELIM_STR)) {
			return msg;
		}
		StringBuilder sb = new StringBuilder(msg.length() + 50);

		int argIndex = 0;
		int start = 0;
		int index = 0;
		while (argIndex < args.length && (index = msg.indexOf(DELIM_STR, start)) >= 0) {
			int escapeCount = 0;
			for (int i = index - 1; i >= start; i--) {
				if (msg.charAt(i) != '\\') {
					break;
				}
				escapeCount++;
			}
			sb.append(msg.substring(start, index));
			if (escapeCount % 2 == 0) {
				sb.append(String.valueOf(args[argIndex]));
				argIndex++;
			} else {
				sb.append(DELIM_STR);
			}
			start = index + 2;
		}
		if (start < msg.length()) {
			sb.append(msg.substring(start));
		}
		return sb.toString();
	}

	public static LogLevel fromSlf4jLocationAwareLoggerInt(int logger_int) {
		if (logger_int >= LocationAwareLogger.ERROR_INT) {
			return LogLevel.ERROR;
		}
		if (logger_int >= LocationAwareLogger.WARN_INT) {
			return LogLevel.WARN;
		}
		if (logger_int >= LocationAwareLogger.INFO_INT) {
			return LogLevel.INFO;
		}
		if (logger_int >= LocationAwareLogger.DEBUG_INT) {
			return LogLevel.DEBUG;
		}
		return LogLevel.TRACE;
	}
}
