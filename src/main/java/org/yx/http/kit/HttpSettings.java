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
package org.yx.http.kit;

import static org.yx.http.server.HttpMethod.DELETE;
import static org.yx.http.server.HttpMethod.GET;
import static org.yx.http.server.HttpMethod.PATCH;
import static org.yx.http.server.HttpMethod.POST;
import static org.yx.http.server.HttpMethod.PUT;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public final class HttpSettings {

	private static final String DEFAULT_TEST_KEY = "thisIsTest";
	private static int errorHttpStatus;

	private static long httpSessionTimeoutInMs;
	private static boolean cookieEnable;
	private static int maxReqLogSize;
	private static int maxRespLogSize;
	private static int warnTime;
	private static int infoTime;
	private static Charset defaultCharset = StandardCharsets.UTF_8;
	private static int maxHttpBody;
	private static String plainKey;

	private static boolean singleLogin;

	private static String traceHeaderName;

	private static String testKey = DEFAULT_TEST_KEY;

	private static Map<String, String> headers;

	private static List<String> defaultHttpMethods = CollectionUtil.unmodifyList(new String[] { POST, GET });
	private static final List<String> ALL_HTTP_METHODS = CollectionUtil
			.unmodifyList(new String[] { POST, GET, DELETE, PUT, PATCH });
	private static List<String> allHttpMethods = ALL_HTTP_METHODS;

	public static List<String> allHttpMethods() {
		return allHttpMethods;
	}

	public static List<String> defaultHttpMethods() {
		return defaultHttpMethods;
	}

	public static String testKey() {
		return testKey;
	}

	public static int errorHttpStatus() {
		return errorHttpStatus;
	}

	public static int maxHttpBody() {
		return maxHttpBody;
	}

	public static long httpSessionTimeoutInMs() {
		return httpSessionTimeoutInMs;
	}

	public static boolean isCookieEnable() {
		return cookieEnable;
	}

	public static boolean isUploadEnable() {
		return AppInfo.getBoolean("sumk.http.upload.enable", true);
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

	public static boolean isSingleLogin() {
		return singleLogin;
	}

	public static boolean allowPlain(HttpServletRequest request) {
		String plainKey = HttpSettings.plainKey;
		return plainKey != null && plainKey.equals(request.getParameter("plainKey"));
	}

	public static Map<String, String> responseHeaders() {
		return headers;
	}

	static List<String> splitAndTrim(String v) {
		List<String> list = StringUtil.splitAndTrim(v, ",", ";");
		return CollectionUtil.unmodifyList(list.toArray(new String[list.size()]));
	}

	public static void init() {
		HttpSettings.errorHttpStatus = AppInfo.getInt("sumk.http.errorcode", 550);
		String c = AppInfo.get("sumk.http.charset");
		if (StringUtil.isNotEmpty(c)) {
			try {
				HttpSettings.defaultCharset = Charset.forName(c);
			} catch (Exception e) {
				Logs.http().error("{}不是有效的字符集编码", c);
			}

		}
		String methods = AppInfo.getLatin("sumk.http.method.default", null);
		if (methods != null) {
			defaultHttpMethods = splitAndTrim(methods);
		}
		methods = null;
		AppInfo.addObserver(info -> {
			HttpSettings.maxReqLogSize = AppInfo.getInt("sumk.http.log.reqsize", 1000);
			HttpSettings.maxRespLogSize = AppInfo.getInt("sumk.http.log.respsize", 5000);
			HttpSettings.warnTime = AppInfo.getInt("sumk.http.log.warn.time", 3000);
			HttpSettings.infoTime = AppInfo.getInt("sumk.http.log.info.time", 1000);
			HttpSettings.maxHttpBody = AppInfo.getInt("sumk.http.body.maxLength", 1024 * 1024 * 100);
			HttpSettings.singleLogin = AppInfo.getBoolean("sumk.http.session.single", false);
			String plain = AppInfo.get("sumk.http.plain.key", null);
			HttpSettings.plainKey = "".equals(plain) ? null : plain;

			HttpSettings.cookieEnable = AppInfo.getBoolean("sumk.http.header.usecookie", true);
			HttpSettings.httpSessionTimeoutInMs = 1000L * AppInfo.getInt("sumk.http.session.timeout", 60 * 30);
			HttpSettings.traceHeaderName = AppInfo.get("sumk.http.header.trace", "s-trace");

			Map<String, String> map = CollectionUtil.unmodifyMap(AppInfo.subMap("s.http.response.header."));
			HttpSettings.headers = map.isEmpty() ? null : map;
			HttpSettings.testKey = AppInfo.get("sumk.http.testkey", DEFAULT_TEST_KEY);
			String ms = AppInfo.getLatin("sumk.http.method.all", null);
			HttpSettings.allHttpMethods = ms == null ? ALL_HTTP_METHODS : splitAndTrim(ms);
		});
	}

	public static Charset defaultCharset() {
		return defaultCharset;
	}

	public static String traceHeaderName() {
		return traceHeaderName;
	}

}
