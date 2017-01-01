package org.yx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.yx.conf.AppInfo;

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

	public static String load(InputStream in) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
			char[] buf = new char[1024];
			int len = 0;
			StringBuilder sb = new StringBuilder();
			while ((len = reader.read(buf)) > -1) {
				if (len == 0) {
					continue;
				}
				sb.append(buf, 0, len);
			}
			return sb.toString();
		} finally {
			in.close();
		}

	}

}
