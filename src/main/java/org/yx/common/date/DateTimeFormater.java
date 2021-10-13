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

import org.yx.util.SumkDate;

/**
 * yyyy-MM-dd HH:mm:ss
 */
public final class DateTimeFormater implements SumkDateFormater {

	public static final DateTimeFormater inst = new DateTimeFormater();

	private DateTimeFormater() {

	}

	@Override
	public int order() {
		return 20;
	}

	@Override
	public SumkDate parse(String text) {
		int firstIndex = text.length() - 15;
		int year = Integer.parseInt(text.substring(0, firstIndex));
		int month = Integer.parseInt(text.substring(firstIndex + 1, firstIndex + 3));
		int day = Integer.parseInt(text.substring(firstIndex + 4, firstIndex + 6));
		int hour = Integer.parseInt(text.substring(firstIndex + 7, firstIndex + 9));
		int minute = Integer.parseInt(text.substring(firstIndex + 10, firstIndex + 12));
		int second = Integer.parseInt(text.substring(firstIndex + 13));
		return SumkDate.of(year, month, day, hour, minute, second, 0);
	}

	@Override
	public boolean accept(String format) {
		return format.length() == 19 && format.regionMatches(0, "yyyy", 0, 4) && format.regionMatches(5, "MM", 0, 2)
				&& format.regionMatches(8, "dd", 0, 2) && format.regionMatches(11, "HH", 0, 2)
				&& format.regionMatches(14, "mm", 0, 2) && format.endsWith("ss");
	}

}
