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

import org.yx.conf.AppInfo;

public class LogKits {

	public static String clipIfNecessary(String log) {
		if (log == null || log.isEmpty()) {
			return log;
		}
		if (log.length() < 1024) {
			return log;
		}
		int maxLength = AppInfo.getInt("sumk.log.union.singlelog", 10_000_000);
		if (log.length() <= maxLength) {
			return log;
		}
		return log.substring(0, maxLength - 5) + "...";
	}

	public static String shorter(String name, int maxLogNameLength) {
		if (maxLogNameLength < 1) {
			return name;
		}
		if (name == null || name.length() < maxLogNameLength) {
			return name;
		}
		return "..." + name.substring(name.length() - maxLogNameLength + 3);
	}

}
