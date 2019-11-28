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
package org.yx.util;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.yx.common.date.DateFormater;
import org.yx.common.date.DateTimeFormater;
import org.yx.common.date.FullDateTimeFormater;
import org.yx.common.date.SumkDateFormater;
import org.yx.common.date.SumkDateQuery;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public final class SumkDate implements Comparable<SumkDate> {
	private static final String LOG_NAME = "sumk.date";

	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

	public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

	public static final String yyyy_MM_dd = "yyyy-MM-dd";

	public static final String HH_mm_ss = "HH:mm:ss";

	public static final String HH_mm_ss_SSS = "HH:mm:ss.SSS";

	public static final int ERROR_CODE = 912753954;

	private static final int MIL_TO_NANO = 1000_000;
	private static final SumkDateFormater[] formaters = { FullDateTimeFormater.inst, DateTimeFormater.inst,
			DateFormater.inst };

	/**
	 * @return 当前时间
	 */
	public static SumkDate now() {
		return of(LocalDateTime.now());
	}

	public static SumkDate of(Calendar cal) {

		int y = cal.get(Calendar.ERA) == GregorianCalendar.AD ? cal.get(Calendar.YEAR) : 1 - cal.get(Calendar.YEAR);
		return of(y, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
	}

	public static SumkDate of(Date d) {
		return of(d.getTime());
	}

	public static SumkDate of(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		return of(cal);
	}

	/**
	 * 
	 * @param year
	 *            年份，从公元元年开始计算
	 * @param month
	 *            1-12
	 * @param day
	 *            1-31
	 * @param hour
	 *            0-23
	 * @param minute
	 *            0-59
	 * @param second
	 *            0-59
	 * @param milSecond
	 *            0-999
	 * @return SumkDate对象
	 */
	public static SumkDate of(int year, int month, int day, int hour, int minute, int second, int milSecond) {
		return new SumkDate(year, (byte) month, (byte) day, (byte) hour, (byte) minute, (byte) second,
				(short) milSecond);
	}

	SumkDate(int year, byte month, byte day, byte hour, byte minute, byte second, short milSecond) {
		if (month < 1 || month > 12) {
			SumkException.throwException(ERROR_CODE, month + " is not valid month");
		}
		if (day < 1 || day > 31) {
			SumkException.throwException(ERROR_CODE, day + " is not valid day");
		}
		if (hour < 0 || hour > 23) {
			SumkException.throwException(ERROR_CODE, hour + " is not valid hour");
		}
		if (minute < 0 || minute > 59) {
			SumkException.throwException(ERROR_CODE, minute + " is not valid minute");
		}
		if (second < 0 || second > 59) {
			SumkException.throwException(ERROR_CODE, second + " is not valid second");
		}
		if (milSecond < 0 || milSecond > 999) {
			SumkException.throwException(ERROR_CODE, milSecond + " is not valid milSecond");
		}
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.milSecond = milSecond;
	}

	public static SumkDate of(LocalDateTime d) {
		return of(d.toLocalDate(), d.toLocalTime());
	}

	/**
	 * @param date
	 *            可以为null，如果为null，年份就是1970-01-01
	 * @param time
	 *            可以为null
	 * @return SumkDate对象
	 */
	public static SumkDate of(LocalDate date, LocalTime time) {
		int year, month, day;
		if (date != null) {
			year = date.getYear();
			month = date.getMonthValue();
			day = date.getDayOfMonth();
		} else {
			year = 1970;
			month = 1;
			day = 1;
		}
		if (time != null) {
			return of(year, month, day, time.getHour(), time.getMinute(), time.getSecond(),
					time.get(ChronoField.MILLI_OF_SECOND));
		}
		return new SumkDate(year, (byte) month, (byte) day, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
	}

	public static SumkDate of(final String dateString) {
		int lastDot = dateString.lastIndexOf(".");

		if (lastDot > 15 && dateString.length() - lastDot <= 4) {
			return FullDateTimeFormater.inst.parse(dateString);
		}
		return DateTimeFormater.inst.parse(dateString);
	}

	public static SumkDate of(final String dateString, final String format) {
		for (SumkDateFormater f : formaters) {
			if (f.accept(format)) {
				return f.parse(dateString);
			}
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			SumkDate sd = formatter.parse(dateString, SumkDateQuery.inst);
			if (sd != null) {
				return sd;
			}
		} catch (Exception e) {
			Log.get(LOG_NAME).warn(e.getMessage(), e);
			if (!AppInfo.getBoolean("sumk.date.retry_simple", true)) {
				throw new SumkException(12345435, dateString + "使用java time方式解析失败", e);
			}
		}
		try {
			return of(new SimpleDateFormat(format).parse(dateString));
		} catch (ParseException e1) {
			Log.get(LOG_NAME).error(e1.getMessage(), e1);
			throw new SumkException(12345435, dateString + "使用SimpleDateFormat方式解析失败", e1);
		}

	}

	/**
	 * 将日期转化为字符串
	 * 
	 * @param d
	 *            不能为null
	 * @param format
	 *            格式
	 * @return SumkDate对象，如果日期为null，就返回null
	 */
	public static String format(Date d, String format) {
		if (d == null) {
			return null;
		}
		if (yyyy_MM_dd_HH_mm_ss_SSS.equals(format)) {
			return of(d).to_yyyy_MM_dd_HH_mm_ss_SSS();
		}
		if (yyyy_MM_dd_HH_mm_ss.equals(format)) {
			return of(d).to_yyyy_MM_dd_HH_mm_ss();
		}
		if (yyyy_MM_dd.equals(format)) {
			return of(d).to_yyyy_MM_dd();
		}
		if (HH_mm_ss.equals(format)) {
			return of(d).to_HH_mm_ss();
		}
		if (HH_mm_ss_SSS.equals(format)) {
			return of(d).to_HH_mm_ss_SSS();
		}
		return new SimpleDateFormat(format).format(d);
	}

	public static String format(LocalDateTime d, String format) {
		if (d == null) {
			return null;
		}
		if (yyyy_MM_dd_HH_mm_ss_SSS.equals(format)) {
			return of(d).to_yyyy_MM_dd_HH_mm_ss_SSS();
		}
		if (yyyy_MM_dd_HH_mm_ss.equals(format)) {
			return of(d).to_yyyy_MM_dd_HH_mm_ss();
		}
		if (yyyy_MM_dd.equals(format)) {
			return of(d).to_yyyy_MM_dd();
		}
		if (HH_mm_ss.equals(format)) {
			return of(d).to_HH_mm_ss();
		}
		if (HH_mm_ss_SSS.equals(format)) {
			return of(d).to_HH_mm_ss_SSS();
		}
		return d.format(DateTimeFormatter.ofPattern(format));
	}

	public static Date toDate(String dateString, String format) {
		return of(dateString, format).toDate();
	}

	public static LocalDateTime toLocalDateTime(String dateString, String format) {
		return of(dateString, format).toLocalDateTime();
	}

	public static Date toDate(LocalDateTime localDateTime) {
		return of(localDateTime).toDate();
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return of(date).toLocalDateTime();
	}

	public static long takeUpTimeInMils(SumkDate from, SumkDate to) {
		return to.getTimeInMils() - from.getTimeInMils();
	}

	final int year;

	final byte month;

	final byte day;

	final byte hour;

	final byte minute;

	final byte second;

	final short milSecond;

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
	 * @return 毫秒，0-999
	 */
	public int getMilSecond() {
		return milSecond;
	}

	/**
	 * @return 分钟，0-59
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @return 月份，1-12
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return 秒，0-59
	 */
	public int getSecond() {
		return second;
	}

	/**
	 * @return 按公元纪年。公元前的年份可能会有问题，因为该年份不会小于0
	 * 
	 */
	public int getYear() {
		return year;
	}

	/**
	 * 返回的格式如13:12:59
	 * 
	 * @return HH:mm:ss 格式
	 */
	public String to_HH_mm_ss() {
		return new SumkDateStringBuilder(this).to_HH_mm_ss().toString();
	}

	/**
	 * 返回的格式如13:12:59.123
	 * 
	 * @return HH:mm:ss.SSS 格式
	 */
	public String to_HH_mm_ss_SSS() {
		return new SumkDateStringBuilder(this).to_HH_mm_ss_SSS().toString();
	}

	/**
	 * 返回的格式如2018-10
	 * 
	 * @return yyyy-MM格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String to_yyyy_MM() {
		return new SumkDateStringBuilder(this).to_yyyy_MM().toString();
	}

	/**
	 * 返回的格式如2018-10-20
	 * 
	 * @return yyyy-MM-dd格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String to_yyyy_MM_dd() {
		return new SumkDateStringBuilder(this).to_yyyy_MM_dd().toString();
	}

	/**
	 * 返回的格式如2018-10-20 13:12:59
	 * 
	 * @return yyyy-MM-dd HH:mm:ss 格式<BR>
	 *         如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String to_yyyy_MM_dd_HH_mm_ss() {
		return new SumkDateStringBuilder(this).to_yyyy_MM_dd_HH_mm_ss().toString();
	}

	/**
	 * 返回的格式如2018-10-20 13:12:59.123
	 * 
	 * @return yyyy-MM-dd HH:mm:ss.SSS 格式
	 *         如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public String to_yyyy_MM_dd_HH_mm_ss_SSS() {
		return new SumkDateStringBuilder(this).to_yyyy_MM_dd_HH_mm_ss_SSS().toString();
	}

	private Calendar toCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hour, minute, second);
		cal.set(Calendar.MILLISECOND, milSecond);
		return cal;
	}

	public Date toDate() {
		return toCalendar().getTime();
	}

	public long getTimeInMils() {
		return toCalendar().getTimeInMillis();
	}

	public LocalDate toLocalDate() {
		return LocalDate.of(year, month, day);
	}

	public LocalDateTime toLocalDateTime() {
		return LocalDateTime.of(toLocalDate(), toLocalTime());
	}

	public LocalTime toLocalTime() {
		return LocalTime.of(hour, minute, second, milSecond * MIL_TO_NANO);
	}

	@SuppressWarnings("deprecation")
	public Timestamp toTimestamp() {
		return new Timestamp(getYear() - 1900, getMonth() - 1, getDay(), getHour(), getMinute(), getSecond(),
				this.milSecond * 1_000_000);
	}

	public boolean isAfter(SumkDate d) {
		return this.compareTo(d) > 0;
	}

	public boolean isBefore(SumkDate d) {
		return this.compareTo(d) < 0;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss.SSS 格式
	 *         如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	@Override
	public String toString() {
		return to_yyyy_MM_dd_HH_mm_ss_SSS();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + hour;
		result = prime * result + milSecond;
		result = prime * result + minute;
		result = prime * result + month;
		result = prime * result + second;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SumkDate other = (SumkDate) obj;
		if (day != other.day)
			return false;
		if (hour != other.hour)
			return false;
		if (milSecond != other.milSecond)
			return false;
		if (minute != other.minute)
			return false;
		if (month != other.month)
			return false;
		if (second != other.second)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public int compareTo(SumkDate d) {
		if (d == null) {
			return 1;
		}

		if (this.year != d.year) {
			return year > d.year ? 1 : -1;
		}

		int v = this.month - d.month;
		if (v != 0) {
			return v;
		}

		v = this.day - d.day;
		if (v != 0) {
			return v;
		}

		v = this.hour - d.hour;
		if (v != 0) {
			return v;
		}

		v = this.minute - d.minute;
		if (v != 0) {
			return v;
		}

		v = this.second - d.second;
		if (v != 0) {
			return v;
		}

		return this.milSecond - d.milSecond;
	}

	public SumkDate withYear(int year) {
		if (year == this.year) {
			return this;
		}
		return new SumkDate(year, month, day, hour, minute, second, milSecond);
	}

	/**
	 * 设置月份（不修改原来的对象）
	 * 
	 * @param month
	 *            1-12
	 * @return 新的SumkDate对象
	 */
	public SumkDate withMonth(int month) {
		if (month == this.month) {
			return this;
		}
		if (month < 1 || month > 12) {
			throw new DateTimeException(
					new StringBuilder().append("月份参数 ").append(month).append(" 不在有效值范围内").toString());
		}
		return new SumkDate(year, (byte) month, day, hour, minute, second, milSecond);
	}

	public SumkDate withDay(int day) {
		if (day == this.day) {
			return this;
		}
		DAY_OF_MONTH.checkValidValue(day);
		return new SumkDate(year, month, (byte) day, hour, minute, second, milSecond);
	}

	public SumkDate withHour(int h) {
		if (h == this.hour) {
			return this;
		}
		if (h < 0 || h > 23) {
			throw new DateTimeException(new StringBuilder().append("小时参数 ").append(h).append(" 不在有效值范围内").toString());
		}
		return new SumkDate(year, month, day, (byte) h, minute, second, milSecond);
	}

	public SumkDate withMinute(int m) {
		if (m == this.minute) {
			return this;
		}
		if (m < 0 || m > 59) {
			throw new DateTimeException(new StringBuilder().append("分钟参数 ").append(m).append(" 不在有效值范围内").toString());
		}
		return new SumkDate(year, month, day, hour, (byte) m, second, milSecond);
	}

	public SumkDate withSecond(int sec) {
		if (sec == this.second) {
			return this;
		}
		if (sec < 0 || sec > 59) {
			throw new DateTimeException(new StringBuilder().append("秒参数 ").append(sec).append(" 不在有效值范围内").toString());
		}
		return new SumkDate(year, month, day, hour, minute, (byte) sec, milSecond);
	}

	public SumkDate withMilSecond(int milSecond) {
		if (milSecond == this.milSecond) {
			return this;
		}
		if (milSecond < 0 || milSecond > 999) {
			throw new DateTimeException(
					new StringBuilder().append("毫秒参数 ").append(milSecond).append(" 不在有效值范围内").toString());
		}
		return new SumkDate(year, month, day, hour, minute, second, (short) milSecond);
	}

	public SumkDate plusYears(int years) {
		if (years == 0) {
			return this;
		}
		return new SumkDate(year + years, month, day, hour, minute, second, milSecond);
	}

	/**
	 * months个月以后的日期
	 * 
	 * @param months
	 *            任意数字，正数、负数都可以
	 * @return months个月以后的日期
	 */
	public SumkDate plusMonths(int months) {
		if (months == 0) {
			return this;
		}
		return of(this.toLocalDateTime().plusMonths(months));
	}

	public SumkDate plusDays(int days) {
		if (days == 0) {
			return this;
		}
		return of(this.toLocalDateTime().plusDays(days));
	}

	public SumkDate plusHours(int hours) {
		if (hours == 0) {
			return this;
		}
		return of(this.toLocalDateTime().plusHours(hours));
	}

	public SumkDate plusMinutes(int minutes) {
		if (minutes == 0) {
			return this;
		}
		return of(this.toLocalDateTime().plusMinutes(minutes));
	}

	public SumkDate plusSeconds(int seconds) {
		if (seconds == 0) {
			return this;
		}
		return of(this.toLocalDateTime().plusSeconds(seconds));
	}

	public SumkDate plusMilSeconds(int mils) {
		if (mils == 0) {
			return this;
		}
		return of(this.toLocalDateTime().plus(mils, ChronoUnit.MILLIS));
	}

	public static String stringOf(Date d) {
		return d == null ? "null" : of(d).toString();
	}

	public static String stringOf(LocalDateTime d) {
		return d == null ? "null" : of(d).toString();
	}
}