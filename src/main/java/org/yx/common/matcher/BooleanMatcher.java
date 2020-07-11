package org.yx.common.matcher;

import java.util.function.Predicate;

public enum BooleanMatcher implements Predicate<String> {

	TRUE(true), FALSE(false);

	private boolean matched;

	private BooleanMatcher(boolean matched) {
		this.matched = matched;
	}

	@Override
	public boolean test(String text) {
		return matched;
	}

}
