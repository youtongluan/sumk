package org.yx.util.lock;

public class LockedKey implements Key {
	static LockedKey key = new LockedKey();

	private LockedKey() {

	}

	@Override
	public String getId() {
		return "LockedKey";
	}

}
