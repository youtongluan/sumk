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

import org.yx.util.StringUtil;
import org.yx.util.SumkDate;

public final class FullDateTimeFormater implements SumkDateFormater {
	public static final FullDateTimeFormater inst = new FullDateTimeFormater();

	private FullDateTimeFormater() {

	}

	@Override
	public int order() {
		return 0;
	}

	@Override
	public SumkDate parse(String text) {
		int dotIndex = text.length() - 1;
		for (; dotIndex > 15; dotIndex--) {
			char c = text.charAt(dotIndex);
			if (!StringUtil.isNumber(c)) {
				break;
			}
		}
		String textMil = text.substring(dotIndex + 1);
		int mils = Integer.parseInt(textMil);
		if (textMil.length() == 1) {
			mils *= 100;
		} else if (textMil.length() == 2) {
			mils *= 10;
		}
		int second = Integer.parseInt(text.substring(dotIndex - 2, dotIndex));
		int minute = Integer.parseInt(text.substring(dotIndex - 5, dotIndex - 3));
		int hour = Integer.parseInt(text.substring(dotIndex - 8, dotIndex - 6));
		int day = Integer.parseInt(text.substring(dotIndex - 11, dotIndex - 9));
		int month = Integer.parseInt(text.substring(dotIndex - 14, dotIndex - 12));
		int year = Integer.parseInt(text.substring(0, dotIndex - 15));
		return SumkDate.of(year, month, day, hour, minute, second, mils);
	}

	@Override
	public boolean accept(String format) {
		return format.length() == 23 && format.regionMatches(0, "yyyy", 0, 4) && format.regionMatches(5, "MM", 0, 2)
				&& format.regionMatches(8, "dd", 0, 2) && format.regionMatches(11, "HH", 0, 2)
				&& format.regionMatches(14, "mm", 0, 2) && format.regionMatches(17, "ss", 0, 2)
				&& format.endsWith("SSS");
	}

}
