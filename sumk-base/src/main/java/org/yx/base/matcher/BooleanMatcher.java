package org.yx.base.matcher;

import java.util.function.Predicate;

public enum BooleanMatcher implements Predicate<String> {

	FALSE(false), TRUE(true);

	private final boolean matched;

	private BooleanMatcher(boolean matched) {
		this.matched = matched;
	}

	@Override
	public boolean test(String text) {
		return matched;
	}

	@Override
	public Predicate<String> negate() {
		return this.matched ? FALSE : TRUE;
	}

}
