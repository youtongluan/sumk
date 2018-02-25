/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.util.date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;

public class SumkDate {

	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_TIME_MILS = "yyyy-MM-dd HH:mm:ss.SSS";

	public static final String DATE = "yyyy-MM-dd";

	public static final String TIME = "HH:mm:ss";

	public static final String TIME_MILS = "HH:mm:ss.SSS";

	public static final DateTimeFormatter CHN_LOCAL_DATE_TIME = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral(' ').append(DateTimeFormatter.ISO_LOCAL_TIME)
			.toFormatter();

	private final int year;
	private final int month;
	private final int day;
	private final int hour;
	private final int minute;
	private final int second;
	private final int milSecond;
	private static final int MIL_TO_NANO = 1000_000;
	private static final char DATE_SPLIT = '-';
	private static final char TIME_SPLIT = ':';

	public SumkDate(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH) + 1;
		this.day = cal.get(Calendar.DATE);

		this.hour = cal.get(Calendar.HOUR_OF_DAY);
		this.minute = cal.get(Calendar.MINUTE);
		this.second = cal.get(Calendar.SECOND);
		this.milSecond = cal.get(Calendar.MILLISECOND);
	}

	/**
	 * @return 按公元纪年。公元前的年份可能会有问题，因为该年份不会小于0
	 * 
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return 月份，1-12
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return 日期中的天，1-31
	 * 
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return 24小时制，0-23
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @return 分钟，0-59
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @return 秒，0-59
	 */
	public int getSecond() {
		return second;
	}

	/**
	 * @return 毫秒，0-999
	 */
	public int getMilSecond() {
		return milSecond;
	}

	public LocalDateTime toLocalDateTime() {
		return LocalDateTime.of(toLocalDate(), toLocalTime());
	}

	public LocalDate toLocalDate() {
		return LocalDate.of(year, month, day);
	}

	public LocalTime toLocalTime() {
		return LocalTime.of(hour, minute, second, milSecond * MIL_TO_NANO);
	}

	/**
	 * @return yyyy-MM-dd格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String toDateString() {
		StringBuilder sb = new StringBuilder(this.toMonthString());
		sb.append(DATE_SPLIT);

		if (day < 10) {
			sb.append('0');
		}
		sb.append(day);
		return sb.toString();
	}

	/**
	 * @return yyyy-MM格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String toMonthString() {
		StringBuilder sb = new StringBuilder();
		if (year < 1000 && year > -1) {
			String yearString = String.valueOf(year);
			for (int i = 0; i < 4 - yearString.length(); i++) {
				sb.append('0');
			}
		}
		sb.append(year).append(DATE_SPLIT);
		if (month < 10) {
			sb.append('0');
		}
		sb.append(month);
		return sb.toString();
	}

	/**
	 * @return HH:mm:ss 格式
	 */
	public String toTimeString() {
		StringBuilder sb = new StringBuilder();
		if (hour < 10) {
			sb.append('0');
		}
		sb.append(hour).append(TIME_SPLIT);

		if (minute < 10) {
			sb.append('0');
		}
		sb.append(minute).append(TIME_SPLIT);

		if (second < 10) {
			sb.append('0');
		}
		sb.append(second);
		return sb.toString();
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss 格式<BR>
	 *         如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String toDateTimeString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(this.toDateString()).append(' ').append(this.toTimeString()).toString();
	}

	/**
	 * @return HH:mm:ss.SSS 格式
	 */
	public String toFullTimeString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.toTimeString()).append('.');
		String milStr = String.valueOf(this.milSecond);
		for (int i = 0; i < 3 - milStr.length(); i++) {
			sb.append('0');
		}
		sb.append(milStr);
		return sb.toString();
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss.SSS 格式
	 *         如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	@Override
	public String toString() {
		return toFullDateTimeString();
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss.SSS 格式
	 *         如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String toFullDateTimeString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(this.toDateString()).append(' ').append(this.toFullTimeString()).toString();
	}

	/**
	 * 将日期转化为字符串
	 * 
	 * @param d
	 *            不能为null
	 * @param format
	 *            格式
	 * @return 如果日期为null，就返回null
	 */
	public static String toString(Date d, String format) {
		if (d == null) {
			return null;
		}
		if (DATE_TIME.equals(format)) {
			return new SumkDate(d).toDateTimeString();
		}
		if (DATE_TIME_MILS.equals(format)) {
			return new SumkDate(d).toFullDateTimeString();
		}
		if (DATE.equals(format)) {
			return new SumkDate(d).toDateString();
		}
		if (TIME.equals(format)) {
			return new SumkDate(d).toTimeString();
		}
		if (TIME_MILS.equals(format)) {
			return new SumkDate(d).toFullTimeString();
		}
		return new SimpleDateFormat(format).format(d);
	}

	public static Date parse(String dateString, String format) throws Exception {
		if (dateString == null || dateString.isEmpty()) {
			return null;
		}
		switch (format) {
		case DATE_TIME:
			return new Date(DateTimeFormater.inst.parse(dateString).getTimeInMillis());
		case DATE_TIME_MILS:
			return new Date(FullDateTimeFormater.inst.parse(dateString).getTimeInMillis());
		case DATE:
			return new Date(DateFormater.inst.parse(dateString).getTimeInMillis());
		default:
			return new SimpleDateFormat(format).parse(dateString);
		}
	}

}
