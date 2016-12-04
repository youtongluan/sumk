package org.yx.util;

import org.yx.exception.SumkException;

public final class Assert {

	public static void notNull(Object obj) {
		if (obj == null) {
			throw new SumkException(567567, "param must not be null");
		}

	}

	public static void notNull(Object obj, String msg) {
		if (obj == null) {
			throw new SumkException(652345436, msg);
		}

	}

	public static void notEmpty(String obj, String msg) {
		if (obj == null || obj.isEmpty()) {
			throw new SumkException(657645465, msg);
		}

	}

	public static void isTrue(boolean b, String msg) {
		if (b) {
			return;
		}
		throw new SumkException(5674354, msg);
	}

	public static void hasText(String text, String msg) {
		if (text == null || text.trim().isEmpty()) {
			throw new SumkException(652342134, msg);
		}

	}

}
