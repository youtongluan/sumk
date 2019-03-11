package org.yx.common.matcher;

public class BooleanMatcher implements TextMatcher {

	public static final BooleanMatcher TRUE = new BooleanMatcher(true);
	public static final BooleanMatcher FALSE = new BooleanMatcher(false);

	private boolean matched;

	BooleanMatcher(boolean matched) {
		this.matched = matched;
	}

	@Override
	public boolean match(String text) {
		return matched;
	}

	@Override
	public String toString() {
		return "BooleanMatcher [" + matched + "]";
	}
}
