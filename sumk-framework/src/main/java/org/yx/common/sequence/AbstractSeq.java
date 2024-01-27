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
package org.yx.common.sequence;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.slf4j.Logger;
import org.yx.log.Log;

public abstract class AbstractSeq implements Seq {
	private static final long FROMMILS = 1420041600000L;
	private static final int LOCAL_SEQ_INDEX = 64;
	private AtomicIntegerArray localSeqs = new AtomicIntegerArray(LOCAL_SEQ_INDEX + 1);
	protected final long fromMils;
	protected SeqCounter counter;

	public AbstractSeq() {
		this(FROMMILS);
	}

	public AbstractSeq(long from) {
		this.fromMils = from;
		try {
			for (int i = 0; i < localSeqs.length(); i++) {
				localSeqs.set(i, ThreadLocalRandom.current().nextInt(256));
			}
		} catch (Exception e) {
			Log.get("sumk.seq").error(e.getLocalizedMessage(), e);
		}
	}

	protected int localHashIndex(String name) {
		if (name == null || name.isEmpty()) {
			return LOCAL_SEQ_INDEX;
		}
		return name.hashCode() & (LOCAL_SEQ_INDEX - 1);
	}

	protected int localSeq(String name) {
		int hash = localHashIndex(name);
		int num = localSeqs.incrementAndGet(hash);
		if (num > 0x3FFFFFFF) {
			localSeqs.compareAndSet(hash, num, ThreadLocalRandom.current().nextInt(100));
		}
		return num;
	}

	protected int subNumber(String name) {
		if (counter != null) {
			try {
				return counter.incr(name);
			} catch (Exception e) {
				Logger log = Log.get("sumk.seq");
				if (log.isTraceEnabled()) {
					log.trace(e.getLocalizedMessage(), e);
				} else {
					log.debug(e.toString());
				}
			}
		}
		int sub = (ThreadLocalRandom.current().nextInt(0x100) << 16);
		sub |= ((int) System.nanoTime()) & 0xFF00;
		return sub | (localSeq(name) & 0xFF);

	}

	public void setCounter(SeqCounter counter) {
		this.counter = counter;
	}

	public SeqCounter getCounter() {
		return this.counter;
	}

	protected final long shortMills(long time) {
		return time - fromMils;
	}

	protected long fullTime(long time) {
		return time + fromMils;
	}

	public long next() {
		return next(null);
	}
}
