package org.yx.util;

import org.yx.exception.SumkException;

public class BitUtil {

	public static int setBit(int flag, int rightCount, boolean value) {
		if (rightCount < 1 || rightCount > 32) {
			throw new SumkException(23436546, "bit要在1-32之间，实际却是" + rightCount);
		}
		int v = 1 << (rightCount - 1);
		if (value) {
			return flag | v;
		}
		return flag & (~v);
	}

	public static boolean getBit(int flag, int rightCount) {
		if (rightCount < 1 || rightCount > 32) {
			throw new SumkException(23436546, "bit要在1-32之间，实际却是" + rightCount);
		}
		int v = 1 << (rightCount - 1);
		return (flag & v) != 0;
	}

	public static int setBitsToTrue(int flag, int... rightCounts) {
		for (int bit : rightCounts) {
			flag = setBit(flag, bit, true);
		}
		return flag;
	}
}
