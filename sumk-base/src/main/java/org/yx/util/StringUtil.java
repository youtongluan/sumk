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

import java.util.ArrayList;
import java.util.List;

import org.yx.annotation.doc.NotNull;
import org.yx.conf.Const;
import org.yx.exception.SumkException;

public final class StringUtil {

	public static String formatNewLineFlag(@NotNull String text) {
		return text.replace("\r\n", Const.LN).replace("\r", Const.LN);
	}

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	/**
	 * 这个会自动对每个子项做trim()操作，并且过滤掉空值
	 * 
	 * @param source      原始字符串，不能为null
	 * @param splitRegex  分隔符
	 * @param otherSplits 其它的分隔符，这些分隔符不支持正则表达式。结果集会根据所有的分隔符进行分割
	 * @return 结果不包含null和空字符串。返回值不为null
	 */
	public static List<String> splitAndTrim(@NotNull String source, @NotNull String splitRegex, String... otherSplits) {
		if (otherSplits != null && otherSplits.length > 0) {
			for (String r : otherSplits) {
				source = source.replace(r, splitRegex);
			}
		}
		String[] vs = source.split(splitRegex);
		List<String> list = new ArrayList<>(vs.length);
		for (String v : vs) {
			v = v.trim();
			if (v.isEmpty()) {
				continue;
			}
			list.add(v);
		}
		return list;
	}

	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	public static String toLatin(String v) {
		return v.replace('，', ',').replace('；', ';').replace('　', ' ').replace('：', ':').replace('。', '.').replace('？',
				'?');
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	public static boolean isNotEmpty(CharSequence str) {
		return str != null && str.length() > 0;
	}

	public static String camelToUnderline(String param) {
		if (param == null) {
			return param;
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len + 10);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (i == 0) {
				sb.append(Character.toLowerCase(c));
				continue;
			}
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static boolean isNumber(char c) {
		return c >= '0' && c <= '9';
	}

	public static String requireNotEmpty(String text) {
		if (text == null) {
			throw new SumkException(652342134, "字符串是null");
		}
		text = text.trim();
		if (text.isEmpty()) {
			throw new SumkException(652342134, "字符串是空的");
		}
		return text;
	}
}
