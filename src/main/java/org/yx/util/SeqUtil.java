package org.yx.util;

public class SeqUtil {
	static Seq inst = new Seq();

	public static long next() {
		return inst.next();
	}

	public static long next(String name) {
		return inst.next(name);
	}

	public static long getDate(long seq) {
		return Seq.getDate(seq);
	}

	public static void setCounter(SeqCounter counter) {
		inst.setCounter(counter);
	}
}
