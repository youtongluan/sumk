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

public final class LogSettings {
	private static final int DEFAULT_MAX_BODY_LENGTH = 1500;
	private static final int DEFAULT_MAX_LOG_NAME_LENGTH = 32;

	private static boolean console;
	private static boolean showAttach;
	private static int maxBodyLength = DEFAULT_MAX_BODY_LENGTH;
	private static int maxLogNameLength = DEFAULT_MAX_LOG_NAME_LENGTH;

	public static void updateSettings() {
		console = AppInfo.getBoolean("sumk.log.console", false);
		showAttach = AppInfo.getBoolean("sumk.log.attach.show", true);
		maxBodyLength = AppInfo.getInt("sumk.log.body.maxlength", DEFAULT_MAX_BODY_LENGTH);
		maxLogNameLength = AppInfo.getInt("sumk.log.maxLogNameLength", DEFAULT_MAX_LOG_NAME_LENGTH);
	}

	public static boolean showAttach() {
		return showAttach;
	}

	public static boolean consoleEnable() {
		return console;
	}

	public static int maxBodyLength() {
		return maxBodyLength;
	}

	public static int maxLogNameLength() {
		return maxLogNameLength;
	}

}
