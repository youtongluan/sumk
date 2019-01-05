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

import org.yx.util.SumkDate;

public class ConsoleLog extends SumkLogger {

	private static final long serialVersionUID = 1;
	public static final LogLevel DEBUG = LogLevel.DEBUG;
	public static final LogLevel TRACE = LogLevel.TRACE;
	private static Loggers loggers = Loggers.create();

	public static void setDefaultLevel(LogLevel logLevel) {
		if (logLevel != null) {
			loggers.setDefaultLevel(logLevel);
		}
	}

	public static SumkLogger getLogger(String name) {
		SumkLogger log = loggers.get(name);
		if (log != null) {
			return log;
		}
		log = new ConsoleLog(name);
		SumkLogger oldInstance = loggers.putIfAbsent(name, log);
		return oldInstance == null ? log : oldInstance;
	}

	public static SumkLogger get(String name) {
		return getLogger(name);
	}

	private ConsoleLog(String module) {
		super(module);
	}

	@Override
	protected void output(LogLevel methodLevel, String format, Object... arguments) {
		this.show(methodLevel, format, arguments);
	}

	@Override
	protected void output(LogLevel methodLevel, String msg, Throwable e) {
		StringBuilder sb = new StringBuilder();
		sb.append(SumkDate.now().to_yyyy_MM_dd_HH_mm_ss_SSS()).append(" [");
		sb.append(Thread.currentThread().getName()).append("] ").append(methodLevel).append(" ")
				.append(LogKits.shorter(name, 40)).append(" - ").append(msg);
		System.err.print(sb.toString());
		e.printStackTrace();
	}

	private void show(LogLevel level, String msg, Object... args) {
		msg = this.buildMessage(msg, args);
		StringBuilder sb = new StringBuilder();
		sb.append(SumkDate.now().to_yyyy_MM_dd_HH_mm_ss_SSS()).append(" [");
		sb.append(Thread.currentThread().getName()).append("] ").append(level).append(" ")
				.append(LogKits.shorter(name, 40)).append(" - ").append(msg);
		System.out.println(sb.toString());
	}

	@Override
	protected Loggers loggers() {
		return loggers;
	}

}
