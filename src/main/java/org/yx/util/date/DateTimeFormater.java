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
package org.yx.util.date;

import java.util.Calendar;

/**
 * yyyy-MM-dd HH:mm:ss
 */
public class DateTimeFormater implements SumkDateFormater {

	public static SumkDateFormater inst = new DateTimeFormater();

	@Override
	public Calendar parse(String text) {
		int year = Integer.parseInt(text.substring(0, 4));
		int month = Integer.parseInt(text.substring(5, 7)) - 1;
		int day = Integer.parseInt(text.substring(8, 10));
		int hour = Integer.parseInt(text.substring(11, 13));
		int minute = Integer.parseInt(text.substring(14, 16));
		int second = Integer.parseInt(text.substring(17, 19));
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

}
