/**
 * Copyright (C) 2016 - 2017 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class UUIDSeed {
	private final static char[] LETTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private final static int LEN = LETTERS.length;
	private static Random[] RANDOMS;
	private static final int RANDOM_LENGTH = 5;
	private static int R_INDEX = 0;
	static {
		RANDOMS = new Random[RANDOM_LENGTH];
		for (int i = 0; i < RANDOM_LENGTH; i++) {
			RANDOMS[i] = new Random();
		}
	}

	private static Random getRandom() {
		int k = R_INDEX++;
		int index = k % RANDOM_LENGTH;
		if (k < 10000) {
			return RANDOMS[index];
		}
		R_INDEX = ThreadLocalRandom.current().nextInt(RANDOM_LENGTH);
		Random r = new Random();
		RANDOMS[index] = r;
		return r;
	}

	static void fill(char[] source, final int from, long number, int bytes) {
		if (number == Long.MIN_VALUE) {
			number = Long.MAX_VALUE;
		}
		if (number < 0) {
			number = 0 - number;
		}
		int index = from + bytes - 1;
		while (number > 0) {
			int k = (int) (number % LEN);
			source[index] = LETTERS[k];
			if (index == from) {
				return;
			}
			number = number / LEN;
			index--;
		}
		while (index >= from) {
			source[index--] = '0';
		}
	}

	public static long toLong(String s) {
		String all = new String(LETTERS);
		long ret = 0;
		for (int i = s.length() - 1, k = 0; i >= 0; i--, k++) {
			char c = s.charAt(i);
			int v = all.indexOf(c + "");
			ret += (v * Math.pow(LEN, k));
		}
		return ret;
	}

	private final static int DOUBLE_LEN = LEN * LEN;
	private static AtomicInteger current = new AtomicInteger(6538);

	/**
	 * 获取seq类型sn的时间
	 * 
	 * @param sn
	 * @return
	 */
	public static long getSeqTime(String sn) {
		return toLong(sn.substring(0, 8));
	}

	public static long getRandomSNTime(String sn) {
		char[] k = sn.toCharArray();
		String d = new String(new char[] { k[8], k[12], k[16], k[1], k[5], k[9], k[13], k[17] });
		return toLong(d);
	}

	/**
	 * 获取按时间排序的UUID
	 * 
	 * @return
	 */
	public static String seq() {
		char[] ret = new char[20];
		fill(ret, 0, System.currentTimeMillis(), 8);
		fill(ret, 8, System.nanoTime(), 4);
		fill(ret, 12, ThreadLocalRandom.current().nextInt(DOUBLE_LEN), 2);

		int addNum = getRandom().nextInt(LEN) + 1;
		int next = current.addAndGet(addNum);
		if (next > 1000000000) {
			current = new AtomicInteger(next % (DOUBLE_LEN * DOUBLE_LEN) + DOUBLE_LEN);
		}

		fill(ret, 14, next, 4);
		fill(ret, 18, addNum, 1);
		fill(ret, 19, getRandom().nextInt(LEN), 1);
		return new String(ret);
	}

	/**
	 * 返回20位的字符UUID。 类似于UUID，无法做到绝对的不重复，但是重复的概率非常低
	 * 
	 * @return
	 */
	public static String random() {
		char[] ret = new char[20];
		fill(ret, 0, ThreadLocalRandom.current().nextInt(DOUBLE_LEN), 2);
		fill(ret, 2, System.currentTimeMillis(), 8);
		fill(ret, 10, System.nanoTime(), 4);

		int addNum = getRandom().nextInt(LEN) + 1;
		int next = current.addAndGet(addNum);
		if (next > 1000000000) {
			current = new AtomicInteger(next % (DOUBLE_LEN * DOUBLE_LEN) + DOUBLE_LEN);
		}

		fill(ret, 14, next, 4);
		fill(ret, 18, addNum, 1);
		fill(ret, 19, getRandom().nextInt(LEN), 1);
		return reOrder(ret);
	}

	private static String reOrder(char[] cs) {
		char[] temp = new char[cs.length];
		int len = cs.length / 2;
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < len; i++) {
				temp[i * 2] = cs[i];
				temp[2 * i + 1] = cs[i + len];
			}
			cs = temp;
			temp = new char[cs.length];
		}
		return new String(cs);
	}

	/**
	 * 
	 * @param number
	 *            要转化的数字
	 * @param bytes
	 *            要转化的位数，不足的话，在前面补0
	 * @return
	 */
	public static String parse(long number, int bytes) {
		if (number == Long.MIN_VALUE) {
			number = Long.MAX_VALUE;
		}
		if (number < 0) {
			number = 0 - number;
		}
		String ret = "";
		while (number > 0) {
			int k = (int) (number % LEN);
			ret = LETTERS[k] + ret;
			number = number / LEN;
		}
		while (ret.length() < bytes) {
			ret = "0" + ret;
		}
		if (ret.length() > bytes) {
			return ret.substring(ret.length() - bytes, ret.length());
		}
		return ret;
	}

}
