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

class SumkDateStringBuilder {
	private static final char DATE_SPLIT = '-';
	private static final char TIME_SPLIT = ':';

	private final SumkDate sumkDate;

	public SumkDateStringBuilder(SumkDate sumkDate) {
		this.sumkDate = sumkDate;
	}

	/**
	 * 返回的格式如13:12:59
	 * 
	 * @return HH:mm:ss 格式
	 */
	public StringBuilder to_HH_mm_ss() {
		StringBuilder sb = new StringBuilder();
		if (sumkDate.hour < 10) {
			sb.append('0');
		}
		sb.append(sumkDate.hour).append(TIME_SPLIT);

		if (sumkDate.minute < 10) {
			sb.append('0');
		}
		sb.append(sumkDate.minute).append(TIME_SPLIT);

		if (sumkDate.second < 10) {
			sb.append('0');
		}
		return sb.append(sumkDate.second);
	}

	/**
	 * 返回的格式如13:12:59.123
	 * 
	 * @return HH:mm:ss.SSS 格式
	 */
	public StringBuilder to_HH_mm_ss_SSS() {
		StringBuilder sb = this.to_HH_mm_ss().append('.');
		String milStr = String.valueOf(sumkDate.milSecond);
		for (int i = 0; i < 3 - milStr.length(); i++) {
			sb.append('0');
		}
		return sb.append(milStr);
	}

	/**
	 * 返回的格式如2018-10
	 * 
	 * @return yyyy-MM格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public StringBuilder to_yyyy_MM() {
		StringBuilder sb = new StringBuilder();
		if (sumkDate.year == Integer.MIN_VALUE) {
			sb.append(sumkDate.year);
		} else {
			if (sumkDate.year < 0) {
				sb.append('-');
			}
			int y = sumkDate.year >= 0 ? sumkDate.year : -sumkDate.year;
			if (y < 1000) {
				if (y >= 100) {
					sb.append('0');
				} else if (y >= 10) {
					sb.append("00");
				} else {
					sb.append("000");
				}
			}
			sb.append(y);
		}

		sb.append(DATE_SPLIT);
		if (sumkDate.month < 10) {
			sb.append('0');
		}
		return sb.append(sumkDate.month);
	}

	/**
	 * 返回的格式如2018-10-20
	 * 
	 * @return yyyy-MM-dd格式 如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public StringBuilder to_yyyy_MM_dd() {
		StringBuilder sb = this.to_yyyy_MM();
		sb.append(DATE_SPLIT);

		if (sumkDate.day < 10) {
			sb.append('0');
		}
		return sb.append(sumkDate.day);
	}

	/**
	 * 返回的格式如2018-10-20 13:12:59
	 * 
	 * @return yyyy-MM-dd HH:mm:ss 格式<BR>
	 *         如果年份小于1000，会在年份前面补上0。与SimpleDateFormat兼容
	 */
	public StringBuilder to_yyyy_MM_dd_HH_mm_ss() {
		return this.to_yyyy_MM_dd().append(' ').append(this.to_HH_mm_ss());
	}

	public StringBuilder to_yyyy_MM_dd_HH_mm_ss_SSS() {
		return this.to_yyyy_MM_dd().append(' ').append(this.to_HH_mm_ss_SSS());
	}
}
