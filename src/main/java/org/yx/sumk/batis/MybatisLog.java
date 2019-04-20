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
package org.yx.sumk.batis;

import java.util.Objects;

import org.apache.ibatis.logging.LogFactory;
import org.slf4j.Logger;
import org.yx.log.CodeLineMarker;
import org.yx.log.Log;

public class MybatisLog implements org.apache.ibatis.logging.Log {

	private static CodeLineMarker marker = new CodeLineMarker("org.yx.sumk.batis.");

	public static CodeLineMarker getMarker() {
		return marker;
	}

	public static void setMarker(CodeLineMarker marker) {
		MybatisLog.marker = Objects.requireNonNull(marker);
	}

	private Logger log;

	public MybatisLog(String clazz) {
		log = Log.get(clazz);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	@Override
	public void error(String s, Throwable e) {
		log.error(marker, s, e);
	}

	@Override
	public void error(String s) {
		log.error(marker, s);
	}

	@Override
	public void debug(String s) {
		log.debug(marker, s);
	}

	@Override
	public void trace(String s) {
		log.trace(marker, s);
	}

	@Override
	public void warn(String s) {
		log.info(marker, s);
	}

	public static void enableMybatisLog() {
		LogFactory.useCustomLogging(MybatisLog.class);
	}

}
