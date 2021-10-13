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
package org.yx.util;

import org.yx.conf.AppInfo;

/**
 * 一般用于国际化或提示语
 */
public final class M {
	public static String get(String name, String defaultV) {
		return AppInfo.get(name, defaultV);
	}

	public static String get(String name, String defaultV, Object param0) {
		String template = AppInfo.get(name, defaultV);
		if (template == null) {
			return null;
		}
		return buildMessage(template, param0);
	}

	public static String get(String name, String defaultV, Object param0, Object param1) {
		String template = AppInfo.get(name, defaultV);
		if (template == null) {
			return null;
		}
		return buildMessage(template, param0, param1);
	}

	public static String get(String name, String defaultV, Object param0, Object param1, Object param2) {
		String template = AppInfo.get(name, defaultV);
		if (template == null) {
			return null;
		}
		return buildMessage(template, param0, param1, param2);
	}

	public static String get(String name, String defaultV, Object... params) {
		String template = AppInfo.get(name, defaultV);
		if (template == null) {
			return null;
		}
		return buildMessage(template, params);
	}

	public static String buildMessage(String template, Object param0) {
		return template.replace("{0}", String.valueOf(param0));
	}

	public static String buildMessage(String template, Object param0, Object param1) {
		return template.replace("{0}", String.valueOf(param0)).replace("{1}", String.valueOf(param1));
	}

	public static String buildMessage(String template, Object param0, Object param1, Object param2) {
		return template.replace("{0}", String.valueOf(param0)).replace("{1}", String.valueOf(param1)).replace("{2}",
				String.valueOf(param2));
	}

	public static String buildMessage(String template, Object... params) {
		String s = template;
		for (int i = 0; i < params.length; i++) {
			s = s.replace("{" + i + "}", String.valueOf(params[i]));
		}
		return s;
	}
}
