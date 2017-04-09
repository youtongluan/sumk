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
package org.yx.common;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.yx.log.Log;

public final class Seq {

	private static final long FROMMILS = 1420041600000L;
	private static final int LOCAL_SEQ_INDEX = 64;

	private AtomicIntegerArray localSeqs = new AtomicIntegerArray(LOCAL_SEQ_INDEX + 1);
	private SeqCounter counter;

	public Seq() {
		try {
			for (int i = 0; i < localSeqs.length(); i++) {
				localSeqs.set(i, ThreadLocalRandom.current().nextInt(256));
			}
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

	public void setCounter(SeqCounter counter) {
		this.counter = counter;
	}

	private int localHashIndex(String name) {
		if (name == null || name.isEmpty()) {
			return LOCAL_SEQ_INDEX;
		}
		return name.hashCode() & (LOCAL_SEQ_INDEX - 1);
	}

	private int localSeq(String name) {
		int hash = localHashIndex(name);
		int num = localSeqs.incrementAndGet(hash);
		if (num > 50000000) {
			localSeqs.weakCompareAndSet(hash, num, ThreadLocalRandom.current().nextInt(100));
		}
		return num;
	}

	private static long shortNowMills() {
		return System.currentTimeMillis() - FROMMILS;
	}

	private static long fullTime(long time) {
		return time + FROMMILS;
	}

	int sub(String name) {
		if (counter != null) {
			try {
				return counter.count(name);
			} catch (Exception e) {
				Log.printStack(e);
			}
		}
		int sub = (ThreadLocalRandom.current().nextInt(0x100) << 16);
		sub |= ((int) System.nanoTime()) & 0xFF00;
		return sub | (localSeq(name) & 0xFF);

	}

	public long next(String name) {
		if (name != null) {
			name = name.trim();
			if (name.isEmpty()) {
				name = null;
			}
		}
		long num = shortNowMills();
		num &= 0x7FFFFFFFFFL;
		num <<= 24;
		int sub = sub(name) & 0xFFFFFF;
		return num | sub;
	}

	public long next() {
		return next(null);
	}

	public static long getDate(long seq) {
		long num = seq & 0x7FFFFFFFFF000000L;
		num >>= 24;
		return fullTime(num);
	}

}
