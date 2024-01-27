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

import java.util.Objects;

public class LogHelper {

	private static PlainOutput output = new PlainOutputImpl();

	public static void setOutput(PlainOutput output) {
		LogHelper.output = Objects.requireNonNull(output);
	}

	public static String plainMessage(LogObject logObject, boolean showAttachs) {
		return output.plainMessage(logObject, showAttachs);
	}

	public static void plainMessage(StringBuilder sb, LogObject logObject, boolean showAttachs) {
		output.plainMessage(sb, logObject, showAttachs);
	}

	public static CodeLine extractCodeLine(String pre) {
		if (pre == null || pre.isEmpty()) {
			return null;
		}
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		if (stack != null && stack.length > 2) {
			int i = stack.length - 1;

			for (; i > 0; i--) {
				if (stack[i].getClassName().startsWith(pre)) {
					i++;
					break;
				}
			}
			StackTraceElement s = stack[i];

			if (s.getClassName().contains(".sumkbox.") && (++i) < stack.length) {
				s = stack[i];
			}
			return new CodeLine(s.getClassName(), s.getMethodName(), s.getLineNumber());
		}
		return null;
	}

	public static String realContext(String text, String pattern, String slot) {
		String[] fs = pattern.split(slot, 2);
		if (fs.length != 2) {
			LogAppenders.consoleLog.error("{} should contain and only contain one {}", pattern, slot);
			return null;
		}
		if (fs[0].length() + fs[1].length() > text.length()) {
			return null;
		}
		int end = text.length() - fs[1].length();
		if (!fs[0].equals(text.substring(0, fs[0].length())) || !fs[1].equals(text.substring(end))) {
			return null;
		}
		return text.substring(fs[0].length(), end);
	}

}
