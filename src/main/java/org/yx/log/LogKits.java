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

public final class LogKits {

	public static String shorterPrefix(String name, int maxLogNameLength) {
		if (maxLogNameLength < 5 || name == null || name.length() <= maxLogNameLength) {
			return name;
		}
		return "..".concat(name.substring(name.length() - maxLogNameLength + 2));
	}

	public static String shorterSubfix(String text, int maxLogNameLength) {
		if (maxLogNameLength < 5 || text == null || text.length() <= maxLogNameLength) {
			return text;
		}
		return text.substring(0, maxLogNameLength - 2).concat("..");
	}

}
