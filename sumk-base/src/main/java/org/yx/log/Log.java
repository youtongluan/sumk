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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {
	private static final String SUMKBOX = ".sumkbox.";

	private Log() {
	}

	public static boolean isTraceEnable(String module) {
		return get(module).isTraceEnabled();
	}

	public static Logger get(Class<?> clz) {
		String name = clz.getName();
		if (name.contains(SUMKBOX)) {
			int index = name.lastIndexOf(SUMKBOX);
			name = String.join(".", name.substring(0, index), name.substring(index + SUMKBOX.length()));
		}
		return get(name);
	}

	public static Logger get(String module) {
		if (module == null) {
			module = "";
		}
		Logger logger = LoggerFactory.getLogger(module);
		if (isNOPLogger(logger)) {
			return DelegateLogger.get(module);
		}
		return logger;
	}

	public static boolean isNOPLogger(Logger logger) {
		return "NOPLogger".equals(logger.getClass().getSimpleName());
	}

	public static void printStack(String module, Throwable e) {
		get(module).error(e.getLocalizedMessage(), e);
	}

	public static boolean isON(Logger log) {

		return log.isTraceEnabled() && log instanceof SumkLogger && ((SumkLogger) log).isON();
	}

}
