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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Marker;
import org.yx.annotation.doc.NotNull;
import org.yx.base.context.ActionContext;
import org.yx.base.context.LogContext;
import org.yx.conf.AppInfo;
import org.yx.log.LogKits;
import org.yx.log.LogLevel;
import org.yx.log.LogSettings;
import org.yx.log.SumkLogger;
import org.yx.util.SumkDate;

public class LogObject {
	public static final char LN = '\n';
	public static final Charset CHARSET = StandardCharsets.UTF_8;
	private static boolean codelineEnable = true;

	@NotNull
	final SumkDate logDate;

	@NotNull
	final LogLevel methodLevel;

	final String body;

	@NotNull
	final String threadName;

	final Throwable exception;

	@NotNull
	final String loggerName;

	final CodeLine codeLine;

	final LogContext logContext;

	public static LogObject create(Marker marker, LogLevel methodLevel, String message, Throwable e,
			SumkLogger logger) {

		String loggerName = logger.getName();
		CodeLine codeLine = null;
		if (codelineEnable && (!loggerName.startsWith("sumk.") || marker != null)) {
			codeLine = CodeLineKit.parse(marker, loggerName);
		}
		return new LogObject(loggerName, SumkDate.now(), methodLevel,
				LogKits.shorterSubfix(message, LogSettings.maxBodyLength()), e, Thread.currentThread().getName(),
				ActionContext.current().logContext(), codeLine);
	}

	public LogObject(@NotNull String loggerName, @NotNull SumkDate logDate, @NotNull LogLevel methodLevel, String body,
			Throwable exception, @NotNull String threadName, LogContext logContext, CodeLine codeLine) {
		this.logDate = logDate;
		this.methodLevel = methodLevel;
		this.body = body;
		this.exception = exception;
		this.threadName = threadName;
		this.logContext = logContext != null ? logContext : LogContext.empty();
		this.loggerName = loggerName;
		this.codeLine = codeLine;
	}

	public String spanId() {
		return logContext.spanId;
	}

	public String traceId() {
		return logContext.traceId;
	}

	public String userId() {
		return logContext.userId;
	}

	public boolean isTest() {
		return logContext.test;
	}

	public Map<String, String> attachments() {
		return logContext.unmodifiedAttachs();
	}

	public SumkDate getLogDate() {
		return logDate;
	}

	public LogLevel getMethodLevel() {
		return methodLevel;
	}

	public String getBody() {
		return body;
	}

	public String getThreadName() {
		return threadName;
	}

	public Throwable getException() {
		return exception;
	}

	public String getLoggerName() {
		return this.loggerName;
	}

	public CodeLine getCodeLine() {
		return codeLine;
	}

	public LogContext getLogContext() {
		return logContext;
	}

	static void updateCodeLineOnOff() {
		codelineEnable = AppInfo.getBoolean("sumk.log.codeline", true);
	}

}
