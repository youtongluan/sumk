package org.yx.common.matcher;

import java.util.function.Predicate;

public class BooleanMatcher implements Predicate<String> {

	public static final BooleanMatcher TRUE = new BooleanMatcher(true);
	public static final BooleanMatcher FALSE = new BooleanMatcher(false);

	private boolean matched;

	BooleanMatcher(boolean matched) {
		this.matched = matched;
	}

	@Override
	public boolean test(String text) {
		return matched;
	}

	@Override
	public String toString() {
		return "BooleanMatcher [" + matched + "]";
	}
}
