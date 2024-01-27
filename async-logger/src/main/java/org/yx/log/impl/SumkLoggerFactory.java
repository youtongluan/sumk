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
import java.util.function.Function;

import org.slf4j.ILoggerFactory;
import org.yx.log.LogLevel;
import org.yx.log.Loggers;
import org.yx.log.SumkLogger;

public final class SumkLoggerFactory implements ILoggerFactory {
	static final Loggers loggers = Loggers.create("Slf4jLog");
	private static Function<String, SumkLogger> loggerFactory = SumkLoggerImpl::new;
	static {
		LogAppenders.init();
	}

	public static Function<String, SumkLogger> getLoggerFactory() {
		return loggerFactory;
	}

	public static void setLoggerFactory(Function<String, SumkLogger> loggerFactory) {
		SumkLoggerFactory.loggerFactory = Objects.requireNonNull(loggerFactory);
	}

	public static void setDefaultLevel(LogLevel level) {
		if (level != null) {
			Loggers.setDefaultLevel(level);
		}
	}

	public SumkLogger getLogger(String name) {
		SumkLogger log = loggers.get(name);
		if (log != null) {
			return log;
		}
		log = loggerFactory.apply(name);
		SumkLogger oldInstance = loggers.putIfAbsent(name, log);
		return oldInstance == null ? log : oldInstance;
	}
}
