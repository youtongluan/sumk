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

import java.util.Collections;
import java.util.Map;

import org.yx.log.ConsoleLog;
import org.yx.log.LogLevel;

public class LeveledDayRollingFileAppender extends DayRollingFileAppender {

	private LogLevel level = LogLevel.INFO;

	public LeveledDayRollingFileAppender() {
		this("level");
	}

	public LeveledDayRollingFileAppender(String name) {
		super(name);
	}

	@Override
	protected boolean accept(LogObject logObject) {
		return logObject.methodLevel.ordinal() >= level.ordinal() && super.accept(logObject);
	}

	@Override
	public void config(Map<String, String> configMap) {
		if (configMap == null) {
			configMap = Collections.emptyMap();
		}
		super.config(configMap);
		String lev = configMap.get("level");
		if (lev == null || lev.isEmpty()) {
			return;
		}
		try {
			this.level = LogLevel.valueOf(lev.toUpperCase());
		} catch (Exception e) {
			ConsoleLog.defaultLog.error("{} is not a valid level name", lev);
		}
	}

}
