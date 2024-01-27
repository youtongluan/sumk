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
package org.yx.base.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;

import org.yx.util.SumkDate;

public class SumkDateQuery implements TemporalQuery<SumkDate> {
	public static final SumkDateQuery inst = new SumkDateQuery();

	@Override
	public SumkDate queryFrom(TemporalAccessor temporal) {
		if (temporal instanceof LocalDateTime) {
			return SumkDate.of((LocalDateTime) temporal);
		} else if (temporal instanceof ZonedDateTime) {
			LocalDateTime dt = ((ZonedDateTime) temporal).toLocalDateTime();
			return SumkDate.of(dt);
		} else if (temporal instanceof OffsetDateTime) {
			LocalDateTime dt = ((OffsetDateTime) temporal).toLocalDateTime();
			return SumkDate.of(dt);
		}
		LocalDate date = temporal.query(TemporalQueries.localDate());
		LocalTime time = temporal.query(TemporalQueries.localTime());
		if (date == null && time == null) {
			return null;
		}
		return SumkDate.of(date, time);
	}

}
