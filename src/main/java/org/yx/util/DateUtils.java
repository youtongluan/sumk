package org.yx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public final static String DATETIME = "yyyy-MM-dd HH:mm:ss";

	public static String toString(Date d, String fromat) {
		if (d == null) {
			return null;
		}
		return new SimpleDateFormat(fromat).format(d);
	}

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

	public static Date parse(String d) throws ParseException {
		return parse(d, DATETIME);
	}
}
