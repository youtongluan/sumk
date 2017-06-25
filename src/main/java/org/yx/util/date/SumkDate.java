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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * 本对象没有进行内存压缩，不适合存储于内存中，一般应用于中间计算
 */
public class SumkDate {
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public final static String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 */
	public final static String DATE_TIME_MILS = "yyyy-MM-dd HH:mm:ss.SSS";
	/**
	 * yyyy-MM-dd
	 */
	public final static String DATE = "yyyy-MM-dd";
	/**
	 * HH:mm:ss
	 */
	public final static String TIME = "HH:mm:ss";

	/**
	 * HH:mm:ss.SSS
	 */
	public final static String TIME_MILS = "HH:mm:ss.SSS";

	private final int year;
	private final int month;
	private final int day;
	private final int hour;
	private final int minute;
	private final int second;
	private final int milSecond;
	private final static int MIL_TO_NANO = 1000000;

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
	 * 按公元纪年。公元前的年份可能会有问题，因为该年份不会小于0
	 * 
	 * @return
	 */
	public int getYear() {
		return year;
	}

	/**
	 * 月份，1-12
	 * 
	 * @return
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * 日期中的天，1-31
	 * 
	 * @return
	 */
	public int getDay() {
		return day;
	}

	/**
	 * 24小时制，0-23
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * 分钟，0-59
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * 秒，0-59
	 */
	public int getSecond() {
		return second;
	}

	/**
	 * 毫秒，0-999
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
	 * yyyy-MM-dd格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String toDateString() {
		StringBuilder sb = new StringBuilder();
		final char date_split = '-';
		if (year < 1000 && year > -1) {
			String yearString = String.valueOf(year);
			for (int i = 0; i < 4 - yearString.length(); i++) {
				sb.append('0');
			}
		}
		sb.append(year).append(date_split);
		if (month < 10) {
			sb.append('0');
		}
		sb.append(month).append(date_split);

		if (day < 10) {
			sb.append('0');
		}
		sb.append(day);
		return sb.toString();
	}

	/**
	 * HH:mm:ss 格式
	 */
	public String toTimeString() {
		StringBuilder sb = new StringBuilder();
		final char time_split = ':';
		if (hour < 10) {
			sb.append('0');
		}
		sb.append(hour).append(time_split);

		if (minute < 10) {
			sb.append('0');
		}
		sb.append(minute).append(time_split);

		if (second < 10) {
			sb.append('0');
		}
		sb.append(second);
		return sb.toString();
	}

	/**
	 * yyyy-MM-dd HH:mm:ss 格式<BR>
	 * 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String toDateTimeString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(this.toDateString()).append(' ').append(this.toTimeString()).toString();
	}

	/**
	 * HH:mm:ss.SSS 格式
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
	 * yyyy-MM-dd HH:mm:ss.SSS 格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	@Override
	public String toString() {
		return toFullDateTimeString();
	}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS 格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
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
	 * @param fromat
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

	/**
	 * 将字符串转化为日期
	 * 
	 * @param dateString
	 * @param fromat
	 *            不能为null
	 * @return 如果dateString为空，就返回null
	 * @throws ParseException
	 */
	public static Date parse(String dateString, String format) throws Exception {
		if (dateString == null || dateString.isEmpty()) {
			return null;
		}
		switch (format) {
		case DATE_TIME:
			return new Date(DateTimeFormater.inst.parse(dateString).getTimeInMillis());
		case DATE_TIME_MILS:
			return new Date(FullDateTimeFormater.inst.parse(dateString).getTimeInMillis());
		default:
			return new SimpleDateFormat(format).parse(dateString);
		}
	}

}
