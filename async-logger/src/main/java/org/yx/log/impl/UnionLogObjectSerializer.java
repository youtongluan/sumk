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

import java.io.IOException;
import java.util.function.Function;

import org.yx.conf.AppInfo;
import org.yx.log.ConsoleLog;

public class UnionLogObjectSerializer implements Function<LogObject, UnionLogObject> {

	private final String appId;

	private int extraSize = 350;

	public UnionLogObjectSerializer() {
		this.appId = AppInfo.appId(null);
	}

	@Override
	public UnionLogObject apply(LogObject log) {
		int estimate = extraSize;
		if (log.body != null) {
			estimate += log.body.length();
		}
		StringBuilder sb = new StringBuilder(estimate);
		try {
			UnionLogUtil.appendLogObject(sb, log, appId);
			return new UnionLogObject(log.loggerName, log.logDate, sb.toString());
		} catch (IOException e) {
			ConsoleLog.defaultLog.error("数据解析出错", e);
			return null;
		}
	}

	public int getExtraSize() {
		return extraSize;
	}

	public void setExtraSize(int extraSize) {
		if (extraSize > 0) {
			this.extraSize = extraSize;
		}
	}
}
