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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.yx.conf.AppInfo;

public class HttpSettings {
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	public static final int ERROR_HTTP_STATUS = AppInfo.getInt("sumk.http.errorcode", 499);

	public static boolean isCookieEnable() {
		return AppInfo.getBoolean("sumk.http.header.usecookie", true);
	}

	public static long httpSessionTimeoutInMs() {
		return 1000L * AppInfo.getInt("sumk.http.session.timeout", 60 * 30);
	}

	public static boolean isUploadEnable() {
		return AppInfo.getBoolean("sumk.http.upload.enable", true);
	}
}
