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
package org.yx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public final static String DATETIME = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_TIME_MILS = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static String DATE = "yyyy-MM-dd";
	public final static String TIME = "HH:mm:ss";

	public static String toString(Date d, String fromat) {
		if (d == null) {
			return null;
		}
		return new SimpleDateFormat(fromat).format(d);
	}

	/**
	 * 用yyyy-MM-dd HH:mm:ss进行序列化
	 * 
	 * @param d
	 * @return
	 */
	public static String toDateTimeString(Date d) {
		return toString(d, DATETIME);
	}

	public static Date parse(String d, String fromat) throws ParseException {
		if (d == null) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat(fromat);
		return sf.parse(d);
	}

	/**
	 * 用yyyy-MM-dd HH:mm:ss进行反序列化
	 * 
	 * @param d
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String d) throws ParseException {
		return parse(d, DATETIME);
	}
}
