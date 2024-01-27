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

import org.yx.util.SumkDate;

public final class LongTermSeqImpl extends AbstractSeq {

	public LongTermSeqImpl() {
		super();
	}

	public LongTermSeqImpl(long from) {
		super(from);
	}

	public long next(String name) {
		long sub = subNumber(name) & 0x7FFFFFL;
		return prefix(System.currentTimeMillis()) | sub;
	}

	private long prefix(long time) {
		long num = shortMills(time);
		num &= 0x1FFFFFFFFFFL;
		return num << 23;
	}

	public long getTimeMillis(long seq) {
		long num = seq & 0xFFFFFFFFFF800000L;
		num >>>= 23;
		return fullTime(num);
	}

	@Override
	public long low(SumkDate date) {
		return prefix(date.getTimeInMils());
	}

	@Override
	public long high(SumkDate date) {
		return prefix(date.getTimeInMils()) | 0x7FFFFF;
	}

}
