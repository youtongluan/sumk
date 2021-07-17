package org.yx.common;

import java.util.List;
import java.util.function.Predicate;

import org.yx.util.StringUtil;

public class Predicator {

	public static boolean test(String vs, Predicate<String> p) {
		if (vs.contains("||")) {
			return orTest(vs, p);
		}
		return andTest(vs, p);
	}

	public static boolean andTest(String vs, Predicate<String> p) {
		List<String> list = StringUtil.splitAndTrim(vs, ",", "，", "&&");
		for (String s : list) {
			if (!p.test(s)) {
				return false;
			}
		}
		return true;
	}

	public static boolean orTest(String vs, Predicate<String> p) {
		List<String> list = StringUtil.splitAndTrim(vs, "\\|\\|");
		for (String s : list) {
			if (p.test(s)) {
				return true;
			}
		}
		return false;
	}
}
