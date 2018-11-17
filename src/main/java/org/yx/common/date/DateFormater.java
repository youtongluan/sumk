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
 * yyyy-MM-dd
 */
public final class DateFormater implements SumkDateFormater {

	public static final DateFormater inst = new DateFormater();

	@Override
	public int order() {
		return 50;
	}

	@Override
	public SumkDate parse(String text) {
		int firstIndex = text.length() - 6;
		int year = Integer.parseInt(text.substring(0, firstIndex));
		int month = Integer.parseInt(text.substring(firstIndex + 1, firstIndex + 3));
		int day = Integer.parseInt(text.substring(firstIndex + 4));
		return SumkDate.of(year, month, day, 0, 0, 0, 0);
	}

	@Override
	public boolean accept(String format) {
		return format.length() == 10 && format.regionMatches(0, "yyyy", 0, 4) && format.regionMatches(5, "MM", 0, 2)
				&& format.endsWith("dd");
	}

}
