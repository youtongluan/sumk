package org.yx.util;

public class StringUtils {

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	public static boolean isNotEmpty(CharSequence str) {
		return str != null && str.length() > 0;
	}

}
