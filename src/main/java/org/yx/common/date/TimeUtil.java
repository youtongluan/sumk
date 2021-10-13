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
package org.yx.common.date;

import static org.yx.exception.SumkExceptionCode.DATETIME_CONVERT;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.yx.exception.SumkException;
import org.yx.util.SumkDate;

public class TimeUtil {

	public static boolean isGenericDate(Class<?> type) {
		return type == Date.class || type == java.sql.Date.class || type == Time.class || type == Timestamp.class
				|| type == LocalDate.class || type == LocalTime.class || type == LocalDateTime.class
				|| type == SumkDate.class;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toType(Object v, Class<?> type, boolean failIfNotSupport) {
		if (v.getClass() == type) {
			return (T) v;
		}
		if (type == SumkDate.class) {
			return toSumkDate(v, failIfNotSupport);
		}
		if (v instanceof Date) {
			return toType((Date) v, type, failIfNotSupport);
		}
		Class<?> sourceClz = v.getClass();
		if (LocalDateTime.class == sourceClz) {
			return toType((LocalDateTime) v, type, failIfNotSupport);
		}

		if (LocalDate.class == sourceClz) {
			return toType((LocalDate) v, type, failIfNotSupport);
		}

		if (LocalTime.class == sourceClz) {
			return toType((LocalTime) v, type, failIfNotSupport);
		}
		if (SumkDate.class == sourceClz) {
			return toType((SumkDate) v, type, failIfNotSupport);
		}

		if (failIfNotSupport || !isGenericDate(type)) {
			throw new SumkException(1234345, type.getClass().getName() + " cannot convert to " + type.getName());
		}
		return (T) v;
	}

	@SuppressWarnings("unchecked")
	private static <T> T toSumkDate(Object v, boolean failIfNotSupport) {
		if (v instanceof Date) {
			return (T) SumkDate.of((Date) v);
		}
		if (v instanceof LocalDateTime) {
			return (T) SumkDate.of((LocalDateTime) v);
		}
		if (v instanceof LocalDate) {
			return (T) SumkDate.of((LocalDate) v, null);
		}
		if (v instanceof LocalTime) {
			return (T) SumkDate.of(null, (LocalTime) v);
		}
		if (failIfNotSupport) {
			throw new SumkException(63414353, v.getClass().getName() + "is not supported Date type");
		}
		return (T) v;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private static <T> T toType(SumkDate v, Class<?> type, boolean failIfNotSupport) {
		if (Date.class == type) {
			return (T) v.toDate();
		}
		if (java.sql.Date.class == type) {
			return (T) java.sql.Date.valueOf(v.toLocalDate());
		}

		if (Timestamp.class == type) {
			return (T) v.toTimestamp();
		}

		if (Time.class == type) {
			return (T) new Time(v.getHour(), v.getMinute(), v.getSecond());
		}

		if (LocalDate.class == type) {
			return (T) v.toLocalDate();
		}

		if (LocalDateTime.class == type) {
			return (T) v.toLocalDateTime();
		}

		if (LocalTime.class == type) {
			return (T) v.toLocalTime();
		}
		if (failIfNotSupport || !isGenericDate(type)) {
			throw new SumkException(63414353, type.getClass().getName() + "is not supported Date type");
		}
		return (T) v;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private static <T> T toType(Date v, Class<?> type, boolean failIfNotSupport) {
		long time = v.getTime();
		if (Date.class == type) {
			return (T) new Date(time);
		}
		if (java.sql.Date.class == type) {
			if (Time.class == v.getClass()) {
				throw new SumkException(DATETIME_CONVERT, "Time cannot convert to java.sql.Date");
			}
			return (T) new java.sql.Date(v.getYear(), v.getMonth(), v.getDate());
		}

		if (Timestamp.class == type) {
			return (T) new Timestamp(time);
		}

		if (Time.class == type) {
			if (java.sql.Date.class == v.getClass()) {
				throw new SumkException(DATETIME_CONVERT, "java.sql.Date cannot convert to Time");
			}
			return (T) new Time(v.getHours(), v.getMinutes(), v.getSeconds());
		}
		SumkDate sumk = SumkDate.of(v);

		if (LocalDate.class == type) {
			if (Time.class == v.getClass()) {
				throw new SumkException(DATETIME_CONVERT, "Time cannot convert to LocalDate");
			}
			return (T) sumk.toLocalDate();
		}

		if (LocalDateTime.class == type) {
			return (T) sumk.toLocalDateTime();
		}

		if (LocalTime.class == type) {
			if (java.sql.Date.class == v.getClass()) {
				throw new SumkException(DATETIME_CONVERT, "java.sql.Date cannot convert to LocalTime");
			}
			return (T) sumk.toLocalTime();
		}
		if (failIfNotSupport || !isGenericDate(type)) {
			throw new SumkException(63414353, type.getClass().getName() + "is not supported Date type");
		}
		return (T) v;
	}

	@SuppressWarnings("unchecked")
	private static <T> T toType(LocalDateTime v, Class<?> type, boolean failIfNotSupport) {
		if (Date.class == type) {
			return (T) Date.from(v.atZone(ZoneId.systemDefault()).toInstant());
		}
		if (java.sql.Date.class == type) {
			return (T) java.sql.Date.valueOf(v.toLocalDate());
		}

		if (Timestamp.class == type) {
			return (T) Timestamp.valueOf(v);
		}

		if (Time.class == type) {
			return (T) Time.valueOf(v.toLocalTime());
		}

		if (LocalDate.class == type) {
			return (T) v.toLocalDate();
		}

		if (LocalTime.class == type) {
			return (T) v.toLocalTime();
		}

		if (failIfNotSupport || !isGenericDate(type)) {
			throw new SumkException(DATETIME_CONVERT, type.getClass().getName() + "is not a supported Date type");
		}
		return (T) v;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private static <T> T toType(LocalDate v, Class<?> type, boolean failIfNotSupport) {
		if (Date.class == type) {
			return (T) new Date(v.getYear() - 1900, v.getMonthValue() - 1, v.getDayOfMonth());
		}
		if (java.sql.Date.class == type) {
			return (T) java.sql.Date.valueOf(v);
		}

		if (Timestamp.class == type) {
			LocalDateTime dt = LocalDateTime.of(v, LocalTime.of(0, 0));
			return (T) Timestamp.valueOf(dt);
		}

		if (Time.class == type) {
			throw new SumkException(DATETIME_CONVERT, "LocalDate cannot convert to Time");
		}

		if (LocalDateTime.class == type) {
			return (T) LocalDateTime.of(v, LocalTime.of(0, 0));
		}

		if (LocalTime.class == type) {
			throw new SumkException(DATETIME_CONVERT, "LocalDate cannot convert to LocalTime");
		}

		if (failIfNotSupport || !isGenericDate(type)) {
			throw new SumkException(DATETIME_CONVERT, type.getClass().getName() + "is not a supported Date type");
		}
		return (T) v;
	}

	@SuppressWarnings("unchecked")
	private static <T> T toType(LocalTime v, Class<?> type, boolean failIfNotSupport) {
		if (Date.class == type) {
			return (T) new Date(Time.valueOf(v).getTime());
		}
		if (java.sql.Date.class == type) {
			throw new SumkException(DATETIME_CONVERT, "LocalTime cannot convert to java.sql.Date");
		}

		if (Timestamp.class == type) {
			return (T) new Timestamp(Time.valueOf(v).getTime());
		}

		if (Time.class == type) {
			return (T) Time.valueOf(v);
		}

		if (LocalDateTime.class == type) {
			return (T) LocalDateTime.of(LocalDate.ofEpochDay(0), v);
		}

		if (LocalDate.class == type) {
			throw new SumkException(DATETIME_CONVERT, "LocalTime cannot convert to LocalDate");
		}

		if (failIfNotSupport || !isGenericDate(type)) {
			throw new SumkException(DATETIME_CONVERT, type.getClass().getName() + "is not a supported Date type");
		}
		return (T) v;
	}
}
