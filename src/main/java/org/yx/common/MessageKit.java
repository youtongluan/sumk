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
package org.yx.common;

public final class MessageKit {

	public static String buildMessage(String template, String param0) {
		return template.replace("{0}", param0);
	}

	public static String buildMessage(String template, String param0, String param1) {
		return template.replace("{0}", param0).replace("{1}", param1);
	}

	public static String buildMessage(String template, String... params) {
		String s = template;
		for (int i = 0; i < params.length; i++) {
			s = s.replace("{" + i + "}", params[i]);
		}
		return s;
	}
}
