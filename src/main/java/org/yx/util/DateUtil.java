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

import java.util.Date;

import org.yx.util.date.SumkDate;

public class DateUtil {

	public static String toString(Date d, String fromat) {
		return SumkDate.toString(d, fromat);
	}

	/**
	 * 用yyyy-MM-dd HH:mm:ss进行序列化<BR>
	 * 
	 * @param d
	 *            日期对象
	 * @return 如果date为null，就返回null
	 */
	public static String toDateTimeString(Date d) {
		return toString(d, SumkDate.DATE_TIME);
	}

	public static Date parse(String d, String format) throws Exception {
		return SumkDate.parse(d, format);
	}

	/**
	 * 用yyyy-MM-dd HH:mm:ss进行反序列化<br>
	 * 如果分钟、小时等属性等参数溢出，会自动进行调整，不会出错<BR>
	 * 
	 * @param d
	 *            日期对象
	 * @return 如果date为null，就返回null
	 * @throws java.lang.Exception
	 *             如果有异常发生
	 */
	public static Date parseDateTime(String d) throws Exception {
		return SumkDate.parse(d, SumkDate.DATE_TIME);
	}

	public static SumkDate toSumkDate(Date d) {
		return new SumkDate(d);
	}

}
