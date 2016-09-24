package org.yx.rpc;

public class SourceSn {
	private static ThreadLocal<String> tl = new ThreadLocal<String>();

	public static void register(String sn) {
		if (tl.get() == null) {
			tl.set(sn);
		}
	}

	public static String getSn0() {
		return tl.get();
	}

	public static void removeSn0() {
		tl.remove();
	}
}
