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
package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.yx.log.impl.SumkLoggerFactory;

public class StaticLoggerBinder implements LoggerFactoryBinder {

	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

	public static final StaticLoggerBinder getSingleton() {
		return SINGLETON;
	}

	public static String REQUESTED_API_VERSION = "1.7.0";

	private final ILoggerFactory loggerFactory;

	private StaticLoggerBinder() {
		loggerFactory = new SumkLoggerFactory();
	}

	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	public String getLoggerFactoryClassStr() {
		return SumkLoggerFactory.class.getName();
	}
}
