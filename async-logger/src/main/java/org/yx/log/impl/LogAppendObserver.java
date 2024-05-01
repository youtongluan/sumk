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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.yx.conf.AppInfo;
import org.yx.conf.SystemConfig;
import org.yx.log.ConsoleLog;
import org.yx.log.LogSettings;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class LogAppendObserver implements Consumer<SystemConfig> {

	@Override
	public void accept(SystemConfig info) {
		LogSettings.updateSettings();
		LogObject.updateCodeLineOnOff();
		Map<String, String> newAppenders = AppInfo.subMap(LogAppenders.LOG_APPENDER);
		for (LogAppender append : LogAppenders.logAppenders) {
			String v = newAppenders.remove(append.name());
			if (v == null || v.isEmpty()) {
				try {
					append.stop();
				} catch (Exception e) {
					ConsoleLog.defaultLog.error(e.toString(), e);
				}
				continue;
			}
			v = StringUtil.toLatin(v);
			Map<String, String> map = CollectionUtil.loadMapFromText(v, ";", ":");
			append.config(map);
		}

		if (newAppenders.isEmpty()) {
			return;
		}
		List<LogAppender> appends = new ArrayList<>();
		for (LogAppender append : LogAppenders.logAppenders) {
			appends.add(append);
		}
		if (LogAppenders.isStarted()) {
			ConsoleLog.defaultLog.info("find new appends:{}", newAppenders);
		}
		for (Entry<String, String> entry : newAppenders.entrySet()) {
			String k = entry.getKey();
			String p = entry.getValue();
			LogAppender appender = LogAppenders.startAppender(k, p);
			if (appender != null) {
				appends.add(appender);
			}

		}
		LogAppenders.logAppenders = appends.toArray(new LogAppender[appends.size()]);
	}

}
