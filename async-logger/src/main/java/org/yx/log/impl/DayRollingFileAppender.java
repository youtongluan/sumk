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

import java.time.LocalDate;
import java.util.Objects;

import org.yx.conf.AppInfo;
import org.yx.util.SumkDate;

public class DayRollingFileAppender extends RollingFileAppender {

	public DayRollingFileAppender() {
		super("day");
	}

	public DayRollingFileAppender(String name) {
		super(Objects.requireNonNull(name));
	}

	@Override
	protected boolean shouldDelete(String fileName) {
		String c = LogHelper.realContext(fileName, filePattern, SLOT);
		if (c == null || c.length() != 10) {
			return false;
		}
		try {
			LocalDate logDate = LocalDate.parse(c);
			LocalDate now = LocalDate.now();
			return logDate.isBefore(now.minusDays(AppInfo.getInt("sumk.log.day.max", 30)));
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected String formatDateString(SumkDate date) {
		return date.to_yyyy_MM_dd();
	}

}
