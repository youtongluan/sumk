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

import org.slf4j.Logger;
import org.yx.conf.AppInfo;
import org.yx.log.ConsoleLog;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class LogAppenders {
	static final String LOG_APPENDER = "s.log.";
	public static final String MODULE = "module";
	public static final String PATH = "path";
	static LogAppender[] logAppenders = new LogAppender[0];
	private static boolean started;
	static final Logger consoleLog = ConsoleLog.get("sumk.log");

	static LogAppender startAppender(String name, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		value = StringUtil.toLatin(value);
		try {
			return LogAppenderFactory.start(name, CollectionUtil.loadMapFromText(value, ";", ":"));
		} catch (Throwable e) {
			System.err.println("appender [" + name + "] = " + value + " create failed");
			e.printStackTrace();
			return null;
		}
	}

	static synchronized void init() {
		if (started) {
			return;
		}
		started = true;
		try {
			LogAppenderFactory.init();
			AppInfo.addObserver(new LogAppendObserver());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean offer(LogObject logObject) {
		boolean output = false;
		for (LogAppender log : logAppenders) {
			if (log.offer(logObject)) {
				output = true;
			}
		}
		if (UnionLogs.getUnionLog().offer(logObject)) {
			return true;
		}
		return output;
	}

	public static boolean isStarted() {
		return started;
	}

}
