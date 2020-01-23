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
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

public final class HttpSettings {

	private static int errorHttpStatus;

	private static Set<String> fusing = Collections.emptySet();
	private static long httpSessionTimeoutInMs;
	private static boolean cookieEnable;
	private static int maxReqLogSize;
	private static int maxRespLogSize;
	private static int warnTime;
	private static int infoTime;
	private static Charset defaultCharset = StandardCharsets.UTF_8;

	public static int getErrorHttpStatus() {
		return errorHttpStatus;
	}

	public static Set<String> getFusing() {
		return fusing;
	}

	public static long getHttpSessionTimeoutInMs() {
		return httpSessionTimeoutInMs;
	}

	public static boolean isCookieEnable() {
		return cookieEnable;
	}

	public static boolean isUploadEnable() {
		return AppInfo.getBoolean("sumk.http.upload.enable", true);
	}

	public static void setCookieEnable(boolean cookieEnable) {
		HttpSettings.cookieEnable = cookieEnable;
	}

	public static void setFusing(Set<String> fusing) {
		HttpSettings.fusing = Objects.requireNonNull(fusing);
	}

	public static void setHttpSessionTimeoutInMs(long httpSessionTimeoutInMs) {
		HttpSettings.httpSessionTimeoutInMs = httpSessionTimeoutInMs;
	}

	public static int maxReqLogSize() {
		return maxReqLogSize;
	}

	public static int maxRespLogSize() {
		return maxRespLogSize;
	}

	public static int warnTime() {
		return warnTime;
	}

	public static int infoTime() {
		return infoTime;
	}

	public static void init() {
		HttpSettings.errorHttpStatus = AppInfo.getInt("sumk.http.errorcode", 499);
		String c = AppInfo.get("sumk.http.charset");
		if (StringUtil.isNotEmpty(c)) {
			try {
				HttpSettings.defaultCharset = Charset.forName(c);
			} catch (Exception e) {
				Logs.http().error("{}不是有效的字符集编码", c);
			}

		}
		AppInfo.addObserver(info -> {
			HttpSettings.maxReqLogSize = AppInfo.getInt("sumk.http.log.reqsize", 1000);
			HttpSettings.maxRespLogSize = AppInfo.getInt("sumk.http.log.respsize", 1000);
			HttpSettings.warnTime = AppInfo.getInt("sumk.http.log.warn", 3000);
			HttpSettings.infoTime = AppInfo.getInt("sumk.http.log.info", 1000);
		});
	}

	public static Charset defaultCharset() {
		return defaultCharset;
	}

}
