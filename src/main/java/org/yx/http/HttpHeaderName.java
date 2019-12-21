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
package org.yx.http;

import org.yx.conf.AppInfo;

public final class HttpHeaderName {

	public static final String CLIENT_PC = "PC";
	private static String sessionId;
	private static String token;
	private static String type;

	public static void init() {
		sessionId = AppInfo.get("sumk.http.name.sessionId", "sid");
		token = AppInfo.get("sumk.http.name.token", "stoken");
		type = AppInfo.get("sumk.http.name.token", "stype");
	}

	public static String sessionId() {
		return sessionId;
	}

	public static String token() {
		return token;
	}

	public static String type() {
		return type;
	}
}
