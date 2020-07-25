/**
 * Copyright (C) 2016 - 2030 youtongluan.
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

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class UUIDSeed {
	private static final char[] LETTERS = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final int LEN = LETTERS.length;
	private static final int DOUBLE_LEN = LEN * LEN;
	private static AtomicInteger current = new AtomicInteger(6538);

	static void fill(char[] source, final int from, int bytes, long number) {
		if (number < 0) {
			number = Math.max(0 - number, 0);
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
		s = s.toLowerCase();
		String all = new String(LETTERS);
		long ret = 0;
		for (int i = s.length() - 1, k = 0; i >= 0; i--, k++) {
			char c = s.charAt(i);
			int v = all.indexOf(String.valueOf(c));
			ret += (v * Math.pow(LEN, k));
		}
		return ret;
	}

	public static long getSeqTime(String sn) {
		return toLong(sn.substring(0, 8));
	}

	public static long getRandomSNTime(String sn) {
		char[] k = sn.toCharArray();
		String d = new String(new char[] { k[8], k[12], k[16], k[1], k[5], k[9], k[13], k[17] });
		return toLong(d);
	}

	public static String seq() {
		return new String(seqChars());
	}

	public static String seq18() {
		return new String(seqChars(), 1, 18);
	}

	public static String random() {
		return reOrder(seqChars());
	}

	static char[] seqChars() {
		ThreadLocalRandom r = ThreadLocalRandom.current();
		char[] ret = new char[20];
		fill(ret, 0, 8, System.currentTimeMillis());
		fill(ret, 8, 4, System.nanoTime());
		fill(ret, 12, 2, r.nextInt(DOUBLE_LEN));

		int addNum = r.nextInt(LEN) + 1;
		int next = current.addAndGet(addNum);
		if (next > 1000000000) {
			current = new AtomicInteger(next % (DOUBLE_LEN * DOUBLE_LEN) + DOUBLE_LEN);
		}

		fill(ret, 14, 4, next);
		fill(ret, 18, 1, addNum);
		fill(ret, 19, 1, r.nextInt(LEN));
		return ret;
	}

	static String reOrder(char[] cs) {
		char g1 = cs[12];
		char g2 = cs[13];
		System.arraycopy(cs, 0, cs, 2, 8);
		cs[0] = g1;
		cs[1] = g2;
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

	public static String parse(long number, int bytes) {
		char[] cs = new char[bytes];
		fill(cs, 0, bytes, number);
		return new String(cs);
	}

}
